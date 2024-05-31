# Qurany

### The development are continuous in this project ⚒️ ⚒️

## Qurany : Simple Android Application aims to provide a way for listen to Holy Quran Online over 209 Quran Karim Reciters with ability to download suras.

## ScreenShot

| Reciters List                 | Reciter Sura list          | Holy Quran Sura audio player         | My Reciters List                |
|-------------------------------|----------------------------|--------------------------------------|---------------------------------|
| ![Reciters](/art/qurany1.png) | ![Suras](/art/qurany2.png) | ![Suraaudioplayer](/art/qurany3.png) | ![MyReciters](/art/qurany4.png) |

### Tech stack 3pr Libs Used in Qurany

* The entire app re-written with [Kotlin](https://kotlinlang.org/).
* [AndroidX](https://developer.android.com/jetpack/androidx).
* [ExoPlayer](https://github.com/google/ExoPlayer) main audio player.
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
  for asynchronous tasks.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency
  injection.💉
* [ReactiveNetwork](https://github.com/pwittchen/ReactiveNetwork) for observing/checking
  internet/network connection availability .
* [Retrofit](https://square.github.io/retrofit/) and [OkHttp](https://square.github.io/okhttp/) for
  network layer.
* [Chip Navigation Bar](https://github.com/ismaeldivita/chip-navigation-bar) Bottom Navigation mixed
  with Chips component.
* [TextPathView](https://github.com/totond/TextPathView) for animated text in splash screen.
* JetPack
    - LiveData.
    - Lifecycle.
    - ViewModel.
    - Room Persistence.

* Architecture
    - MVVM Architecture
    - Repository pattern

## Run the project

* You can run the project directly , the API used is free without any fees , full respect to its
  owner / owners.
* [Api EndPoint description](https://mp3quran.net/ar/api).

## Testing

* .....

### Coming changes :-

- ~~Replace RxJava with Coroutines & Flow.~~
  -~~Optimize The UI with the Material Component with simple animations.~~
- Expand the Functionality of the player by support play full play-list of sura's.
- ~~Replace Reactive Network lib that depend on RxJava with Coroutines & Flow API Solution.~~

### License

```
Copyright 2020 Nedal Hasan ABDALLAH (NedaluOf)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

```