---
title: Amplitude
sourceCodeUrl: 'https://github.com/expo/expo/tree/main/packages/expo-analytics-amplitude'
packageName: 'expo-analytics-amplitude'
---

import APISection from '~/components/plugins/APISection';
import {APIInstallSection} from '~/components/plugins/InstallSection';
import PlatformsSection from '~/components/plugins/PlatformsSection';

> **Deprecated.** This module will be removed in SDK 46. There will be no replacement that works with the classic build service (`expo build`) because [the classic build service has been superseded by **EAS Build**](https://blog.expo.dev/turtle-goes-out-to-sea-d334db2a6b60). With **EAS Build**, you should use the official [@amplitude/react-native](https://github.com/amplitude/Amplitude-ReactNative) instead.

**`expo-analytics-amplitude`** provides access to [Amplitude](https://amplitude.com/) mobile analytics which allows you track and log various events and data. This module wraps Amplitude's [iOS](https://github.com/amplitude/Amplitude-iOS) and [Android](https://github.com/amplitude/Amplitude-Android) SDKs. For a great example of usage, see the [Expo app source code](https://github.com/expo/expo/tree/main/home/api/Analytics.ts).

**Please note:** Session tracking may not work correctly when running Experiences in the main Expo app. It will work correctly if you create a standalone app. For example, the version logged when running experiences in the Expo app will be the [Expo app version](constants.md#constantsexpoversion). Whereas in standalone apps, the version set in **app.json** is used. For more information see [this issue on GitHub](https://github.com/expo/expo/issues/4720).

<PlatformsSection android emulator ios simulator />

## Installation

<APIInstallSection />

## API

```js
import * as Amplitude from 'expo-analytics-amplitude';
```

<APISection packageName="expo-analytics-amplitude" apiName="Amplitude" />
