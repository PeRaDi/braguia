import {Animated, Easing, FlatList, ListRenderItem, StyleSheet, View} from 'react-native';
import {ActivityIndicator, Card, IconButton, SegmentedButtons, Text,} from 'react-native-paper';
import * as React from 'react';
import {useEffect, useRef, useState} from 'react';
import {Media, Pin, RelPin} from '@model/models.ts';
import {pinDAO} from '@pins/PinDAO.ts';
import {GalleryComponent} from "@src/gallery/GalleryComponent.tsx";
import {EmptyListComponent} from "@shared/EmptyListComponent.tsx";
import GPSLocationUI from "@shared/GPSLocationUI.tsx";

const DetailsCard = ({
  pin,
}: {
  pin: Pin;
}) => {
  const [expanded, setExpanded] = useState(false);
  const [contentHeight, setContentHeight] = useState(0);
  const [expandIcon, setExpandIcon] = useState('chevron-down');
  const [imageUrl, setImageUrl] = React.useState<string | undefined>();

  const animation = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(animation, {
      toValue: expanded ? 1 : 0,
      duration: 300,
      easing: Easing.ease,
      useNativeDriver: false,
    }).start();

    Pin.firstImage(pin)
        .then(m => m?.fileUrl)
        .then(url => setImageUrl(url))
        .catch(e => {
          setImageUrl(undefined);
          console.error(e);
        });

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
      {imageUrl && <Card.Cover style={styles.cardCover} source={{ uri: imageUrl }} />}
      <View style={styles.spacing} />
      <Card.Content style={styles.cardContent}>
        <Text variant="titleLarge">{pin.name}</Text>
        <View style={styles.extraContent}>
          <View style={styles.cardExtra}>
            {/*<Text>Latitude: {pin.latitude}</Text>*/}
            {/*<Text>Longitude: {pin.longitude}</Text>*/}
            {/*<Text>Altitude: {pin.altitude} </Text>*/}
            <GPSLocationUI latitude={pin.latitude} longitude={pin.longitude} altitude={pin.altitude}></GPSLocationUI>
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

const GalleryView = ({ pin, navigation }: { pin: Pin, navigation: any  }) => {
  const [items, setItems] = useState<Media[]>([]);
  useEffect(() => {
    const fetch = async () => {
      try {
        const data = await pin.medias.fetch() as Media[];
        setItems(data);
      } catch (error) {
        console.error(error);
      }
    };
    fetch();
  }, [pin]);


  return (
      <GalleryComponent medias={items} navigation={navigation} />
  );
};

const RelPinsView = ({pin, navigation}: { pin: Pin, navigation: any }) => {
  const [items, setItems] = useState<RelPin[]>([]);

  useEffect(() => {
    const fetch = async () => {
      try {
        const data = await pin.rel_pins.fetch() as RelPin[];
        setItems(data);
      } catch (error) {
        console.error(error);
      }
    };
    fetch();
  }, [pin]);

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: 'column',
      marginTop: 10,
      marginHorizontal: 10,
    },
    card: {
      paddingVertical: 5,
      paddingHorizontal: 5,
      borderRadius: 2,
    },
    item: {
      flex: 1,
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
  });

  const renderItem: ListRenderItem<RelPin> = ({item}) => {
    return (
        <Card style={styles.card}>
          <Card.Content>
            <View style={styles.item}>
              <Text>{item.attrib}</Text>
              <Text>{item.value}</Text>
            </View>
          </Card.Content>
        </Card>
    );
  };

  return (
      <View style={styles.container}>
        <FlatList
            keyExtractor={item => item.id}
            data={items}
            renderItem={renderItem}
            ListEmptyComponent={<EmptyListComponent/>}
        />
      </View>
  );
};

export const PinDetailsComponent = ({ route, navigation }) => {
  const { pinId } = route.params;
  const [pin, setPin] = useState<Pin | null>();
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

  const renderView = () => {
    switch (selectedView) {
      case 'media':
        return <GalleryView pin={pin} navigation={navigation}/>;
      case 'relPin':
        return <RelPinsView pin={pin} navigation={navigation}/>;
      default:
        return <></>;
    }
  };

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
          {
            value: 'relPin',
            icon: 'transit-connection-variant',
            label: 'Outros',
          },
        ]}
        value={selectedView}
        onValueChange={setSelectedView}
      />
      <View style={styles.views}>
        <View style={styles.views}>{renderView()}</View>
      </View>
    </View>
  );
};

export default PinDetailsComponent;
