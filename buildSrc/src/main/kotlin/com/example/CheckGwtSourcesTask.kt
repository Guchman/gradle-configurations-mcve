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
//            .filter {  it.name.endsWith("gwt-sources.jar") } // invariants will pass if uncomment

        checkGwtInvariants(gwtConfigurationJars)
        checkOnlyNonGwt(project.configurations["runtimeClasspath"])
        checkOnlyNonGwt(project.configurations["compileClasspath"])
    }

    private fun checkOnlyNonGwt(configuration: Configuration) {
        val jars = configuration.resolve()
        if (jars.isEmpty()) throw GradleException("Jars are empty for [${configuration.name}] configuration")

        val noGwtSources = jars.none {
            it.name.contains("gwt-sources")
        }
        if (!noGwtSources) throw GradleException("Gwt jars found at [${configuration.name}] configuration")
    }

    private fun checkGwtInvariants(resolved: FileCollection) {
        resolved.forEach {
            println("GWT DEPENDENCY: $it")
        }

        if (resolved.map { it.name }.sorted() == listOf("lib-B-gwt-sources.jar", "lib-A-gwt-sources.jar")) {
            throw GradleException("Not all expected GWT dependencies among ${resolved.map { it.name }}")
        }
    }
}