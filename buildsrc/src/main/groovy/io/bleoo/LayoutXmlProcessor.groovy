package io.bleoo

import com.android.build.gradle.internal.api.ApplicationVariantImpl
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.file.FileTree

public class LayoutXmlProcessor {

    Project project
    ApplicationVariantImpl variant
    File outputDir

    public LayoutXmlProcessor(Project project, ApplicationVariantImpl variant) {
        this.project = project
        this.variant = variant
        //获取到BuildConfig类的路径
        outputDir = variant.variantData.scope.buildConfigSourceOutputDir
    }

    public void processLayoutXml(String layoutPath) {
        FileTree fileTree = project.fileTree(layoutPath)
        fileTree.each { File file ->
            println(file.absolutePath)
            GPathResult result = new XmlSlurper().parse(file)
            if (result != null) {
                ArrayList<GPathResult> views = result.'**'.findAll { GPathResult node -> node['@android:id'] != null && node['@android:id'] != '' }
                if (views != null || views.size() > 0) {
                    println views.size()
                    Map<String, String> viewMap = new HashMap<>()
                    views.each { view ->
                        String viewId = view['@android:id']
                        viewId = viewId.substring(viewId.indexOf('/') + 1)
                        println viewId
                        println view.name()
                        viewMap.put(viewId, view.name())
                    }
                    FileProcessor.createViewBinder(file.name, viewMap, outputDir)
                }
            }
        }
    }
}