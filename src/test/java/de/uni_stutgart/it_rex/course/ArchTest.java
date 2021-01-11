package de.uni_stutgart.it_rex.course;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.uni_stutgart.it_rex.course");

        noClasses()
            .that()
                .resideInAnyPackage("de.uni_stutgart.it_rex.course.service..")
            .or()
                .resideInAnyPackage("de.uni_stutgart.it_rex.course.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..de.uni_stutgart.it_rex.course.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
