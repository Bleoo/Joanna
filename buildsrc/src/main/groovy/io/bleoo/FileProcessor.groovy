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

    static void createViewBinder(String xmlName, Map<String, String> viewMap, File outputDir) {
        String className = getClassNameByXmlName(xmlName)
//        String content = """package io.github.bleoo.joanna;\n"""
//        content += """import android.support.v7.app.AppCompatActivity;\n"""
//        content += """import android.widget.TextView;\n"""
//        content += """public class ${className} {\n"""
//        content += """public static TextView tv_text;\n"""
//        content += """    public static void bind(AppCompatActivity activity){\n"""
//        content += """        tv_text = (TextView)activity.findViewById(R.id.tv_text);\n"""
//        content += """    }\n"""
//        content += """}"""
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
                constructor.addStatement(viewId + " = (" + clazz + ")activity.findViewById(R.id." + viewId + ")")
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

    private static String getClassNameByXmlName(String xmlName) {
        return xmlName.substring(0, 1).toUpperCase() +
                xmlName.substring(1, xmlName.indexOf(".")) +
                VIEW_BINDER_SUFFIX
    }
}