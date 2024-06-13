import {Animated, Easing, Linking, StyleSheet, View} from 'react-native';
import {
  ActivityIndicator,
  Card,
  IconButton,
  SegmentedButtons,
  Text,
  Button,
} from 'react-native-paper';
import * as React from 'react';
import {useEffect, useRef, useState} from 'react';
import {Pin, Trail} from '@model/models.ts';
import {trailDAO} from '@trails/TrailDAO.ts';
import {formatDuration} from '@shared/utils.ts';
import MapView, {Marker} from 'react-native-maps';
import {Region} from 'react-native-maps/lib/sharedTypes';

const DetailsCard = ({
  trail,
  onNavigationPress,
}: {
  trail: Trail;
  onNavigationPress: () => void;
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
      <Card.Cover style={styles.cardCover} source={{uri: trail.imageUrl}} />
      <View style={styles.spacing} />
      <Card.Content style={styles.cardContent}>
        <Text variant="titleLarge">{trail.name}</Text>
        <View style={styles.extraContent}>
          <View style={styles.cardExtra}>
            <Text>Duração: {formatDuration(trail.duration)}</Text>
            <Text>Dificuldade: {trail.difficulty} </Text>
            <Button mode="text" icon="google-maps" onPress={onNavigationPress}>
              Iniciar roteiro
            </Button>
          </View>
        </View>
        <View style={styles.spacing} />
        <View
          style={{...styles.ghostDescription}}
          onLayout={event => setContentHeight(event.nativeEvent.layout.height)}>
          <Text variant="bodyMedium">{trail.description}</Text>
        </View>
        <Animated.View style={styles.descriptionContainer}>
          <Text variant="bodyMedium">{trail.description}</Text>
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

const PinsView = ({trail}: {trail: Trail}) => {
  const [list, setList] = useState([] as any[]);
  console.log('list', list.length);

  useEffect(() => {
    const fetch = async () => {
      try {
        // const data = await trail.edges.fetch();
        // const pin = await data[0].startPin.fetch();
        // console.log(`Edge`);
        // console.log(data[0].description);
        // console.log(`Start`);
        // console.log(pin);
        const data = await Trail.associatedMedias(trail);
        setList(data);
      } catch (error) {
        console.error(error);
      }
    };
    fetch();
  }, [trail]);

  return (
    <>
      <Text>Pontos de interesse</Text>
    </>
  );
};

const pinToURIParam = (pin: Pin): string => `${pin.latitude},${pin.longitude}`;

const pinsToGoogleMapsURI = (pins: Pin[]): string | null => {
  if (!pins || pins.length <= 1) {
    return null;
  }
  const count = pins.length;
  const origin = `&origin=${pinToURIParam(pins[0])}`;
  const destination = `&destination=${pinToURIParam(pins[count - 1])}`;
  const waypointsArray = [];
  for (let i = 1; i < count - 1; i++) {
    waypointsArray.push(pins[i]);
  }
  const waypoints =
    count === 2
      ? ''
      : '&waypoints=' + waypointsArray.map(p => pinToURIParam(p)).join('|');
  return `https://www.google.com/maps/dir/?api=1${origin}${destination}${waypoints}`;
};

const startGoogleMaps = async (pins: Pin[]): Promise<void> => {
  const uri = pinsToGoogleMapsURI(pins);
  if (uri) {
    try {
      await Linking.openURL(uri);
    } catch (error) {
      console.error(error);
    }
  }
};

const PinMarker = ({pin, onClick}: {pin: Pin; onClick?: () => void}) => {
  return (
    <Marker
      coordinate={{
        latitude: pin.latitude,
        longitude: pin.longitude,
      }}
      title={pin.name}
      description={pin.description}
      onPress={() => (onClick ? onClick() : null)}
    />
  );
};

const MapsView = ({trail}: {trail: Trail}) => {
  const [pins, setPins] = useState<Pin[]>([]);
  const [region, setRegion] = useState<Region | undefined>();

  useEffect(() => {
    const fetch = async () => {
      try {
        const data = await Trail.associatedPins(trail);
        setPins(data);

        if (data.length > 0) {
          const latitudes = data.map(p => p.latitude);
          const longitudes = data.map(p => p.longitude);
          const minLat = Math.min(...latitudes);
          const maxLat = Math.max(...latitudes);
          const minLng = Math.min(...longitudes);
          const maxLng = Math.max(...longitudes);
          setRegion({
            latitude: (minLat + maxLat) / 2,
            longitude: (minLng + maxLng) / 2,
            latitudeDelta: (maxLat - minLat) * 1.2,
            longitudeDelta: (maxLng - minLng) * 1.2,
          });
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetch();
  }, [trail]);

  if (!region) {
    return <></>;
  }

  return (
    <View style={mapStyles.container}>
      <MapView style={mapStyles.map} region={region}>
        {pins.map((pin, index) => (
          <PinMarker key={index} pin={pin} />
        ))}
      </MapView>
      <IconButton
        icon="google-maps"
        size={35}
        iconColor="blue"
        onPress={() => startGoogleMaps(pins)}
        style={{position: 'absolute', top: 0, right: 0}}
      />
    </View>
  );
};

const GalleryView = ({trail}: {trail: Trail}) => {
  return (
    <>
      <Text>Galeria</Text>
    </>
  );
};

export const TrailDetailsComponent = ({route, navigation}) => {
  const {trailId} = route.params;
  const [trail, setTrail] = useState(null as Trail | null);
  const [selectedView, setSelectedView] = React.useState('pontos');

  const startNavigation = async () => {
    if (!trail) {
      return;
    }
    await startGoogleMaps(await Trail.associatedPins(trail));
  };

  useEffect(() => {
    const fetch = async () => {
      setTrail(await trailDAO.findById(trailId));
    };
    fetch();
  }, [trailId]);

  if (!trail) {
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
    pontosInteresse: {},
  });

  const renderView = () => {
    switch (selectedView) {
      case 'mapa':
        return <MapsView trail={trail} />;
      case 'media':
        return <GalleryView trail={trail} />;
      default:
        return <PinsView trail={trail} />;
    }
  };

  return (
    <View style={styles.container}>
      <DetailsCard trail={trail} onNavigationPress={startNavigation} />
      <SegmentedButtons
        style={styles.segments}
        buttons={[
          {
            value: 'pontos',
            icon: 'walk',
            label: 'Interesses',
            style: styles.pontosInteresse,
          },
          {
            value: 'mapa',
            icon: 'google-maps',
            label: 'Mapa',
          },
          {
            value: 'media',
            icon: 'view-gallery',
            label: 'Galeria',
          },
        ]}
        value={selectedView}
        onValueChange={setSelectedView}
      />
      <View style={styles.views}>{renderView()}</View>
    </View>
  );
};

const mapStyles = StyleSheet.create({
  container: {
    // ...StyleSheet.absoluteFillObject,
    // justifyContent: 'flex-end',
    // alignItems: 'center',
    height: '100%',
  },
  map: {
    // ...StyleSheet.absoluteFillObject,
    // borderStyle: "solid",
    // borderWidth: 2,
    height: '100%',
  },
});
