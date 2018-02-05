# Joanna

该插件思想确立，目标使用方式如下。它能正常运行，但是不够完美。

The idea of the plug-in is established, and the use of the target is as follows. It can run normally, but not perfect enough.

> activity_main.xml --> Activity_main_ViewBinder.java

不使用apt及注解的方式，在编译期，根据layout的xml生成viewBinder类

Not using APT and annotations, in the compilation period, generating viewBinder classes based on the XML of layout.

## Usage
在需要使用该插件的module的build.gradle中添加:

in the build.gradle of the module that needs to use this plug-in add:

```groovy
apply plugin: 'io.bleoo.Joanna'
```

在根目录下的build.gradle下添加:

In the build.gradle of the root directory add:

```groovy
buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.Bleoo:Joanna:1.0' //添加依赖
    }
}
```

你的layout下xml文件将会这样生成java类，并且使用它。

Your layout xml file will generate a java class that you can use it like this.

> activity_main.xml --> Activity_main_ViewBinder.java

```java
import io.github.bleoo.joanna.Activity_main_ViewBinder;

public class MainActivity extends AppCompatActivity {

    Activity_main_ViewBinder viewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewBinder = new Activity_main_ViewBinder(this);
        viewBinder.tv_text.setText("gradle initialize me");
    }
}
```

## License
Copyright 2018 Yang Liu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.