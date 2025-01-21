plugins {
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

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