import {Animated, Easing, StyleSheet, View} from 'react-native';
import {
  ActivityIndicator,
  Card,
  IconButton,
  SegmentedButtons,
  Text,
} from 'react-native-paper';
import {useEffect, useRef, useState} from 'react';
import {Trail} from '@model/models.ts';
import {trailDAO} from '@trails/TrailDAO.ts';
import * as React from 'react';
import {formatDuration} from '@shared/utils.ts';

const DetailsCard = ({trail}: {trail: Trail}) => {
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
      marginTop: 10,
      borderRadius: 5,
    },
    cardCover: {
      borderRadius: 5,
      borderBottomLeftRadius: 0,
      borderBottomRightRadius: 0,
      height: 150,
    },
    spacing: {
      marginTop: 5,
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
      marginTop: 2,
    },
    cardExtra: {
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
  });

  return (
    <Card style={styles.card}>
      <Card.Cover style={styles.cardCover} source={{uri: trail.imageUrl}} />
      <View style={styles.spacing} />
      <Card.Content>
        <Text variant="titleLarge">{trail.name}</Text>
        <View style={styles.extraContent}>
          <View style={styles.cardExtra}>
            <Text>Duração: {formatDuration(trail.duration)}</Text>
            <Text>Dificuldade: {trail.difficulty} </Text>
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
      <Card.Actions>
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
  return (
    <>
      <Text>Pontos de interesse</Text>
    </>
  );
};

const MapsView = ({trail}: {trail: Trail}) => {
  return (
    <>
      <Text>Mapa</Text>
    </>
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
      paddingHorizontal: 10,
    },
    views: {
      marginTop: 10,
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
      <DetailsCard trail={trail} />
      <SegmentedButtons
        style={styles.views}
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
      {renderView()}
    </View>
  );
};
