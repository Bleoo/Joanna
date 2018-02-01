package io.bleoo

import org.gradle.api.Plugin
import org.gradle.api.Project

class Joanna implements Plugin<Project>
{

    @Override
    void apply(Project project) {
        project.task('helloPluginTask') {
            group 'bleoo'
            doLast {
                println 'hell i am a task in plugin'
            }
        }
    }
}