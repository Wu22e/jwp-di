package core.di;


import core.annotation.Bean;
import core.annotation.ComponentScan;
import core.annotation.Configuration;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;

import java.lang.reflect.Method;
import java.util.Set;

public class AnnotatedBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.registry = beanDefinitionRegistry;
    }

    public void register(Class<?>... componentClasses) {
        this.registry.registerConfigurationBeans(componentClasses);
    }

//    private void registerBean(Class<?> componentClass) {
//        if (!componentClass.isAnnotationPresent(Configuration.class)) {
//            return;
//        }
//
//        Set<Method> beanMethods = ReflectionUtils.getAllMethods(componentClass, ReflectionUtilsPredicates.withAnnotation(Bean.class));
//        for (Method method : beanMethods) {
//            BeanDefinition beanDefinition = new BeanDefinition(componentClass, method);
//            this.registry.registerBeanDefinition(method.getReturnType(), beanDefinition);
//        }
//
//        if (componentClass.isAnnotationPresent(ComponentScan.class)) {
//            ComponentScan componentScan = componentClass.getAnnotation(ComponentScan.class);
//            String[] basePackages = componentScan.value();
//        }
//    }
}
