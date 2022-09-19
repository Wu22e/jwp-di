package core.di;

import java.lang.reflect.Method;
import java.util.Optional;

public class BeanDefinition {
    private Class<?> beanClass;
    private Method beanMethod;

    // todo : beanMethod 가 null 이면 classpath, null 이 아니면 configuration
    public BeanDefinition(Class<?> beanClass, Method beanMethod) {
        this.beanClass = beanClass;
        this.beanMethod = beanMethod;
    }

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Method getBeanMethod() {
        return this.beanMethod;
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }
}
