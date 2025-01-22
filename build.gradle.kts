plugins {
    id("application")
    id("org.sonarqube") version "6.0.1.5171"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sonar {
    properties {
        property("sonar.projectKey", "ldap-project-key")
        property("sonar.host.url", "myHostUrl")
        property("sonar.token", "sqa_62c42906ff19b35da598e7535323248ad0cfd9ac")
    }
}


repositories {
    mavenCentral()
}


application {
    mainClass = "org.example.Main"
}

dependencies {
    implementation("com.unboundid:unboundid-ldapsdk:7.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.1")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


project.tasks["sonar"].dependsOn("build")