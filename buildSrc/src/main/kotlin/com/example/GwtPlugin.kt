package com.example

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

const val GWT_COMPILE_SOURCES_DEPENDENCIES_CONFIGURATION = "gwtCompileDependencies"

class GwtPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply<GwtLibPlugin>()

        project.configurations.resolvable(GWT_COMPILE_SOURCES_DEPENDENCIES_CONFIGURATION) {
            extendsFrom(
                project.configurations["api"],
                project.configurations["implementation"],
            )
        }

        project.tasks.register<CheckGwtSourcesTask>("checkGwtSources")
    }
}

