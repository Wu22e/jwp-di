package core.di;

import java.lang.reflect.Method;
import java.util.Objects;

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

    public boolean isMethodEqual(Method method) {
        return this.beanMethod.equals(method);
    }

    public boolean isMethodBean() {
        return this.beanMethod != null;
    }

    public Class<?> methodReturnType() {
        return this.beanMethod.getReturnType();
    }

    public boolean isMethodReturnTypeEqual(Class<?> methodReturnType) {
        verifyNull(this.beanMethod);
        return this.beanMethod.getReturnType() == methodReturnType;
    }

    private void verifyNull(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("해당 빈의 정보는 클래스 타입 빈입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanDefinition that = (BeanDefinition) o;

        if (!beanClass.equals(that.beanClass)) return false;
        return Objects.equals(beanMethod, that.beanMethod);
    }

    @Override
    public int hashCode() {
        int result = beanClass.hashCode();
        result = 31 * result + (beanMethod != null ? beanMethod.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanClass=" + beanClass +
                ", beanMethod=" + beanMethod +
                '}';
    }

}
