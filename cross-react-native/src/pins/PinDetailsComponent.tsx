import { Animated, Easing, Linking, StyleSheet, View } from 'react-native';
import {
  ActivityIndicator,
  Card,
  IconButton,
  SegmentedButtons,
  Text,
  Button,
} from 'react-native-paper';
import * as React from 'react';
import { useEffect, useRef, useState } from 'react';
import { Pin } from '@model/models.ts';
import { pinDAO } from '@pins/PinDAO.ts';

const DetailsCard = ({
  pin,
}: {
  pin: Pin;
}) => {
  const [expanded, setExpanded] = useState(false);
  const [contentHeight, setContentHeight] = useState(0);
  const [expandIcon, setExpandIcon] = useState('chevron-down');

  const animation = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(animation, {
      toValue: expanded ? 1 : 0,
      duration: 300,
      easing: Easing.ease,
      useNativeDriver: false,
    }).start();
  }, [animation, expanded]);

  const handlePress = () => {
    setExpandIcon(expanded ? 'chevron-down' : 'chevron-up');
    setExpanded(!expanded);
  };

  const height = animation.interpolate({
    inputRange: [0, 1],
    outputRange: [0, contentHeight],
    extrapolate: 'clamp',
  });

  const styles = StyleSheet.create({
    card: {
      marginTop: 5,
      borderRadius: 5,
    },
    cardCover: {
      borderRadius: 5,
      borderBottomLeftRadius: 0,
      borderBottomRightRadius: 0,
      height: 150,
    },
    cardContent: {},
    cardActionContainer: {
      height: 30,
    },
    spacing: {
      marginTop: 1,
    },
    ghostDescription: {
      position: 'absolute',
      top: -9999,
      opacity: 0,
    },
    descriptionContainer: {
      height,
      overflow: 'hidden',
    },
    expandPress: {
      flexDirection: 'row',
      alignItems: 'center',
      borderWidth: 0,
    },
    extraContent: {
      marginTop: 0,
    },
    cardExtra: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
  });

  return (
    <Card style={styles.card}>
      <Card.Cover style={styles.cardCover} source={{ uri: pin.media ? pin.media.find(media => media.media_type === 'I')?.media_file : null }} />
      <View style={styles.spacing} />
      <Card.Content style={styles.cardContent}>
        <Text variant="titleLarge">{pin.name}</Text>
        <View style={styles.extraContent}>
          <View style={styles.cardExtra}>
            <Text>Latitude: {pin.latitude}</Text>
            <Text>Longitude: {pin.longitude}</Text>
            <Text>Altitude: {pin.altitude} </Text>
          </View>
        </View>
        <View style={styles.spacing} />
        <View
          style={{ ...styles.ghostDescription }}
          onLayout={event => setContentHeight(event.nativeEvent.layout.height)}>
          <Text variant="bodyMedium">{pin.description}</Text>
        </View>
        <Animated.View style={styles.descriptionContainer}>
          <Text variant="bodyMedium">{pin.description}</Text>
        </Animated.View>
      </Card.Content>
      <Card.Actions style={styles.cardActionContainer}>
        <IconButton
          icon={expandIcon}
          onPress={handlePress}
          style={styles.expandPress}
        />
      </Card.Actions>
    </Card>
  );
};

const GalleryView = ({ pin }: { pin: Pin }) => {
  return (
    <>
      <Text>Galeria</Text>
    </>
  );
};

export const PinDetailsComponent = ({ route }) => {
  const { pinId } = route.params;
  const [pin, setPin] = useState(null as Pin | null);
  const [selectedView, setSelectedView] = React.useState('media');

  useEffect(() => {
    const fetch = async () => {
      setPin(await pinDAO.findById(pinId));
    };
    fetch();
  }, [pinId]);

  if (!pin) {
    return (
      <>
        <ActivityIndicator />
      </>
    );
  }

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      paddingHorizontal: 10,
    },
    segments: {
      marginTop: 5,
    },
    views: {
      flex: 1,
      marginTop: 5,
      width: '100%',
      height: 'auto',
    },
  });

  return (
    <View style={styles.container}>
      <DetailsCard pin={pin} />
      <SegmentedButtons
        style={styles.segments}
        buttons={[
          {
            value: 'media',
            icon: 'view-gallery',
            label: 'Galeria',
          },
        ]}
        value={selectedView}
        onValueChange={setSelectedView}
      />
      <View style={styles.views}>
        {selectedView === 'media' && <GalleryView pin={pin} />}
      </View>
    </View>
  );
};

export default PinDetailsComponent;
