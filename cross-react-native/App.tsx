import React from 'react';
import {useColorScheme} from 'react-native';

import {SafeAreaProvider} from 'react-native-safe-area-context';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import AppInformation from '@src/info/AppInformation.tsx';
import Home from '@home/Home.tsx';
import Login from "@src/auth/Login.tsx";
import {TrailDetailsComponent} from "@trails/TrailDetailsComponent.tsx";

const Stack = createNativeStackNavigator();

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <NavigationContainer>
      <SafeAreaProvider style={backgroundStyle}>
        <Stack.Navigator initialRouteName="Home">
          <Stack.Screen
            name="Home"
            component={Home}
            options={{headerShown: false}}
          />
          <Stack.Screen
            name="Login"
            component={Login}
            options={{headerShown: false}}
          />
          <Stack.Screen
            name="About"
            component={AppInformation}
            options={{title: 'Acerca da BraGuia'}}
          />
          <Stack.Screen
            name="TrailDetails"
            component={TrailDetailsComponent}
            options={{title: '', headerShown: false}}
          />
        </Stack.Navigator>
      </SafeAreaProvider>
    </NavigationContainer>
  );
}

export default App;
