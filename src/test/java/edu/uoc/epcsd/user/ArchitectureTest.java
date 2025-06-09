package edu.uoc.epcsd.user;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "edu.uoc.epcsd.user", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule is_hexagonal_arch =
            onionArchitecture()
                    .domainModels("edu.uoc.epcsd.user.domain..")
                    .domainServices("edu.uoc.epcsd.user.domain.service..")
                    .applicationServices("edu.uoc.epcsd.user.application..")
                    .adapter("persistence", "edu.uoc.epcsd.user.infrastructure.repository..")
                    .adapter("rest", "edu.uoc.epcsd.user.infrastructure.repository.rest..");

    @ArchTest
    public static final ArchRule services_implementations_have_good_naming =
            classes()
                    .that().resideInAPackage("edu.uoc.epcsd.user.domain.service..")
                    .and().areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("ServiceImpl");

}
