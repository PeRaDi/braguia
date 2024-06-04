import React from 'react';
import {BottomNavigation, Text} from 'react-native-paper';

const TrailsRoute = () => <Text>Trails</Text>;

const PinsRoute = () => <Text>Pins</Text>;

const SettingsRoute = () => <Text>Settings</Text>;

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
