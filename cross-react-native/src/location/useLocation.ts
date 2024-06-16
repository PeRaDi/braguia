import {useEffect, useRef, useState} from 'react';
import {Alert, Linking, PermissionsAndroid, Platform, ToastAndroid,} from 'react-native';
import Geolocation, {GeoPosition} from 'react-native-geolocation-service';

import appConfig from 'app.json';
import {Pin} from '@model/models.ts';
import notifee, {AndroidCategory, AndroidImportance} from '@notifee/react-native';
import {database} from "@model/database.ts";
import {useSelector} from "react-redux";
import {notified, selectLocation, store} from "@store/store.ts";

const updateNotification = async (position: GeoPosition, pin: Pin, onlyAlertOnce: boolean = true) => {
  await notifee.displayNotification({
    id: 'location-tracking',
    title: 'Ponto de interesse',
    body: `${pin.name}`,
    data: {
      screen: 'PinDetails',
      params: pin.id,
    },
    android: {
      channelId: 'location',
      asForegroundService: true,
      color: '#ff0000',
      smallIcon: 'ic_launcher',
      category: AndroidCategory.RECOMMENDATION,
      onlyAlertOnce,
      actions: [
        {
          title: 'Ver',
          pressAction: {
            id: 'ver',
            launchActivity: 'default',
          },
        }
      ]
    },
  });
};

