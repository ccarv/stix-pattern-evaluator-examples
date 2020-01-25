package design.unstructured.examples.spe.example.kafkafeed;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import design.unstructured.stix.evaluator.mapper.StixObservableMapper;
import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixObject;

/**
 * StixObservableMapperConfiguration
 */
@Service
public class StixObservableMapperConfiguration {

    private KafkaFeedConfiguration configuration;

    @Bean
    public StixObservableMapper mapper() {

        StixObservableMapper mapper = new StixObservableMapper(StixObservableMapperConfiguration
                .scanPackageForAnnotations(configuration.getStixObjectPackage(), Arrays.asList(StixEntity.class, StixObject.class)));

        mapper.addPathFilter("binary_ref");

        return mapper;
    }

    private static Set<Class<?>> scanPackageForAnnotations(String annotationPackage, List<Class<? extends Annotation>> annotations) {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        final Set<Class<?>> classes = new HashSet<>();

        // Scan for STIX Entity annotations
        for (Class<? extends Annotation> annotation : annotations) {
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        }
        scanner.findCandidateComponents(annotationPackage).forEach((bean) -> {
            try {
                classes.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException ex) {
            }
        });

        return classes;
    }

    @Autowired
    public void setConfiguration(KafkaFeedConfiguration configuration) {
        this.configuration = configuration;
    }

}
