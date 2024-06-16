import {combineReducers, configureStore, createSlice} from '@reduxjs/toolkit';
import {User} from '@src/auth/model.ts';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {persistReducer, persistStore} from 'redux-persist';
import {Pin} from "@model/models.ts";
import {stat} from "react-native-fs";

export interface AuthState {
  user?: User | null;
  isAuthenticated: boolean;
  isPremium: boolean;
  isStandard: boolean;
}

export interface LocationState {
  lastNotifiedPinId?: string | null;
  lastUpdatedTime?: number | null;
  lastNotifiedTime?: number | null;
  shouldNotify: boolean;
}

// Define a slice for authentication state
const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: undefined,
    isAuthenticated: false,
    isPremium: false,
    isStandard: false,
  } as AuthState,
  reducers: {
    login: (state, action) => {
      state.user = action.payload;
      state.isAuthenticated = true;
      state.isPremium = state.user?.user_type == 'Premium';
      state.isStandard = state.user?.user_type == 'Standard';
    },
    logout: state => {
      state.user = null;
      state.isAuthenticated = false;
      state.isPremium = false;
      state.isStandard = false;
    },
  },
});

const locationSlice = createSlice({
  name: 'location',
  initialState: {
    shouldNotify: true
  } as LocationState,
  reducers: {
    notified: (state, action) => {
      const currentTime = new Date().getTime();
      if(!state.lastNotifiedTime) {
        state.lastNotifiedTime = currentTime;
        state.lastNotifiedPinId = action.payload;
        state.shouldNotify = true;
      } else {
        const currentId = state.lastNotifiedPinId;
        const elipsedSeconds = Math.abs(currentTime - state.lastNotifiedTime) / 1000;
        if (currentId != action.payload ||  elipsedSeconds > 30) {
          state.shouldNotify = true;
          state.lastNotifiedPinId = action.payload;
          state.lastNotifiedTime = currentTime;
        } else {
          state.shouldNotify = false;
        }
      }

      // console.log(state);
    }
  }
});

// Export actions
export const {login, logout} = authSlice.actions;
export const { notified } = locationSlice.actions;

// Selector function to retrieve authenticated user data
export const selectAuth = state => state.auth as AuthState;
export const selectLocation = state => state.location as LocationState;

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
  whitelist: ['auth', 'location'],
};

const persistedReducer = persistReducer(
  persistConfig,
  combineReducers({
    auth: authSlice.reducer,
    location: locationSlice.reducer,
  }),
);

// Create the Redux store
export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);
