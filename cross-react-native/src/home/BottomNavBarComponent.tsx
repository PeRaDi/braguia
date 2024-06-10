import React, {useEffect, useState} from 'react';
import {ActivityIndicator, BottomNavigation} from 'react-native-paper';
import EnhancedTrailsComponent from '../trails/TrailsComponent';
import EnhancedPinsComponent from '../pins/PinsComponent';
import SettingsComponent from '../settings/SettingsComponent';
import {useSelector} from 'react-redux';
import {selectAuth} from '@store/store.ts';
import {StyleSheet, View} from "react-native";

export interface RouteItem {
  key: string;
  title: string;
  focusedIcon: string;
}



const BottomNavBarComponent = ({navigation}) => {
  // Retrieve user data from Redux store
  const {isAuthenticated} = useSelector(selectAuth);

  const [index, setIndex] = React.useState(0);
  // const [routes, setRoutes] = React.useState([
  //   {
  //     key: 'trails',
  //     title: 'Roteiros',
  //     focusedIcon: 'walk',
  //   },
  //   ...(isAuthenticated
  //     ? [
  //         {
  //           key: 'pins',
  //           title: 'Pontos de Interesse',
  //           focusedIcon: 'pin',
  //         },
  //       ]
  //     : []),
  //   {
  //     key: 'settings',
  //     title: 'Definições',
  //     focusedIcon: 'cog',
  //   },
  // ] as RouteItem[]);

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
      ...(isAuthenticated
          ? [
            {
              key: 'pins',
              title: 'Pontos de Interesse',
              focusedIcon: 'pin',
            },
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
  }, [isAuthenticated]);

  const renderScene = BottomNavigation.SceneMap({
    trails: () => <EnhancedTrailsComponent navigation={navigation} />,
    pins: () => <EnhancedPinsComponent navigation={navigation} />,
    settings: () => <SettingsComponent navigation={navigation} />,
  });

  if(routes.length == 0) {
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
