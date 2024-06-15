/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {database} from '@model/database';
import {DatabaseProvider} from '@nozbe/watermelondb/DatabaseProvider';
import {Provider} from 'react-redux';
import {persistor, store} from '@store/store';
import {PersistGate} from 'redux-persist/integration/react';
import notifee, {EventType} from '@notifee/react-native';

//this handler will listen to background events:
// notifee.onBackgroundEvent(async ({type, detail}) => {
//   const {notification, pressAction} = detail;
//   //log out notification data
//   console.log('type ', type);
//   console.log('notification data ', detail);
//
//   //Check if the user has pressed the notification
//   if (type === EventType.PRESS && pressAction.id === 'default') {
//     // Do some processing..
//     console.log('the default button was pressed');
//     notifee.registerForegroundService;
//     // Remove the notification after the event was registered.
//     await notifee.cancelNotification(notification.id);
//   }
// });

notifee.registerForegroundService(notification => {
  return new Promise((resolve, reject) => {
    // console.log("Foreground: ", notification);
    // return resolve();
    // notifee.onForegroundEvent(async ({type, detail}) => {
    //     console.log("Foreground: ", notification);
    //   if (type === EventType.ACTION_PRESS && detail.pressAction.id === 'stop') {
    //     await notifee.stopForegroundService();
    //   }
    // });
  });
});

const AppWrapper = () => (
  <DatabaseProvider database={database}>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <App />
      </PersistGate>
    </Provider>
  </DatabaseProvider>
);

AppRegistry.registerComponent(appName, () => AppWrapper);
