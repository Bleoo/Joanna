package io.bleoo

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import org.gradle.api.Plugin
import org.gradle.api.Project

public class Joanna implements Plugin<Project> {

    void apply(Project project) {
        println 'Joanna plugin apply!'
        AppExtension android = project.extensions.getByType(AppExtension)

        if (project.plugins.hasPlugin(AppPlugin) || project.plugins.hasPlugin(LibraryPlugin)) {
            android.applicationVariants.all { ApplicationVariantImpl variant ->

                //创建一个task
                def createTaskName = variant.variantData.scope.getTaskName('generate', 'ViewBinder')
                def createTask = project.task(createTaskName)
                File outputDir = variant.variantData.scope.buildConfigSourceOutputDir
//                def createTask = project.tasks.create('generateViewBinder')
//                File outputDir = new File(project.buildDir.absolutePath
//                        + File.separator + 'generated'
//                        + File.separator + 'source'
//                        + File.separator + 'viewBinder')
//                createTask.outputs.dir(outputDir.absolutePath)
//                variant.registerJavaGeneratingTask(createTask, outputDir)
                //设置task要执行的任务
                createTask.doLast {
                    println 'Joanna plugin start!'

                    ProjectProcessor processor = new ProjectProcessor(project, outputDir)
                    String manifestFilePath = project.projectDir.absolutePath + File.separator +
                            'src' + File.separator +
                            'main' + File.separator +
                            'AndroidManifest.xml'
                    processor.processPackageName(manifestFilePath)
                    String layoutDirPath = project.projectDir.absolutePath + File.separator +
                            'src' + File.separator +
                            'main' + File.separator +
                            'res' + File.separator +
                            'layout'
                    processor.processLayoutXml(layoutDirPath)
                }
                //设置task依赖于生成BuildConfig的task，然后在生成BuildConfig后生成我们的类
                String generateBuildConfigTaskName = variant.variantData.scope.generateBuildConfigTask.name
                def generateBuildConfigTask = project.tasks.getByName(generateBuildConfigTaskName)
                if (generateBuildConfigTask) {
                    createTask.dependsOn(generateBuildConfigTask)
                    generateBuildConfigTask.finalizedBy(createTask)
                }
            }
        }

    }
}