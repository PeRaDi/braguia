import React from 'react';
import {BottomNavigation} from 'react-native-paper';
import TrailsComponent from './trails/TrailsComponent';
import PinsComponent from './pins/PinsComponent';
import SettingsComponent from './settings/SettingsComponent';

const TrailsRoute = () => <TrailsComponent />;

const PinsRoute = () => <PinsComponent />;

const SettingsRoute = () => <SettingsComponent />;

const BottomNavBarComponent = () => {
  const [index, setIndex] = React.useState(0);
  const [routes] = React.useState([
    {
      key: 'trails',
      title: 'Roteiros',
      focusedIcon: 'walk',
    },
    {
      key: 'pins',
      title: 'Pontos de Interesse',
      focusedIcon: 'pin',
    },
    {
      key: 'settings',
      title: 'Definições',
      focusedIcon: 'cog',
    },
  ]);

  const renderScene = BottomNavigation.SceneMap({
    trails: TrailsRoute,
    pins: PinsRoute,
    settings: SettingsRoute,
  });

  return (
    <BottomNavigation
      navigationState={{index, routes}}
      onIndexChange={setIndex}
      renderScene={renderScene}
    />
  );
};

export default BottomNavBarComponent;
