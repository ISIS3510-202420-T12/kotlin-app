clear

# Build and install the app on the phone
sudo ./gradlew installDebug && adb shell am start -n com.wearabouts/.MainActivity