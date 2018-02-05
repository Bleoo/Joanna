package io.bleoo

import com.squareup.javapoet.*

import javax.lang.model.element.Modifier

public class FileProcessor {

    public final static String PACKAGE_NAME = 'io.github.bleoo.joanna'
    public final static String VIEW_BINDER_SUFFIX = '_ViewBinder'

    File outputDir
    String packageName

    public FileProcessor(File outputDir, String packageName) {
        this.outputDir = outputDir
        this.packageName = packageName
    }

    public void createViewBinder(String xmlName, Map<String, String> viewMap) {
        String className = getClassNameByXmlName(xmlName)
        Map<String, MethodSpec.Builder> cbMap = new HashMap<>()
        cbMap.put('Activity', getConstructorBuilder('Activity'))
        cbMap.put('Dialog', getConstructorBuilder('Dialog'))
        cbMap.put('View', getConstructorBuilder('View'))

        ClassName RClass = ClassTypeHelper.getR(packageName)
        ArrayList<FieldSpec> fieldList = new HashMap<>()
        viewMap.each { String viewId, String clazz ->
            ClassName classType = ClassTypeHelper.getViewClassType(clazz)
            if (classType != null) {
                FieldSpec fieldSpec = FieldSpec.builder(ClassTypeHelper.getViewClassType(clazz), viewId)
                        .addModifiers(Modifier.PUBLIC)
                        .build()
                fieldList.add(fieldSpec)

                cbMap.each { String target, MethodSpec.Builder builder ->
                    builder.addStatement(viewId + ' = (' + clazz + ')' + target.toLowerCase() + '.findViewById($T.id.' + viewId + ')', RClass)
                }
            }
        }

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addFields(fieldList)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)

        cbMap.each { String target, MethodSpec.Builder builder ->
            typeSpecBuilder.addMethod(builder.build())//在类中添加方法
        }

        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, typeSpecBuilder.build())
                .addFileComment('Generated code from Joanna. Do not modify!')
                .build()
        javaFile.writeTo(outputDir)
    }

    private MethodSpec.Builder getConstructorBuilder(String targetClass) {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassTypeHelper.getTargetClassType(targetClass), targetClass.toLowerCase())
        return constructor
    }

    private String getClassNameByXmlName(String xmlName) {
        return xmlName.substring(0, 1).toUpperCase() +
                xmlName.substring(1, xmlName.indexOf('.')) +
                VIEW_BINDER_SUFFIX
    }
}