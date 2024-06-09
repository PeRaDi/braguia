/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {database} from "@model/database";
import {DatabaseProvider} from "@nozbe/watermelondb/DatabaseProvider";

const AppWrapper = () => (
  <DatabaseProvider database={database}>
    <App />
  </DatabaseProvider>
);

AppRegistry.registerComponent(appName, () => AppWrapper);
