import React, { useEffect, useState, useCallback } from 'react';
import {
  FlatList,
  ListRenderItem,
  RefreshControl,
  StyleSheet,
  View,
} from 'react-native';
import InfoCard from '../shared/InfoCard';
import {Media, Pin} from '@model/models.ts';
import { pinDAO } from '@pins/PinDAO.ts';
import { withObservables } from '@nozbe/watermelondb/react';
import { database } from '@model/database.ts';
import { Text } from 'react-native-paper';
import { placeholderURL } from 'app.json';
import {EmptyListComponent} from "@shared/EmptyListComponent.tsx";
import GPSLocationUI from "@shared/GPSLocationUI.tsx";

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

const PinCard = ({ pin, navigation }: {pin: Pin, navigation: any}) => {
  const [imageUrl, setImageUrl] = React.useState<string | undefined>();
  useEffect(() => {
    Pin.firstImage(pin)
        .then(m => m?.fileUrl)
        .then(url => setImageUrl(url))
        .catch(e => {
          setImageUrl(undefined);
          console.error(e);
        });
  }, []);
  return (
      <InfoCard
          key={pin.id}
          title={pin.name}
          description={pin.description}
          coverUri={imageUrl}
          onInfoClick={() => navigation.navigate('PinDetails', { pinId: pin.id, title: pin.name })}
          visited={pin.visited}
          onVisitClick={() => pin.setVisited(!pin.visited)}
      >
        <View style={styles.pinCardExtra}>
          {/*<Text>Latitude: {pin.latitude}</Text>*/}
          {/*<Text>Longitude: {pin.longitude}</Text>*/}
          {/*<Text>Altitude: {pin.altitude}</Text>*/}
          <GPSLocationUI latitude={pin.latitude} longitude={pin.longitude} altitude={pin.altitude}></GPSLocationUI>
        </View>
      </InfoCard>
  );
};

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
        ListEmptyComponent={<EmptyListComponent />}
      />
    </View>
  );
};

const enhance = withObservables([], () => ({
  pins: database.collections.get(Pin.table).query(),
}));

const EnhancedPinsComponent = enhance(PinsComponent);

export default EnhancedPinsComponent;
