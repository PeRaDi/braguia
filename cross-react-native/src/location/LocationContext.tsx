import React, {createContext, useContext} from 'react';
import {useLocation} from './useLocation';

const LocationContext = createContext(null);

export const LocationProvider = ({children}) => {
  const location = useLocation();

  return (
    <LocationContext.Provider value={location}>
      {children}
    </LocationContext.Provider>
  );
};

export const useLocationContext = () => {
  return useContext(LocationContext);
};
