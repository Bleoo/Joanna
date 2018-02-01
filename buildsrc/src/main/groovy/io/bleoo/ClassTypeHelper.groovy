package io.bleoo

import com.squareup.javapoet.ClassName

public class ClassTypeHelper {

    public final static ClassName Activity_Type = ClassName.get("android.app", "Activity")
    public final static ClassName TextView_Type = ClassName.get("android.widget", "TextView")

    static Map<String, ClassName> classMap

    static {
        classMap = new HashMap<>()
        classMap.put("Activity", Activity_Type)
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