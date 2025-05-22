package com.example

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import java.io.File

val Project.sourceSets
    get() = extensions.getByType(SourceSetContainer::class)

val SourceSetContainer.main: SourceSet
    get() = getByName("main")

/**
 * Adds module in notation `com.example.lib` to the gwt compile sources.
 * They will be available for GWT compile task of dependant projects.
 */
fun Project.gwtSources(module: String) {
    val modulePath = module
        .removeSuffix("/")
        .removePrefix("/")
    val main = sourceSets.main
    (main.java.sourceDirectories + main.resources.sourceDirectories)
        .distinct()
        .forEach {
            gwtSources(it, modulePath)
        }
}

fun Project.gwtSources(rootDir: File, modulePath: String) {
    project.files(rootDir).distinct().forEach { println("Adding source file $it") }
    val mainAdditionalSources = getMainAdditionalSources(GWT_SOURCES)
    mainAdditionalSources.srcDirs(rootDir)
    mainAdditionalSources.filter.include("$modulePath/**")
    mainAdditionalSources.forEach {
        println("Adding source file $it")
        if (it.name != "SomeGwtStuff.kt")
            throw GradleException("Only gwt files allowed at sources")
    }
}

fun Project.getMainAdditionalSources(additionalSourcesName: String): SourceDirectorySet {
    return sourceSets.main.getAdditionalSources(additionalSourcesName)
}

fun Project.createAdditionalSources(
    sourcesSetName: String,
    additionalSourcesName: String
): SourceDirectorySet {
    val sourceDirectorySet =
        objects.sourceDirectorySet(additionalSourcesName, "[$additionalSourcesName] sources of [$name]")
    sourceSets.getByName(sourcesSetName)
        .extensions
        .add(additionalSourcesName, sourceDirectorySet)
    return sourceDirectorySet
}

fun SourceSet.getAdditionalSources(
    additionalSourcesName: String
): SourceDirectorySet {
    return extensions.getByName(additionalSourcesName) as SourceDirectorySet
}
