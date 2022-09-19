package core.di;

import core.annotation.Bean;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanDefinitionRegistry {

    private final Map<Class<?>, BeanDefinition> beanDefinitions;

    public BeanDefinitionRegistry() {
        this.beanDefinitions = new HashMap<>();
    }

    public void registerClassPathBeans(Set<Class<?>> classPathBeanClasses) {
        for (Class<?> classPathBeanClass : classPathBeanClasses) {
            this.beanDefinitions.put(classPathBeanClass, new BeanDefinition(classPathBeanClass));
        }
    }

    public void registerConfigurationBeans(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerConfigurationBean(componentClass);
        }
    }

    private void registerConfigurationBean(Class<?> componentClass) {
        Set<Method> beanMethods = ReflectionUtils.getAllMethods(componentClass, ReflectionUtilsPredicates.withAnnotation(Bean.class));

        for (Method method : beanMethods) {
            this.beanDefinitions.put(method.getReturnType(), new BeanDefinition(componentClass, method));
        }
    }

    public Set<Class<?>> getPreInstantiateBeans() {
        return this.beanDefinitions.keySet();
    }

    public BeanDefinition getBeanDefinition(Class<?> preInstantiateBean) {
        return this.beanDefinitions.get(preInstantiateBean);
    }
}
