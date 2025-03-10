---
title: ImagePicker
sourceCodeUrl: 'https://github.com/expo/expo/tree/main/packages/expo-image-picker'
packageName: 'expo-image-picker'
---

import { ConfigClassic, ConfigReactNative, ConfigPluginExample, ConfigPluginProperties } from '~/components/plugins/ConfigSection';
import { AndroidPermissions, IOSPermissions } from '~/components/plugins/permissions';
import APISection from '~/components/plugins/APISection';
import {APIInstallSection} from '~/components/plugins/InstallSection';
import PlatformsSection from '~/components/plugins/PlatformsSection';
import Video from '~/components/plugins/Video';
import SnackInline from '~/components/plugins/SnackInline';

**`expo-image-picker`** provides access to the system's UI for selecting images and videos from the phone's library or taking a photo with the camera.

<Video file={"sdk/imagepicker.mp4"} loop={false} />

<PlatformsSection android emulator ios simulator web />

## Installation

<APIInstallSection />

## Configuration in app.json / app.config.js

<!-- update library name here -->

You can configure `expo-image-picker` using its built-in [config plugin](../../../guides/config-plugins.md) if you use config plugins in your project ([EAS Build](../../../build/introduction.md) or `expo run:[android|ios]`). The plugin allows you to configure various properties that cannot be set at runtime and require building a new app binary to take effect.

<ConfigClassic>

<!-- this is a standard type of message that will apply to many libraries. if there are no usage description keys or permissions, then you may want to say something else here -->

You can configure [the permissions for this library](#permissions) using [`ios.infoPlist`](../config/app.md#infoplist) and [`android.permissions`](../config/app.md#permissions).

</ConfigClassic>

<ConfigReactNative>

<!-- update library name and link here -->

Learn how to configure the native projects in the [installation instructions in the `expo-image-picker` repository](https://github.com/expo/expo/tree/main/packages/expo-image-picker#installation-in-bare-react-native-projects).

</ConfigReactNative>

<ConfigPluginExample>

<!-- add some example usage of the plugin. you don't need to provide every option -->

```json
{
  "expo": {
    "plugins": [
      [
        "expo-image-picker",
        {
          "photosPermission": "The app accesses your photos to let you share them with your friends."
        }
      ]
    ]
  }
}
```

</ConfigPluginExample>

<!-- look in the plugin directory for the library and see what the options are, then fill in the below table as needed. here's an example plugin: https://git.io/JKlrN -->

<ConfigPluginProperties properties={[
{ name: 'photosPermission', platform: 'ios', description: 'A string to set the NSPhotoLibraryUsageDescription permission message.', default: '"Allow $(PRODUCT_NAME) to access your photos"' },
{ name: 'cameraPermission', platform: 'ios', description: 'A string to set the NSCameraUsageDescription permission message.', default: '"Allow $(PRODUCT_NAME) to access your camera"' },
{ name: 'microphonePermission', platform: 'ios', description: 'A string to set the NSMicrophoneUsageDescription permission message.', default: '"Allow $(PRODUCT_NAME) to access your microphone"' }
]} />

## Usage

<SnackInline label='Image Picker' dependencies={['expo-image-picker']}>

```js
import React, { useState, useEffect } from 'react';
import { Button, Image, View, Platform } from 'react-native';
import * as ImagePicker from 'expo-image-picker';

export default function ImagePickerExample() {
  const [image, setImage] = useState(null);

  const pickImage = async () => {
    // No permissions request is necessary for launching the image library
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    console.log(result);

    if (!result.cancelled) {
      setImage(result.uri);
    }
  };

  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Button title="Pick an image from camera roll" onPress={pickImage} />
      {image && <Image source={{ uri: image }} style={{ width: 200, height: 200 }} />}
    </View>
  );
}
```

</SnackInline>

When you run this example and pick an image, you will see the image that you picked show up in your app, and something similar to the following logged to your console:

```javascript
{
  "cancelled":false,
  "height":1611,
  "width":2148,
  "uri":"file:///data/user/0/host.exp.exponent/cache/cropped1814158652.jpg"
}
```

## Using ImagePicker with AWS S3

Please refer to the [with-aws-storage-upload example](https://github.com/expo/examples/tree/master/with-aws-storage-upload). Follow [Amplify docs](https://docs.amplify.aws/) to set your project up correctly.

## Using ImagePicker with Firebase

Please refer to the [with-firebase-storage-upload example](https://github.com/expo/examples/tree/master/with-firebase-storage-upload). Make sure you follow the ["Using Firebase"](/guides/using-firebase/) docs to set your project up correctly.

## API

```js
import * as ImagePicker from 'expo-image-picker';
```

<APISection packageName="expo-image-picker" apiName="ImagePicker" />

## Permissions

### Android

<!-- look in the library AndroidManifest.xml and the config plugin to see which permissions are added -->

The following permissions are added automatically through the library **AndroidManifest.xml**.

<AndroidPermissions permissions={['CAMERA', 'READ_EXTERNAL_STORAGE', 'WRITE_EXTERNAL_STORAGE']} />

<!-- if no permissions required, just use this text: -->

<!-- _No permissions required_. -->

### iOS

<!-- look in the README and config plugin to see what usage descriptions required, add that here -->

The following usage description keys are used by the APIs in this library.

<IOSPermissions permissions={[ 'NSMicrophoneUsageDescription', 'NSPhotoLibraryUsageDescription', 'NSCameraUsageDescription' ]} />

<!-- if no permissions required, just use this text: -->

<!-- _No usage description required_. -->

