package design.unstructured.examples.spe.example.kafkafeed;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import design.unstructured.stix.evaluator.mapper.StixObservableMapper;
import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixObject;

/**
 * ApplicationConfiguration
 */
@Configuration
@ConfigurationProperties("kafka-feed")
public class ApplicationConfiguration {

    private String stixObjectPackage;

    private String[] indicatorFiles;

    @Bean
    public StixObservableMapper mapper() {
        StixObservableMapper mapper = new StixObservableMapper(this.getClassFromAnnotation());
        mapper.addPathFilter("binary_ref");

        return mapper;
    }

    public Set<Class<?>> getClassFromAnnotation() {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        final Set<Class<?>> classes = new HashSet<>();

        // Scan for STIX Entity annotations
        scanner.addIncludeFilter(new AnnotationTypeFilter(StixEntity.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(StixObject.class));
        scanner.findCandidateComponents(getStixObjectPackage()).forEach((bean) -> {
            try {
                classes.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException ex) {
            }
        });

        return classes;
    }

    public String getStixObjectPackage() {
        return stixObjectPackage;
    }

    public String[] getIndicatorFiles() {
        return indicatorFiles;
    }

    public void setStixObjectPackage(String stixObjectPackage) {
        this.stixObjectPackage = stixObjectPackage;
    }

    public void setIndicatorFiles(String[] indicatorFiles) {
        this.indicatorFiles = indicatorFiles;
    }

}
