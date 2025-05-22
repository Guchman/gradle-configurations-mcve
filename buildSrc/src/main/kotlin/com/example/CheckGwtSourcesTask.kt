package com.example

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get

abstract class CheckGwtSourcesTask : DefaultTask() {

    @TaskAction
    fun resolve() {
        val gwtConfigurationJars = project.configurations
            .getByName(GWT_COMPILE_SOURCES_DEPENDENCIES_CONFIGURATION)
            .incoming
            .artifactView {
                withVariantReselection()
                attributes {
                    attribute(gwtSourcesArtifactAttribute, GWT_SOURCES_ATTRIBUTE_VALUE)
                }
            }
            .files
//            .filter { it.name.endsWith("gwt-sources.jar") } // invariants will pass if uncomment

        checkGwtInvariants(gwtConfigurationJars)

        checkOnlyNonGwt(project.configurations["runtimeClasspath"])
        checkOnlyNonGwt(project.configurations["compileClasspath"])
    }

    private fun checkOnlyNonGwt(configuration: Configuration) {
        println("=== Checking ${configuration.name} sources invariants ===")
        val jars = configuration.resolve()
        jars.forEach {
            println("${configuration.name} dependency: ${it.name}")
        }
        if (jars.isEmpty()) throw GradleException("Jars are empty for [${configuration.name}] configuration")

        val noGwtSources = jars.none {
            it.name.contains("gwt-sources")
        }
        if (!noGwtSources) throw GradleException("Gwt jars found at [${configuration.name}] configuration")
    }

    private fun checkGwtInvariants(resolved: FileCollection) {
        val resolvedFiles = resolved.map { it.name }
        println("=== Checking gwt sources invariants ===")
        resolvedFiles
            .forEach {
                println("$GWT_COMPILE_SOURCES_DEPENDENCIES_CONFIGURATION dependency: $it")
            }

        if (resolvedFiles.toList().sorted() != listOf("lib-B-gwt-sources.jar", "lib-A-gwt-sources.jar").sorted()) {
            throw GradleException("Not all expected GWT dependencies among $resolvedFiles")
        }
    }
}