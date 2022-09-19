package core.di.factory;

import com.google.common.collect.Maps;
import core.di.BeanDefinition;
import core.di.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private BeanDefinitionRegistry registry;
//    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(BeanDefinitionRegistry registry) {
        this.registry = registry;
//        this.preInstantiateBeans = registry.getPreInstantiateBeans();
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return this.beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
//        for (Class<?> preInstantiateBean : this.preInstantiateBeans) {
        for (Class<?> preInstantiateBean : this.registry.getPreInstantiateBeans()) {
            this.beans.put(preInstantiateBean, instantiateClassPathBean(preInstantiateBean, this.registry.getBeanDefinition(preInstantiateBean)));
        }
    }

    private Object instantiateClassPathBean(Class<?> preInstantiateBean, BeanDefinition beanDefinition) {
        Method beanMethod = beanDefinition.getBeanMethod();
        if (beanMethod == null) {
            return instantiateClassPathBean(preInstantiateBean);
        }
        return instantiateConfigurationBean(beanMethod, beanDefinition);
    }

    private Object instantiateClassPathBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return beans.get(preInstantiateBean);
        }
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);
        if (injectedConstructor == null) {
            return BeanUtils.instantiateClass(preInstantiateBean);
        }
        return instantiateConstructor(injectedConstructor);
    }

    private Object instantiateConfigurationBean(Method method, BeanDefinition beanDefinition) {
        try {
            if (beans.containsKey(beanDefinition.getBeanClass())) {
                return beans.get(beanDefinition.getBeanClass());
            }

            Object configClassInstance = BeanUtils.instantiateClass(beanDefinition.getBeanClass());
            if (method.getParameters().length == 0) {
                return method.invoke(configClassInstance);
            }

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                BeanDefinition parameterMethodBeanDefinition = this.registry.getBeanDefinition(parameter.getType());
                return instantiateConfigurationBean(parameterMethodBeanDefinition.getBeanMethod(), parameterMethodBeanDefinition);
            }

            Object[] args = getArguments(parameters);

            return method.invoke(configClassInstance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getArguments(Parameter[] parameters) {
        Object[] args = new Object[parameters.length];
        for (int idx = 0; idx < parameters.length; idx++) {
                args[idx] = beans.get(parameters[idx].getType());
        }
        return args;
    }

    private Object instantiateConstructor(Constructor<?> injectedConstructor) {
        return BeanUtils.instantiateClass(injectedConstructor, getInstantiatedParameters(injectedConstructor));
    }

    private Object[] getInstantiatedParameters(Constructor<?> injectedConstructor) {
        return Arrays.stream(injectedConstructor.getParameterTypes())
                .map(parameterType -> {
                    Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, this.registry.getPreInstantiateBeans());
                    return instantiateClassPathBean(concreteClass, this.registry.getBeanDefinition(concreteClass));
                }).toArray();
    }
}
