package core.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
public class BeanDefinition {
    private Class<?> type;
    private Set<Class<? extends Annotation>> annotations = Sets.newHashSet();
    private Constructor constructor;
    private List<BeanDefinition> children = Lists.newArrayList();

    @Builder
    public BeanDefinition(
        Class<?> type,
        List<Annotation> annotations,
        Constructor constructor,
        List<BeanDefinition> children
    ) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException();
        }

        this.type = type;

        if (!CollectionUtils.isEmpty(annotations)) {
            this.annotations.addAll(buildAnnotations(annotations));
        }

        this.constructor = constructor;

        if (!CollectionUtils.isEmpty(children)) {
            this.children.addAll(children);
        }
    }

    private Set<? extends Class<? extends Annotation>> buildAnnotations(List<Annotation> annotations) {
        return annotations
            .stream()
            .map(Annotation::annotationType)
            .collect(toSet());
    }
}