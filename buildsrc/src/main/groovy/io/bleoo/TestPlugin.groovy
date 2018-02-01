package io.bleoo

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestPlugin implements Plugin<Project> {

    void apply(Project project) {
        println 'hell i am a task in plugin'
        AppExtension android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MyTransform(project, android))

        if (project.plugins.hasPlugin(AppPlugin)) {
            android.applicationVariants.all { ApplicationVariantImpl variant ->

                //创建一个task
                def createTaskName = variant.variantData.scope.getTaskName("joanna", "Generate")
                def createTask = project.task(createTaskName)
                //设置task要执行的任务
                createTask.doLast {
                    String layoutPath = project.projectDir.absolutePath + File.separator +
                            "src" + File.separator +
                            "main" + File.separator +
                            "res" + File.separator +
                            "layout"
                    new LayoutXmlProcessor(project, variant).processLayoutXml(layoutPath)
                }
                //设置task依赖于生成BuildConfig的task，然后在生成BuildConfig后生成我们的类
                String generateBuildConfigTaskName = variant.variantData.scope.generateBuildConfigTask.name
                def generateBuildConfigTask = project.tasks.getByName(generateBuildConfigTaskName)
                if (generateBuildConfigTask) {
                    createTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy createTask
                }
            }
        }

    }
}