# Changelog

## Unpublished

### 🛠 Breaking changes

### 🎉 New features

- Add new `Sound.setOnAudioSampleReceived` API to support streaming audio sample buffers in realtime. ([#14904](https://github.com/expo/expo/pull/14904), [#16075](https://github.com/expo/expo/pull/16075) by [@barthap](https://github.com/barthap))

### 🐛 Bug fixes

- On Android fix crashes caused by accessing player from the wrong thread ([#16611](https://github.com/expo/expo/pull/16611) by [@mnightingale](https://github.com/mnightingale))

### 💡 Others

- Extract `tolerances` param type definition, used across the package methods, to the separate type `AVPlaybackTolerance`. ([#16905](https://github.com/expo/expo/pull/16905) by [@Simek](https://github.com/Simek))

### ⚠️ Notices

- On Android bump `compileSdkVersion` to `31`, `targetSdkVersion` to `31` and `Java` version to `11`. ([#16941](https://github.com/expo/expo/pull/16941) by [@bbarthec](https://github.com/bbarthec))

## 11.1.0 — 2022-03-10

### 🐛 Bug fixes

- On iOS fix `pauseAsync` causing framedrops and being delayed by not disabling `AVAudioSession` when there is no need for it ([#15873](https://github.com/expo/expo/pull/15873) by [@hirbod](https://github.com/hirbod) and [@mnightingale](https://github.com/mnightingale))

## 11.0.1 — 2022-03-07

### 🐛 Bug fixes

- Fix local asset localUri not being used in development ([#16544](https://github.com/expo/expo/pull/16544) by [@mnightingale](https://github.com/mnightingale))

## 11.0.0 — 2022-03-03

### 🛠 Breaking changes

- Remove `Video` component's static constants `FULLSCREEN_UPDATE_PLAYER_WILL_RESENT`, `FULLSCREEN_UPDATE_PLAYER_DID_RESENT`, `FULLSCREEN_UPDATE_PLAYER_WILL_DISMISS`, `FULLSCREEN_UPDATE_PLAYER_DID_DISMISS` and replace them with new `VideoFullscreenUpdate` enum. ([#16059](https://github.com/expo/expo/pull/16059) by [@Simek](https://github.com/Simek))
- Remove `Video` component's static constants `RESIZE_MODE_CONTAIN`, `RESIZE_MODE_COVER`, `RESIZE_MODE_STRETCH`. Use `ResizeMode` enum values instead. ([#16059](https://github.com/expo/expo/pull/16059) by [@Simek](https://github.com/Simek))
- Remove deprecated `presentIOSFullscreenPlayer` and `dismissIOSFullscreenPlayer` method from `Video` component. ([#16059](https://github.com/expo/expo/pull/16059) by [@Simek](https://github.com/Simek))
- Remove deprecated `onIOSFullscreenUpdate` prop from `Video` component. ([#16059](https://github.com/expo/expo/pull/16059) by [@Simek](https://github.com/Simek))
- Remove unused `presentFullscreenPlayerAsync` method from `Video` component. ([#16059](https://github.com/expo/expo/pull/16059) by [@Simek](https://github.com/Simek))
- Remove `INTERRUPTION_MODE_*` constants in favor of `InterruptionModeAndroid` and `InterruptionModeIOS` enums. ([#16145](https://github.com/expo/expo/pull/16145) by [@Simek](https://github.com/Simek))
- On Android upgrade `com.google.android.exoplayer:*:2.9.2` (available from `jcenter()`) to `com.google.android.exoplayer:*:2.13.3` (available from `google()`). ([#16123](https://github.com/expo/expo/pull/16123) by [@bbarthec](https://github.com/bbarthec))

### 🎉 New features

- Add methods to get and set audio recording inputs. ([#15806](https://github.com/expo/expo/pull/15806) by [@computerjazz](https://github.com/computerjazz))

### 💡 Others

- Updated `@expo/config-plugins` from `4.0.2` to `4.0.14` ([#15621](https://github.com/expo/expo/pull/15621) by [@EvanBacon](https://github.com/EvanBacon))
- Add missing `AudioMode` type export. ([#16145](https://github.com/expo/expo/pull/16145) by [@Simek](https://github.com/Simek))

## 10.2.1 - 2022-02-01

### 🐛 Bug fixes

- Fix iOS build with Expo SDK 44 and React Native 0.65+. ([#15661](https://github.com/expo/expo/pull/15661) by [@schiller-manuel](https://github.com/schiller-manuel))
- Fix `Plugin with id 'maven' not found` build error from Android Gradle 7. ([#16080](https://github.com/expo/expo/pull/16080) by [@kudo](https://github.com/kudo))

## 10.2.0 — 2021-12-03

_This version does not introduce any user-facing changes._

## 10.1.3 — 2021-11-01

### 🐛 Bug fixes

- On Web, do not try to attach fullscreen listener, when component `ref` is `null`. ([#14713](https://github.com/expo/expo/pull/14713) by [@Simek](https://github.com/Simek))

### 💡 Others

- Update component types and event types. ([#14713](https://github.com/expo/expo/pull/14713) by [@Simek](https://github.com/Simek))
- Mark `status` as an optional field in `VideoFullscreenUpdateEvent` and `VideoReadyForDisplayEvent` types, becouse Web implementation do not return `status` in those events. ([#14713](https://github.com/expo/expo/pull/14713) by [@Simek](https://github.com/Simek))

## 10.1.2 — 2021-10-21

### 🐛 Bug fixes

- Fix require cycles ([#14820](https://github.com/expo/expo/pull/14820) by [@EvanBacon](https://github.com/EvanBacon))

## 10.1.1 — 2021-10-15

_This version does not introduce any user-facing changes._

## 10.1.0 — 2021-10-01

### 🐛 Bug fixes

- Fixed `JNI DETECTED ERROR IN APPLICATION: java_object == null in call to GetObjectClass from void versioned.host.exp.exponent.modules.api.reanimated.NativeProxy$EventHandler.receiveEvent` on Android. ([#14569](https://github.com/expo/expo/pull/14569) by [@lukmccall](https://github.com/lukmccall))

## 10.0.0 — 2021-09-28

### 🛠 Breaking changes

- Default audio recording settings on ios are now `extension: '.m4a'` and `outputFormat: RECORDING_OPTION_IOS_OUTPUT_FORMAT_MPEG4AAC` so as to ensure cross-platform compatibility. ([#13492](https://github.com/expo/expo/pull/13492) by [@actuallymentor](https://github.com/actuallymentor))
- Dropped support for iOS 11.0 ([#14383](https://github.com/expo/expo/pull/14383) by [@cruzach](https://github.com/cruzach))

### 🎉 New features

- [plugin] Added ability to disable microphone permission via `microphonePermission: false`. ([#13446](https://github.com/expo/expo/pull/13446) by [@EvanBacon](https://github.com/EvanBacon))
- Add web support for recording. ([#8721](https://github.com/expo/expo/pull/8721) by [@WazzaJB](https://github.com/WazzaJB) and [@mnightingale](https://github.com/mnightingale))
- Add permissions support for web. ([#8721](https://github.com/expo/expo/pull/8721) by [@mnightingale](https://github.com/mnightingale))
- Add Audio `usePermissions` hook from modules factory. ([#13851](https://github.com/expo/expo/pull/13851) by [@bycedric](https://github.com/bycedric))
- On iOS, add Audio metadata (title) updates via `setOnMetadataUpdate`. ([#14134](https://github.com/expo/expo/pull/14134) by [@dani-mp](https://github.com/dani-mp))

### 🐛 Bug fixes

- Fix inline playback on Safari iOS (web). ([#13628](https://github.com/expo/expo/pull/13628) by [@andreibarabas](https://github.com/andreibarabas) and [@IjzerenHein](https://github.com/IjzerenHein))
- Fix building errors from use_frameworks! in Podfile. ([#14523](https://github.com/expo/expo/pull/14523) by [@kudo](https://github.com/kudo))

### 💡 Others

- Migrated from `@unimodules/core` to `expo-modules-core`. ([#13745](https://github.com/expo/expo/pull/13745) by [@tsapeta](https://github.com/tsapeta))
- Updated `@expo/config-plugins` ([#14443](https://github.com/expo/expo/pull/14443) by [@EvanBacon](https://github.com/EvanBacon))

## 9.2.3 — 2021-06-30

### 🎉 New features

- [plugin] Added Android `android.permission.MODIFY_AUDIO_SETTINGS` permission. ([#13163](https://github.com/expo/expo/pull/13163) by [@EvanBacon](https://github.com/EvanBacon))
- Remove lodash and nullthrows. ([#12522](https://github.com/expo/expo/pull/12522) by [@EvanBacon](https://github.com/EvanBacon))
- Add new `Recording.createAsync` API for faster recording on iOS. ([#12294](https://github.com/expo/expo/pull/12294) by [@IjzerenHein](https://github.com/IjzerenHein))
- Add `keepAudioActiveHint` recording option to prevent deactivation of the Audio session when recording on iOS. ([#12294](https://github.com/expo/expo/pull/12294) by [@IjzerenHein](https://github.com/IjzerenHein))
- Allow video audio to continue to play in the background on iOS. ([#12950](https://github.com/expo/expo/pull/12950) by [@matt-oakes](https://github.com/matt-oakes))

### 🐛 Bug fixes

- Fixed the web Video Fullscreen APIs in Safari ([#12258](https://github.com/expo/expo/pull/12258) by [@elliotdickison](https://github.com/elliotdickison))
- Enable kotlin in all modules. ([#12716](https://github.com/expo/expo/pull/12716) by [@wschurman](https://github.com/wschurman))
- Fixed an issue with Audio Interruption Mode not correctly being set on Android. ([#13236](https://github.com/expo/expo/pull/13236) by [@matt-oakes](https://github.com/matt-oakes))

### 💡 Others

- Migrated from `unimodules-file-system-interface` and `unimodules-permissions-interface` to `expo-modules-core`. ([#12961](https://github.com/expo/expo/pull/12961) by [@tsapeta](https://github.com/tsapeta))

## 9.1.2 — 2021-04-13

_This version does not introduce any user-facing changes._

## 9.1.1 — 2021-03-31

_This version does not introduce any user-facing changes._

## 9.1.0 — 2021-03-10

### 🎉 New features

- Converted plugin to TypeScript. ([#11715](https://github.com/expo/expo/pull/11715) by [@EvanBacon](https://github.com/EvanBacon))
- Updated Android build configuration to target Android 11 (added support for Android SDK 30). ([#11647](https://github.com/expo/expo/pull/11647) by [@bbarthec](https://github.com/bbarthec))

### 🐛 Bug fixes

- Remove peerDependencies and unimodulePeerDependencies from Expo modules. ([#11980](https://github.com/expo/expo/pull/11980) by [@brentvatne](https://github.com/brentvatne))

## 9.0.0 — 2021-01-14

### ⚠️ Notices

- The package is now shipped with prebuilt binaries on iOS. You can read more about it on [expo.fyi/prebuilt-modules](https://expo.fyi/prebuilt-modules). ([#11224](https://github.com/expo/expo/pull/11224) by [@tsapeta](https://github.com/tsapeta))

### 🛠 Breaking changes

- Dropped support for iOS 10.0 ([#11344](https://github.com/expo/expo/pull/11344) by [@tsapeta](https://github.com/tsapeta))

### 🎉 New features

- Created config plugins ([#11538](https://github.com/expo/expo/pull/11538) by [@EvanBacon](https://github.com/EvanBacon))
- Add optional sound level information in `RecordingStatus` object described with `metering` key. Add `isMeteringEnabled` flag in `RecordingOptions` to enable computing this information. The flag is set to `true` by default in `RecordingOptions` presets (`RECORDING_OPTIONS_PRESET_HIGH_QUALITY`, `RECORDING_OPTIONS_PRESET_LOW_QUALITY`). ([#10759](https://github.com/expo/expo/pull/10759) by [@danieloi](https://github.com/danieloi))

### 🐛 Bug fixes

- Fixed minor syntax error in `AVManager`. ([#11375](https://github.com/expo/expo/pull/11375) by [@sjchmiela](https://github.com/sjchmiela))

## 8.7.0 — 2020-11-17

### 🐛 Bug fixes

- Fix orientation being returned incorrectly for videos in portrait mode in onReadyForDisplay on iOS. ([#10449](https://github.com/expo/expo/pull/10449) by [@lachenmayer](https://github.com/lachenmayer))
- Fix looping stops after 3 times on iOS. ([#10602](https://github.com/expo/expo/pull/10602) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix Audio.stopAndUnloadAsync not handling no-data on Android. ([#9877](https://github.com/expo/expo/pull/9877) by [@IjzerenHein](https://github.com/IjzerenHein))

## 8.6.0 — 2020-08-18

_This version does not introduce any user-facing changes._

## 8.5.0 — 2020-08-11

### 🐛 Bug fixes

- Fix progress events when no playback is active on Android. ([#9545](https://github.com/expo/expo/pull/9545) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix Video resizeMode not updated on Android. ([#9567](https://github.com/expo/expo/pull/9567) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix Video source always reloaded when changing props on Android. ([#9569](https://github.com/expo/expo/pull/9569) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix blank Video after unlocking screen. ([#9586](https://github.com/expo/expo/pull/9586) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix exception on Android when loading invalid Video source. ([#9596](https://github.com/expo/expo/pull/9596) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix Audio prepareToRecordAsync after it failed once on iOS. ([#9612](https://github.com/expo/expo/pull/9612) by [@IjzerenHein](https://github.com/IjzerenHein))
- Improve error-messages on iOS. ([#9618](https://github.com/expo/expo/pull/9618) by [@IjzerenHein](https://github.com/IjzerenHein))

## 8.4.1 — 2020-07-29

### 🐛 Bug fixes

- Removed unused and potentionally unsafe call on iOS. ([#9436](https://github.com/expo/expo/pull/9436) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix onReadyForDisplay not emitted for HLS streams/m3u8 files on iOS. ([#9443](https://github.com/expo/expo/pull/9443) by [@IjzerenHein](https://github.com/IjzerenHein))

## 8.4.0 — 2020-07-24

### 🐛 Bug fixes

- Fix stability issues when changing source and/or useNativeControls on iOS. ([#9381](https://github.com/expo/expo/pull/9381) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix stability issue due to player-item observers not cleaned up on iOS. ([#9350](https://github.com/expo/expo/pull/9350) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix audio recording not working after reload app in iOS. ([#9283](https://github.com/expo/expo/pull/9283) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix native fullscreen events not emitted on iOS. ([#9323](https://github.com/expo/expo/pull/9323) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix duplicate full-screen will-dismiss event on iOS. ([#9366](https://github.com/expo/expo/pull/9366) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix mem-leak when unmounting Video component on iOS. ([#9379](https://github.com/expo/expo/pull/9379) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix audio not resumable when app is in background on iOS (react-native-music-control usage). ([#9363](https://github.com/expo/expo/pull/9363) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix posterStyle warning. ([#9384](https://github.com/expo/expo/pull/9384) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix audio-session not de-activated after unloading sound on iOS. ([#9365](https://github.com/expo/expo/pull/9365) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fix debugger break in XCode when removing observations. ([#9334](https://github.com/expo/expo/pull/9334) by [@IjzerenHein](https://github.com/IjzerenHein))

## 8.3.0 — 2020-07-08

### 🎉 New features

- [av] Delete `prop-types` in favor of TypeScript. ([#8679](https://github.com/expo/expo/pull/8679) by [@EvanBacon](https://github.com/EvanBacon))
- [av] Directly import `createElement` from `react-native-web` for RNW v12 support. ([#8773](https://github.com/expo/expo/pull/8773) by [@EvanBacon](https://github.com/EvanBacon))

### 🐛 Bug fixes

- Allow playing media files embedded as resources in an Android APK. ([#8936](https://github.com/expo/expo/pull/8936) by [@esamelson](https://github.com/esamelson))

## 8.2.1 — 2020-05-29

_This version does not introduce any user-facing changes._

## 8.2.0 — 2020-05-27

### 🐛 Bug fixes

- Fix unable to call presentFullScreenPlayer twice. ([#8343](https://github.com/expo/expo/pull/8343) by [@IjzerenHein](https://github.com/IjzerenHein))
- Fixed multiplied callbacks in `expo-av` after replaying ([#7193](https://github.com/expo/expo/pull/7193) by [@mczernek](https://github.com/mczernek))
- Fixed `Plaback.loadAsync()` return type. ([#7559](https://github.com/expo/expo/pull/7559) by [@awinograd](https://github.com/awinograd))
- Fixed the adaptive streaming for exoplayer on android. ([#8380](https://github.com/expo/expo/pull/8363) by [@watchinharrison](https://github.com/watchinharrison))
