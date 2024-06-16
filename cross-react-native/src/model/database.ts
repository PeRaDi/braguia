import {Database} from '@nozbe/watermelondb';
import SQLiteAdapter from '@nozbe/watermelondb/adapters/sqlite';

import schema from '@model/schema';
import migrations from '@model/migrations';
import {models} from '@model/models';

const sqLiteAdapter = new SQLiteAdapter({
  schema,
  // migrations,
  dbName: 'braguiadb',
  jsi: false,
  onSetUpError: error => {
    // Database failed to load -- offer the user to reload the app or log out
    console.error(error);
  },
});

export const database = new Database({
  adapter: sqLiteAdapter,
  modelClasses: models,
});
