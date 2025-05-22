import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`kotlin-dsl`
	`java-library`
}

repositories {
	mavenCentral()
}

tasks.withType<KotlinCompile> {
	compilerOptions.jvmTarget = JvmTarget.JVM_21
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}
