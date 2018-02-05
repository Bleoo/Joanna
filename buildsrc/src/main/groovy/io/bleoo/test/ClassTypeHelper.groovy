package io.bleoo.test

import com.squareup.javapoet.ClassName

public class ClassTypeHelper {

    private static final String[] ANDROID_VIEW_PACKAGE_VIEWS = [
            'View', 'ViewGroup', 'ViewStub', 'TextureView', 'SurfaceView'
    ] as String[]

    public static ClassName getTargetClassType(String clazz) {
        switch (clazz) {
            case 'Activity':
                return ClassName.get('android.app', 'Activity')
                break
            case 'Dialog':
                return ClassName.get('android.app', 'Dialog')
                break
            case 'View':
                return ClassName.get('android.view', 'View')
                break
            default:
                return null
        }
    }

    public static ClassName getViewClassType(String clazz) {
        int index = clazz.lastIndexOf('.')
        if (index > 0) {
            String packageName = clazz.substring(0, index)
            String simpleName = clazz.substring(index + 1)
            return ClassName.get(packageName, simpleName)
        } else {
            if (clazz in ANDROID_VIEW_PACKAGE_VIEWS) {
                return ClassName.get('android.view', clazz)
            } else if (clazz == 'WebView') {
                return ClassName.get('android.webkit', clazz)
            } else {
                return ClassName.get('android.widget', clazz)
            }
        }
    }

    public static ClassName getR(String packageName) {
        return ClassName.get(packageName, 'R')
    }
}