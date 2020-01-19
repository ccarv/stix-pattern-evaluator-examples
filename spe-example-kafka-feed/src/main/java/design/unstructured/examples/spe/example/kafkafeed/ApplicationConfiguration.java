package design.unstructured.examples.spe.example.kafkafeed;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import design.unstructured.stix.evaluator.mapper.StixMapper;
import design.unstructured.stix.evaluator.mapper.annotations.StixEntity;
import design.unstructured.stix.evaluator.mapper.annotations.StixObject;

/**
 * ApplicationConfiguration
 */
@ConfigurationProperties("kafka-feed")
public class ApplicationConfiguration {

    private final String stixObjectPackage;

    private final String[] indicatorFiles;

    @ConstructorBinding
    public ApplicationConfiguration(String stixObjectPackage, String[] indicatorFiles) {
        this.stixObjectPackage = stixObjectPackage;
        this.indicatorFiles = indicatorFiles;
    }

    @Bean
    public StixMapper mapper() {
        return new StixMapper(this.getClassFromAnnotation());
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

}
