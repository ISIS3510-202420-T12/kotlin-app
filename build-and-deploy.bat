@echo off

:: Function to build and deploy the app
:build_and_deploy
    gradlew.bat installDebug && adb shell am start -n com.wearabouts/.MainActivity
    goto :eof

call :build_and_deploy