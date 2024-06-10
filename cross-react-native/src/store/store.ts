import {combineReducers, configureStore, createSlice} from '@reduxjs/toolkit';
import {User} from '@src/auth/model.ts';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {persistReducer, persistStore} from 'redux-persist';

export interface AuthState {
  user?: User;
  isAuthenticated: boolean;
}

// Define a slice for authentication state
const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    isAuthenticated: false,
  },
  reducers: {
    login: (state, action) => {
      state.user = action.payload;
      state.isAuthenticated = true;
    },
    logout: state => {
      state.user = null;
      state.isAuthenticated = false;
    },
  },
});

// Export actions
export const {login, logout} = authSlice.actions;

// Selector function to retrieve authenticated user data
export const selectAuth = state => state.auth as AuthState;

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
  whitelist: ['auth'],
};

const persistedReducer = persistReducer(
  persistConfig,
  combineReducers({
    auth: authSlice.reducer,
  }),
);

// Create the Redux store
export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);
