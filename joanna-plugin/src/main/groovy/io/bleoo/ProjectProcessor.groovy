package io.bleoo

import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.file.FileTree

public class ProjectProcessor {

    Project project
    File outputDir
    String packageName

    public ProjectProcessor(Project project, File outputDir) {
        this.project = project
        //获取到BuildConfig类的路径
        this.outputDir = outputDir
    }

    public void processPackageName(String manifestFilePath) {
        File file = new File(manifestFilePath)
        GPathResult result = new XmlSlurper().parse(file)
        packageName = result['@package']
    }

    public void processLayoutXml(String layoutDirPath) {
        FileProcessor fileProcessor = new FileProcessor(outputDir, packageName)
        FileTree fileTree = project.fileTree(layoutDirPath)
        fileTree.each { File file ->
            println(file.absolutePath)
            GPathResult result = new XmlSlurper().parse(file)
            if (result != null) {
                ArrayList<GPathResult> views = result.'**'.findAll { GPathResult node -> node['@android:id'] != null && node['@android:id'] != '' }
                if (views != null || views.size() > 0) {
                    Map<String, String> viewMap = new HashMap<>()
                    views.each { view ->
                        String viewId = view['@android:id']
                        viewId = viewId.substring(viewId.indexOf('/') + 1)
                        viewMap.put(viewId, view.name())
                    }
                    if (viewMap.size() > 0) {
                        fileProcessor.createViewBinder(file.name, viewMap)
                    }
                }
            }
        }
    }
}