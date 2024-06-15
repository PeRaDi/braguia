import React, {useEffect, useState} from 'react';
import {ActivityIndicator, BottomNavigation} from 'react-native-paper';
import EnhancedTrailsComponent from '../trails/TrailsComponent';
import EnhancedPinsComponent from '../pins/PinsComponent';
import SettingsComponent from '../settings/SettingsComponent';
import {useSelector} from 'react-redux';
import {selectAuth} from '@store/store.ts';
import {StyleSheet, View} from 'react-native';
import EnhancedGalleryComponent from '@src/gallery/GalleryComponent';

export interface RouteItem {
  key: string;
  title: string;
  focusedIcon: string;
}

const BottomNavBarComponent = ({navigation}) => {
  // Retrieve user data from Redux store
  const {isPremium} = useSelector(selectAuth);

  const [index, setIndex] = React.useState(0);

  const [routes, setRoutes] = useState([
    {
      key: 'trails',
      title: 'Roteiros',
      focusedIcon: 'walk',
    },
    {
      key: 'settings',
      title: 'Definições',
      focusedIcon: 'cog',
    },
  ] as RouteItem[]);

  useEffect(() => {
    const newRoutes = [
      {
        key: 'trails',
        title: 'Roteiros',
        focusedIcon: 'walk',
      },
      ...(isPremium
        ? [
            {
              key: 'pins',
              title: 'Pontos de Interesse',
              focusedIcon: 'pin',
            },
            {
              key: 'gallery',
              title: 'Galeria',
              focusedIcon: 'view-gallery',
            }
          ]
        : []),
      {
        key: 'settings',
        title: 'Definições',
        focusedIcon: 'cog',
      },
    ];
    setRoutes(newRoutes);
    setIndex(0);
  }, [isPremium]);

  const renderScene = BottomNavigation.SceneMap({
    trails: () => <EnhancedTrailsComponent navigation={navigation} />,
    pins: () => <EnhancedPinsComponent navigation={navigation} />,
    settings: () => <SettingsComponent navigation={navigation} />,
    gallery: () => <EnhancedGalleryComponent navigation={navigation} />,
  });

  if (routes.length == 0) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#0000ff" />
      </View>
    );
  }

  return (
    <BottomNavigation
      navigationState={{index, routes}}
      onIndexChange={setIndex}
      renderScene={renderScene}
    />
  );
};

const styles = StyleSheet.create({
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default BottomNavBarComponent;
