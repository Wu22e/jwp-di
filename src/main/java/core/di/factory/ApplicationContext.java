package core.di.factory;

import core.annotation.ComponentScan;
import core.di.AnnotatedBeanDefinitionReader;
import core.di.BeanDefinitionRegistry;
import core.di.ClassPathBeanDefinitionScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(Class<?>... componentClasses) {
        BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistry();

        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanDefinitionRegistry);
        reader.register(componentClasses);

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
        scanner.scan(getBasePackages(componentClasses));

        beanFactory = new BeanFactory(beanDefinitionRegistry);
        beanFactory.initialize();
    }

    private Object[] getBasePackages(Class<?>... componentClasses) {
        return Arrays.stream(componentClasses)
                .filter(componentClasse -> componentClasse.isAnnotationPresent(ComponentScan.class))
                .map(componentScan -> componentScan.getAnnotation(ComponentScan.class))
                .map(ComponentScan::value)
                .toArray();
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansAnnotatedWith(annotation);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return this.beanFactory.getBean(requiredType);
    }
}
