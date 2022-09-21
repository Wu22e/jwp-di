package core.di;

import core.annotation.Bean;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;

import java.lang.reflect.Method;
import java.util.Set;

public class BeanDefinitionRegistry {

    private final BeanDefinitions methodBeanDefinitions;
    private final BeanDefinitions classBeanDefinitions;

    public BeanDefinitionRegistry() {
        this.methodBeanDefinitions = new BeanDefinitions();
        this.classBeanDefinitions = new BeanDefinitions();
    }

    public void registerClassPathBeans(Set<Class<?>> classPathBeanClasses) {
        for (Class<?> classPathBeanClass : classPathBeanClasses) {
            this.classBeanDefinitions.add(new BeanDefinition(classPathBeanClass));
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
            this.methodBeanDefinitions.add(new BeanDefinition(componentClass, method));
        }
    }

    public BeanDefinition getMethodBeanDefinition(Class<?> methodReturnType) {
        return this.methodBeanDefinitions.getMethodBeanDefinition(methodReturnType);
    }

    public Set<BeanDefinition> getClassBeanDefinitions() {
        return this.classBeanDefinitions.getBeanDefinitions();
    }

    public Set<Class<?>> getPreInstantiateClassBean() {
        return this.classBeanDefinitions.getPreInstantiateClassBean();
    }

    public Set<BeanDefinition> getMethodBeanDefinitions() {
        return this.methodBeanDefinitions.getBeanDefinitions();
    }
}
