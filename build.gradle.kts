import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
	kotlin("plugin.jpa") version "1.5.10"
	kotlin("kapt") version "1.5.10"
}

apply {
	from("gradle/docker.gradle")
}

group = "com.somacode"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR8"

dependencies {

	// CORE
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// SECURITY
	implementation("org.springframework.cloud:spring-cloud-starter-oauth2")
	implementation("org.springframework.security:spring-security-config")
	implementation("org.springframework.security:spring-security-data")
	implementation("org.springframework.security:spring-security-web")

	// KOTLIN
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// DATABASE
	runtimeOnly("com.h2database:h2")
	runtimeOnly("mysql:mysql-connector-java")
	implementation("org.hibernate:hibernate-jpamodelgen")
	kapt("org.hibernate:hibernate-jpamodelgen")

	// SWAGGER
	implementation("io.springfox:springfox-swagger2:2.9.2")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")
	implementation("io.springfox:springfox-bean-validators:2.9.2")
	implementation("com.google.guava:guava:20.0")

	// RETROFIT
	implementation( "com.squareup.retrofit2:retrofit:2.9.0")
	implementation( "com.squareup.retrofit2:converter-gson:2.9.0")

	// RX
	implementation("io.reactivex.rxjava2:rxjava:2.1.9")
	implementation("io.reactivex.rxjava2:rxkotlin:2.2.0")

	// TOOL
	implementation("com.google.code.gson:gson:2.8.2")
	implementation("commons-io:commons-io:2.6")
	implementation("com.github.javafaker:javafaker:1.0.2")

	// TEST
	implementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
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
