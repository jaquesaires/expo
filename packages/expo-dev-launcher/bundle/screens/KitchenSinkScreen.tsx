import { Heading, Row, Spacer, View } from 'expo-dev-client-components';
import * as React from 'react';
import { ScrollView } from 'react-native';

import { BasicButton } from '../components/BasicButton';
import { FlatListLoader } from '../components/FlatList';
import { LoadMoreButton } from '../components/LoadMoreButton';
import { SafeAreaTop } from '../components/SafeAreaTop';
import { Toasts } from '../components/Toasts';
import { useToastStack } from '../providers/ToastStackProvider';

export function KitchenSinkScreen() {
  const toastStack = useToastStack();

  const [isLoading, setIsLoading] = React.useState(false);

  function onLoadMorePress() {
    setIsLoading(true);

    setTimeout(() => {
      setIsLoading(false);
    }, 1500);
  }

  function onWarningToastPress() {
    toastStack.push(() => <Toasts.Warning>Hey there!</Toasts.Warning>, { durationMs: 5000 });
  }

  function onErrorToastPress() {
    toastStack.push(() => <Toasts.Error>Hey there!</Toasts.Error>, { durationMs: 5000 });
  }

  return (
    <ScrollView>
      <SafeAreaTop />
      <View padding="medium">
        <Heading size="large">Kitchen Sink</Heading>

        <Spacer.Vertical size="medium" />

        <Heading>Load More Button</Heading>
        <LoadMoreButton isLoading={isLoading} onPress={onLoadMorePress} />

        <Spacer.Vertical size="medium" />

        <Heading>FlatList Loader</Heading>
        <FlatListLoader />

        <Spacer.Vertical size="medium" />

        <Heading>Basic Buttons</Heading>
        <Spacer.Vertical size="small" />

        <Row>
          <BasicButton label="Primary" type="primary" />
          <Spacer.Horizontal size="small" />
          <BasicButton label="Secondary" type="secondary" />
          <Spacer.Horizontal size="small" />
          <BasicButton label="Tertiary" type="tertiary" />
        </Row>
        <Spacer.Vertical size="small" />
        <Row>
          <BasicButton label="Disabled" type="disabled" />
          <Spacer.Horizontal size="small" />
          <BasicButton label="Ghost" type="ghost" />
          <Spacer.Horizontal size="small" />
          <BasicButton label="Transparent" type="transparent" />
        </Row>

        <Spacer.Vertical size="large" />

        <Heading>Toasts</Heading>
        <Spacer.Vertical size="small" />

        <Row>
          <Spacer.Vertical size="small" />
          <BasicButton label="Warning" onPress={onWarningToastPress} />

          <Spacer.Horizontal size="small" />

          <Spacer.Vertical size="small" />
          <BasicButton label="Error" onPress={onErrorToastPress} />
        </Row>
      </View>
    </ScrollView>
  );
}
