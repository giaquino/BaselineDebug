# BaselineDebug

Draw an overlay baseline grid on your application for checking if your layouts are aligned.

![BaselineDebug](https://raw.githubusercontent.com/giaquino/BaselineDebug/master/assets/sample.png "BaselineDebug")

## Min SDK
BaselineDebug min sdk is API 16

## Getting started

In your root `build.gradle`:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
  }
```

In your `build.gradle`:

```gradle
dependencies {
  debugCompile "com.github.giaquino.BaselineDebug:library:1.0.0"
  releaseCompile "com.github.giaquino.BaselineDebug:library-noop:1.0.0"
  testCompile "com.github.giaquino.BaselineDebug:library-noop:1.0.0"
}
```

In your `Application` class:

```java
public class SampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    BaselineDebug.show(this);
  }
}
```
## TODO
- A way to toggle on/off the baseline while using the application
- A way to change color of the baseline
- A way to set color for every 4dp, 8dp, 16dp... baseline
