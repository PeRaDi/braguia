import {combineReducers, configureStore, createSlice} from '@reduxjs/toolkit';
import {User} from '@src/auth/model.ts';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {persistReducer, persistStore} from 'redux-persist';

export interface AuthState {
  user?: User | null;
  isAuthenticated: boolean;
  isPremium: boolean;
  isStandard: boolean;
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