export const useLocation = () => {
  const [forceLocation, setForceLocation] = useState(true);
  const [highAccuracy, setHighAccuracy] = useState(true);
  const [locationDialog, setLocationDialog] = useState(true);
  const [significantChanges, setSignificantChanges] = useState(false);
  const [observing, setObserving] = useState(false);
  const [foregroundService, setForegroundService] = useState(true);
  const [useLocationManager, setUseLocationManager] = useState(false);
  const [location, setLocation] = useState<GeoPosition | null>(null);
  // const [notifiable, setNotifiable] = useState(true);
  // const { shouldNotify } = useSelector(selectLocation);

  const watchId = useRef<number | null>(null);

  useEffect(() => {
    return () => {
      stopLocationUpdates().then(() => null);
    };
  }, []);

  // useEffect(() => {
  //   setNotifiable(shouldNotify);
  // }, [shouldNotify]);

  const stopLocationUpdates = async () => {
    if (watchId.current !== null) {
      Geolocation.clearWatch(watchId.current);
      watchId.current = null;
      setObserving(false);
    }
    await notifee.stopForegroundService();
  };

  const hasPermissionIOS = async () => {
    const openSetting = () => {
      Linking.openSettings().catch(() => {
        Alert.alert('Unable to open settings');
      });
    };
    const status = await Geolocation.requestAuthorization('always');

    if (status === 'granted') {
      return true;
    }

    if (status === 'denied') {
      Alert.alert('Location permission denied');
    }

    if (status === 'disabled') {
      Alert.alert(
        `Turn on Location Services to allow "${appConfig.displayName}" to determine your location.`,
        '',
        [
          {text: 'Go to Settings', onPress: openSetting},
          {text: "Don't Use Location", onPress: () => {}},
        ],
      );
    }

    return false;
  };

  const hasLocationPermission = async () => {
    if (Platform.OS === 'ios') {
      return await hasPermissionIOS();
    }

    if (Platform.OS === 'android' && Platform.Version < 23) {
      return true;
    }

    const hasPermission = await PermissionsAndroid.check(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    );

    if (hasPermission) {
      return true;
    }

    const status = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    );

    if (status === PermissionsAndroid.RESULTS.GRANTED) {
      return true;
    }

    if (status === PermissionsAndroid.RESULTS.DENIED) {
      ToastAndroid.show(
        'Location permission denied by user.',
        ToastAndroid.LONG,
      );
    } else if (status === PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN) {
      ToastAndroid.show(
        'Location permission revoked by user.',
        ToastAndroid.LONG,
      );
    }

    return false;
  };

  const getLocation = async () => {
    const hasPermission = await hasLocationPermission();

    if (!hasPermission) {
      return;
    }

    Geolocation.getCurrentPosition(
      async position => {
        setLocation(position);
        await checkPinProximity(position);
      },
      error => {
        Alert.alert(`Code ${error.code}`, error.message);
        setLocation(null);
        console.log(error);
      },
      {
        accuracy: {
          android: 'high',
          ios: 'best',
        },
        enableHighAccuracy: highAccuracy,
        timeout: 15000,
        maximumAge: 10000,
        distanceFilter: 10,
        forceRequestLocation: forceLocation,
        forceLocationManager: useLocationManager,
        showLocationDialog: locationDialog,
      },
    );
  };

  const getLocationUpdates = async () => {
    const hasPermission = await hasLocationPermission();

    if (!hasPermission) {
      return;
    }

    if (Platform.OS === 'android' && foregroundService) {
      await startForegroundService();
    }

    setObserving(true);

    watchId.current = Geolocation.watchPosition(
      async position => {
        setLocation(position);
        await checkPinProximity(position);
      },
      error => {
        setLocation(null);
        console.log(error);
      },
      {
        accuracy: {
          android: 'high',
          ios: 'best',
        },
        enableHighAccuracy: highAccuracy,
        distanceFilter: 0,
        interval: 10000,
        fastestInterval: 5000,
        forceRequestLocation: forceLocation,
        forceLocationManager: useLocationManager,
        showLocationDialog: locationDialog,
        useSignificantChanges: significantChanges,
      },
    );
  };

  const startForegroundService = async () => {
    // store.dispatch(notified(null));
    // Create a channel for the notification
    const channelId = await notifee.createChannel({
      id: 'location',
      name: 'Location Tracking',
      importance: AndroidImportance.HIGH,
    });

    // Display the initial notification
    await notifee.displayNotification({
      id: 'location-tracking',
      title: 'Tracking your location',
      body: 'Your location is being tracked in the background.',
      android: {
        channelId,
        asForegroundService: true,
        color: '#ff0000',
        smallIcon: 'ic_launcher',
      },
    });
  };

  const checkPinProximity = async (position: GeoPosition) => {
    await realPinProximityCheck(position);
    // await testPinProximityCheck(position);
  };

  const realPinProximityCheck = async (position: GeoPosition) => {
    const pins = await database.collections.get<Pin>('pins').query().fetch();
    for (const pin of pins) {
      const latitude = pin.latitude;
      const longitude = pin.longitude;
      const distance = calculateDistanceHaversine(position, latitude, longitude);
      if (distance < 100) { // Check if within 100 meters
        await updateNotification(position, pin, true);
      }
    }
  };

  const testPinProximityCheck = async (position: GeoPosition) => {
    const pins = await database.collections.get<Pin>('pins').query().fetch();
    const selectedIndex = Math.floor(Math.random() * pins.length);
      // console.log({notifiable});
    for (const [index, pin] of pins.entries()) {
      // const selected = selectedIndex == index;
      const selected = 0 == index;
      const latitude = selected ? position.coords.latitude : pin.latitude;
      const longitude = selected ? position.coords.longitude : pin.longitude;
      const distance = calculateDistanceHaversine(position, latitude, longitude);
      if (distance < 100) { // Check if within 100 meters
        store.dispatch(notified(pin.id));
        // console.log({shouldNotify});
        // if(shouldNotify) {
          await updateNotification(position, pin, false);
        // }
      }
    }
  };

  const calculateDistanceHaversine = (position: GeoPosition, latitude: number, longitude: number): number => {
    const earthRadius = 6371e3; // Earth radius in meters

    const lat1 = radians(position.coords.latitude);
    const lon1 = radians(position.coords.longitude);
    const lat2 = radians(latitude);
    const lon2 = radians(longitude);

    const dLat = lat2 - lat1;
    const dLon = lon2 - lon1;

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1) * Math.cos(lat2) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadius * c;
  };

  function radians(degrees: number) {
    return degrees * Math.PI / 180;
  }

  return {
    forceLocation,
    setForceLocation,
    highAccuracy,
    setHighAccuracy,
    locationDialog,
    setLocationDialog,
    significantChanges,
    setSignificantChanges,
    observing,
    setObserving,
    foregroundService,
    setForegroundService,
    useLocationManager,
    setUseLocationManager,
    location,
    getLocation,
    getLocationUpdates,
    stopLocationUpdates,
  };
};
