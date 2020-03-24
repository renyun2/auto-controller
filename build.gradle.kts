import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.springframework.boot") version "2.3.0.M3"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    war
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.spring") version "1.3.70"
}

group = "com.renyun"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.2")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.3.1.tmp")
    implementation("com.google.code.gson:gson:2.8.5")


    implementation("com.baomidou:mybatis-plus-generator:3.3.1")
    implementation("org.apache.velocity:velocity-engine-core:2.2")
    implementation("org.freemarker:freemarker:2.3.30")
    implementation("com.ibeetl:beetl:3.0.19.RELEASE")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}


tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("com.renyun.serivce")
    archiveClassifier.set("jdk13")
    archiveVersion.set("1.0.0")
    minimize()
    dependencies {
        include(dependency("org.springframework.boot:spring-boot-starter-web"))
        include(dependency("org.jetbrains.kotlin:kotlin-reflect"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
        include(dependency("org.mybatis.spring.boot:mybatis-spring-boot-starter"))
        include(dependency("org.springframework.boot:spring-boot-starter-tomcat"))
    }
    source
    isZip64 = true
}
