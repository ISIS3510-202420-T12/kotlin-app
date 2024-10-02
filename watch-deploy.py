import time
import subprocess
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

class ChangeHandler(FileSystemEventHandler):
    def on_any_event(self, event):
        if event.is_directory:
            return None
        elif event.event_type in ('modified', 'created', 'deleted', 'moved'):
            self.build_and_deploy()

    def build_and_deploy(self):
        print("Change detected. Building and deploying the app...")

        gradle_process = subprocess.Popen(['cmd.exe', '/c', 'gradlew.bat installDebug'], cwd='.')
        gradle_process.wait()

        adb_process = subprocess.Popen(['cmd.exe', '/c', 'adb shell am start -n com.wearabouts/.MainActivity'], cwd='.')
        adb_process.wait()

if __name__ == "__main__":
    path_to_watch = "app/src/main/java"  # Adjust this path as needed
    event_handler = ChangeHandler()
    observer = Observer()
    observer.schedule(event_handler, path=path_to_watch, recursive=True)

    print(f"Watching for changes in {path_to_watch}. Press Ctrl+C to stop.")
    observer.start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()