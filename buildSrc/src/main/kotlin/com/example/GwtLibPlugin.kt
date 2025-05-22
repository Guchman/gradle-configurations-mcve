package com.example

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.register

const val GWT_SOURCES = "gwt"
const val GWT_SOURCES_CONFIGURATION = "gwtSourcesConfiguration"

val gwtSourcesArtifactAttribute = Attribute.of("sourcesSubset", String::class.java)

const val GWT_SOURCES_ATTRIBUTE_VALUE = "gwt"

class GwtLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val gwtSources = project.createAdditionalSources("main", GWT_SOURCES)
        val jar = project.tasks.register<Jar>("gwtSourcesJar") {
            archiveClassifier.set("gwt-sources")
            duplicatesStrategy = DuplicatesStrategy.WARN
        }
        project.configurations.consumable(GWT_SOURCES_CONFIGURATION) {
            attributes {
                attribute(gwtSourcesArtifactAttribute, GWT_SOURCES_ATTRIBUTE_VALUE)
            }
        }

        jar.configure {
            from(gwtSources)
        }
        project.artifacts.add(GWT_SOURCES_CONFIGURATION, jar)
    }

}
