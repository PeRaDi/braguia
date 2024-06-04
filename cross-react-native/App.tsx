import React from 'react';
import {useColorScheme} from 'react-native';

import {SafeAreaProvider} from 'react-native-safe-area-context';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import BottomNavBarComponent from './src/navigation/BottomNavBarComponent';

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaProvider style={backgroundStyle}>
      <BottomNavBarComponent />
    </SafeAreaProvider>
  );
}

export default App;
