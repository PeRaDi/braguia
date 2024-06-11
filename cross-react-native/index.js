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
