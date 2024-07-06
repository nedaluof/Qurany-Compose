# Qurany
## Qurany is a simple android app aims to provide a way to listen The Holy Quran Online/Offline with over 209 Quran Karim Reciters with the ability to download suras.

## ScreenShot

| Reciters List                 | Reciter Sura list          | Holy Quran Sura audio player         | Favorites List                 |
|-------------------------------|----------------------------|--------------------------------------|--------------------------------|
| ![Reciters](/art/qurany1.png) | ![Suras](/art/qurany2.png) | ![Suraaudioplayer](/art/qurany3.png) | ![Favorites](/art/qurany4.png) |

### Tech stack 3pr Libs Used in Qurany

* The entire app written with [Kotlin](https://kotlinlang.org/).
* [AndroidX](https://developer.android.com/jetpack/androidx).
* [Compose](https://developer.android.com/develop/ui/compose).
* [Media3](https://developer.android.com/media/media3) for media player.
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
  for asynchronous tasks.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency
  injection.💉
* [Retrofit](https://square.github.io/retrofit/) and [OkHttp](https://square.github.io/okhttp/)
  network layer.
* [Moshi](https://github.com/square/moshi) json serializer.
* [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle).
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).
* [Room Database](https://developer.android.com/training/data-storage/room) caching / offline mode.

## Architecture : MVVM + Data layer with repository pattern

<p align="center">
    <img src="https://github.com/nedaluof/Qurany-Compose/blob/master/art/app_arch.png?raw=true" width="700">
</p>

## Run the project

* You can run the project directly , the API used is free without any fees , full respect to its
  owner / owners.
* [Api EndPoint description](https://mp3quran.net/ar/api).


### Coming changes :-
- Expand the Functionality of the player by support play full play-list of sura's.

### License

```
Copyright 2024 Nedal Hasan ABDALLAH (NedaluOf)

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