package core.di;


public class AnnotatedBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.registry = beanDefinitionRegistry;
    }

    public void register(Class<?>... componentClasses) {
        this.registry.registerConfigurationBeans(componentClasses);
    }
}
