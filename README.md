# Korjector
![korjector](https://i.imgur.com/rlUetNe.png)

![License](https://img.shields.io/github/license/jetbrains/projector-client)
[![JetBrains incubator project](https://jb.gg/badges/incubator.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Building](https://github.com/SerVB/korjector/actions/workflows/build.yml/badge.svg)](https://github.com/SerVB/korjector/actions/workflows/build.yml)
[![Testing](https://github.com/SerVB/korjector/actions/workflows/test.yml/badge.svg)](https://github.com/SerVB/korjector/actions/workflows/test.yml)

![logo](https://i.imgur.com/AigeOJj.png)

**Korjector** is a [KorGE](https://github.com/korlibs/korge) game engine-based multiplatform client for [Projector](https://github.com/JetBrains/projector-server). An internal JetBrains Hackathon 2021 project to demonstrate that a Projector client can be created in less than 48 hours for any OS that supports decent canvas 2D API.

![demonstration](demo.gif)
[demonstration](demo.gif)


### Installation
If you want to build a project from the source, you should run the following commands
```shell
git clone https://github.com/SerVB/korjector.git
cd korjector
./gradlew build
```
also, you can download the latest release [here](https://github.com/SerVB/korjector/releases) or on [google drive](https://drive.google.com/drive/folders/1rGAI8BXe0JL4QsYpDq2KDfNuD5xycErK?usp=sharing)

### Running

Run a Projector server, for example, via Docker:

```shell
docker pull registry.jetbrains.team/p/prj/containers/projector-idea-c
docker run --rm -p 8887:8887 -it registry.jetbrains.team/p/prj/containers/projector-idea-c
```
Worked with [this projector version](https://github.com/JetBrains/projector-server/commit/40ab2aaea512604caad87f9641d832037cd9afc7)

After it, run this client.

```shell
./gradlew runJvm  # for Linux/Mac
.\gradlew.bat runJvm  # for Windows 
```

This will open a Java-based app locally. You can edit the URL (defaults to a locally launched Projector server) and connect to the server.

For some reason, the client hangs after some time of running, and it was no luck to debug it during the hackathon.

Also, KorGE allows compiling to JS (Web), Native, Android, and iOS targets, but we weren't able to handle them during the hackathon.

However, it's possible to install the app on iOS:
```shell
./gradlew iosBuildSimulatorDebug # Creates an APP file
./gradlew installIosSimulatorDebug # Installs an APP file in the simulator
./gradlew runIosSimulatorDebug # Runs the APP in the simulator
```

And Android:
```shell
./gradlew installAndroidDebug # Installs an APK in all the connected devices
./gradlew runAndroidEmulatorDebug # Runs the application in an emulator
```

#### Supported features

* Specify URL of the server before a connection
* Rendering of windows (basic shapes, images, fonts). Expect some rendering glitches: we still have them few in the original Projector
* Decorations of windows
* Mouse input
* Ping (currently logged to the console)

#### Unsupported features

We didn't have much time to implement all the features available in the original Projector web client, but it should be doable after some efforts.

* Keyboard input
* Clipboard sync between the client and the server
* Markdown preview
* Speculative typing prototype
* Resizing the whole window in real time (you can resize the window to the desired size before the connection though)
* HiDPI

## License
This project [MIT](LICENSE) licensed.
