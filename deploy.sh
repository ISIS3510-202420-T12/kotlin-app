clear

# Build and install the app on the phone
sudo ./gradlew installDebug && db shell am start -n com.wearabouts/.MainActivity