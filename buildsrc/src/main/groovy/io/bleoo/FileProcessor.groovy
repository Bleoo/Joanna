package io.bleoo

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

import javax.lang.model.element.Modifier

public class FileProcessor {

    public final static String PACKAGE_NAME = "io.github.bleoo.joanna"
    public final static String VIEW_BINDER_SUFFIX = "_ViewBinder"

    File outputDir
    String packageName

    public FileProcessor(File outputDir, String packageName){
        this.outputDir = outputDir
        this.packageName = packageName
    }

    public void createViewBinder(String xmlName, Map<String, String> viewMap) {
        String className = getClassNameByXmlName(xmlName)
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassTypeHelper.getClassType("Activity"), "activity")
        ArrayList<FieldSpec> fieldList = new HashMap<>()
        viewMap.each { String viewId, String clazz ->
            ClassName classType = ClassTypeHelper.getClassType(clazz)
            if(classType != null){
                FieldSpec fieldSpec = FieldSpec.builder(ClassTypeHelper.getClassType(clazz), viewId)
                        .addModifiers(Modifier.PUBLIC)
                        .build()
                fieldList.add(fieldSpec)
                constructor.addStatement(viewId + " = (" + clazz + ')activity.findViewById($T.id.' + viewId + ")",  ClassTypeHelper.getR(packageName))
            }
        }
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addFields(fieldList)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(constructor.build())  //在类中添加方法
                .build()
        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, typeSpec)
                .build()
        javaFile.writeTo(outputDir)
    }

    private String getClassNameByXmlName(String xmlName) {
        return xmlName.substring(0, 1).toUpperCase() +
                xmlName.substring(1, xmlName.indexOf(".")) +
                VIEW_BINDER_SUFFIX
    }
}