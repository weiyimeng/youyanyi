::打包之前先去配置Gradle环境变量
::配置方式见:
::huyan360/app/document/环境变量/Gradle/使用Gradle构建App
@echo off
cd ../../app

@echo on
echo start build
gradle assembleRelease