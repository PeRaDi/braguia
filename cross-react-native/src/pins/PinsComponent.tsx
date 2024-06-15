import React, { useEffect, useState, useCallback } from 'react';
import {
  FlatList,
  ListRenderItem,
  RefreshControl,
  StyleSheet,
  View,
} from 'react-native';
import InfoCard from '../shared/InfoCard';
import { Pin } from '@model/models.ts';
import { pinDAO } from '@pins/PinDAO.ts';
import { withObservables } from '@nozbe/watermelondb/react';
import { database } from '@model/database.ts';
import { Text } from 'react-native-paper';
import { placeholderURL } from 'app.json';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    marginTop: 10,
    marginHorizontal: 10,
  },
  pinCardExtra: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});

// Function to extract media URLs from a single pin where media type is "I"
const getMediaUrlsFromPin = (pin) => {
  return pin.media
    ? pin.media.filter(media => media.media_type === 'I').map(media => media.media_file)
    : [];
};

const PinCard = ({ pin, navigation }) => (
  <InfoCard
    key={pin.id}
    title={pin.name}
    description={pin.description}
    coverUri={getMediaUrlsFromPin(pin).length > 0 ? getMediaUrlsFromPin(pin)[0] : placeholderURL}
    onClick={() => navigation.navigate('PinDetails', { pinId: pin.id })}>
    <View style={styles.pinCardExtra}>
      <Text>Latitude: {pin.latitude}</Text>
      <Text>Longitude: {pin.longitude}</Text>
      <Text>Altitude: {pin.altitude}</Text>
    </View>
  </InfoCard>
);

const EnhancedPinCard = withObservables(['pin'], ({ pin }) => ({
  pin,
}))(PinCard);

export const PinsComponent = ({pins, navigation}: {pins: Pin[]; navigation: any}) => {
  const [refreshing, setRefreshing] = useState(false);

  const fetchPins = async () => {
    await pinDAO.fetchList();
  };

  useEffect(() => {
    fetchPins();
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchPins();
    setRefreshing(false);
  }, []);

  const renderItem: ListRenderItem<Pin> = ({ item }) => {
    return <EnhancedPinCard key={item.id} pin={item} navigation={navigation} />;
  };

  return (
    <View style={styles.container}>
      <FlatList
        keyExtractor={item => item.id}
        data={pins}
        renderItem={renderItem}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      />
    </View>
  );
};

const enhance = withObservables([], () => ({
  pins: database.collections.get(Pin.table).query(),
}));

const EnhancedPinsComponent = enhance(PinsComponent);

export default EnhancedPinsComponent;
