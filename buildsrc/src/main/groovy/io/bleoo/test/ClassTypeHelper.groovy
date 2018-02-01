package io.bleoo.test

import com.squareup.javapoet.ClassName

public class ClassTypeHelper {

    public final static ClassName Activity_Type = ClassName.get("android.app", "Activity")
    public final static ClassName Dialog_Type = ClassName.get("android.app", "Dialog")
    public final static ClassName View_Type = ClassName.get("android.view", "View")
    public final static ClassName TextView_Type = ClassName.get("android.widget", "TextView")

    static Map<String, ClassName> classMap

    static {
        classMap = new HashMap<>()
        classMap.put("Activity", Activity_Type)
        classMap.put("Dialog", Dialog_Type)
        classMap.put("View", View_Type)
        classMap.put("TextView", TextView_Type)
    }

    public static ClassName getClassType(String clazz) {
        ClassName className = classMap.get(clazz, null)
        if (className == null) {
            int index = clazz.lastIndexOf(".")
            if(index < 0){
                return null
            }
            String packageName = clazz.substring(0, index)
            String simpleName = clazz.substring(index + 1)
            className = ClassName.get(packageName, simpleName)
        }
        return className
    }

    public static ClassName getR(String packageName){
        return ClassName.get(packageName, "R")
    }
}