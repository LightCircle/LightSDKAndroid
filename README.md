LightSDKAndroid
===============

## 已实现功能点
- http请求的异步封装
- 异步文件文件上传
- 二维码扫描

## 使用步骤
Light平台的Android SDK通过 aar 包这种方式提供，因此使用SDK的步骤主要分为两步

1. 通过代码build aar包
2. 在你的 Android 工程中使用build好的 aar包

### build arr包的步骤:

1. 将本工程clone后用 Android Studio 打开。
2. 在 Gradle 窗口中双击执行 assembleRelease 任务。
3. 在工程的 `LightSDKAndroid/LightSDK/build/outputs/aar` 路径下会发现 LightSDK-release.aar 文件,这个文件就是我们需要的 aar包文件。

### 再你的 Android 工程中使用 arr包的步骤:

（有空再写）
