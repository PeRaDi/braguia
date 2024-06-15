import React from 'react';
import {useColorScheme} from 'react-native';

import {SafeAreaProvider} from 'react-native-safe-area-context';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import AppInformation from '@src/info/AppInformation.tsx';
import Home from '@home/Home.tsx';
import Login from '@src/auth/Login.tsx';
import {TrailDetailsComponent} from '@trails/TrailDetailsComponent.tsx';
import PinDetailsComponent from '@pins/PinDetailsComponent.tsx';
import ProfileComponent from '@src/profile/ProfileComponent';
import SelectContactComponent from '@src/contacts/SelectContactComponent';
import {LocationSettingsComponent} from '@src/location/LocationSettingsComponent.tsx';
import {LocationProvider} from '@src/location/LocationContext';
import EmergencyCallComponent from '@src/contacts/EmergencyCallComponent';
import {ActivityIndicator} from "react-native-paper";
import {setNavigationRef} from "@src/location/notification.ts";

const Stack = createNativeStackNavigator();


function App(): React.JSX.Element {
    const isDarkMode = useColorScheme() === 'dark';
    const backgroundStyle = {
        backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    };

    // const [loading, setLoading] = useState(true);

    // async function bootstrap() {
    //     const initialNotification = await notifee.getInitialNotification();
    //
    //     if (initialNotification) {
    //         console.log('Notification caused application to open', initialNotification.notification);
    //         console.log('Press action used to open the app', initialNotification.pressAction);
    //     } else {
    //         console.log("No notification");
    //     }
    // }
    //
    // useEffect(() => {
    //     bootstrap().finally(() => setLoading(false));
    // }, []);
    //
    // if (loading) {
    //     return <View style={{
    //         flex: 1,
    //         flexDirection: "row",
    //         alignItems: "center",
    //         justifyContent: "center"
    //     }}><ActivityIndicator></ActivityIndicator></View>
    // }

    return (
        <NavigationContainer ref={setNavigationRef} fallback={<ActivityIndicator />}>
            <LocationProvider>
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
                        <Stack.Screen
                            name="PinDetails"
                            component={PinDetailsComponent}
                            options={{title: '', headerShown: false}}
                        />
                        <Stack.Screen
                            name="UserProfile"
                            component={ProfileComponent}
                            options={{title: '', headerShown: false}}
                        />
                        <Stack.Screen
                            name="SelectContact"
                            component={SelectContactComponent}
                            options={{title: '', headerShown: false}}
                        />
                        <Stack.Screen
                            name="LocationSettings"
                            component={LocationSettingsComponent}
                            options={{title: ''}}
                        />
                        <Stack.Screen
                            name="EmergencyCall"
                            component={EmergencyCallComponent}
                            options={{title: '', headerShown: false}}
                        />
                    </Stack.Navigator>
                </SafeAreaProvider>
            </LocationProvider>
        </NavigationContainer>
    );
}

export default App;
