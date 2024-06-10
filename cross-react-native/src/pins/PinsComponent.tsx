import React, { useCallback, useEffect, useState } from 'react';
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
import { Text } from 'react-native-paper';
import { formatDuration } from '@shared/utils.ts';
import { withObservables } from '@nozbe/watermelondb/react';
import { database } from '@model/database.ts';

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

const PinCard = ({ pin }: { pin: Pin }) => (
  <InfoCard
    key={pin.id}
    title={pin.name}
    description={pin.description}
    coverUri='http://192.168.85.186/media/se_de_braga_m0C5XV9.jpg'>
  </InfoCard>
);

const EnhancedPinCard = withObservables(['pin'], ({ pin }) => ({
  pin,
}))(PinCard);

const PinsComponent = ({ pins, navigation }: { pins: Pin[], navigation: any }) => {
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    console.log('------------------ use efects');
    pinDAO.fetchList({fromCache: true});
  }, []);

  const onRefresh = useCallback(async () => {
    console.log('------------------ onRefresh');
    setRefreshing(true);
    await pinDAO.fetchList();
    setRefreshing(false);
  }, []);

  const renderItem: ListRenderItem<Pin> = ({item}) => {
    return <EnhancedPinCard key={item.id} pin={item} />;
  };

/*  useEffect(() => {
    //console.log('Fetching pins from cache...');
    pinDAO.fetchList({ fromCache: true })
      .then((result) => {
        //console.log('Fetched pins from cache:', result[0]);
      })
      .catch((error) => {
        //console.error('Error fetching pins from cache:', error);
      });
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    //console.log('Refreshing pins...');
    try {
      const result = await pinDAO.fetchList();
      console.log('Refreshed pins:', result[0]);
    } catch (error) {
      console.error('Error refreshing pins:', error);
    }
    setRefreshing(false);
  }, []);

  const renderItem: ListRenderItem<Pin> = ({ item }) => {
    //console.log('-------------------------------------------Rendering pin item:', item);
    return <EnhancedPinCard key={item.id} pin={item} />;
  };
*/
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

const enhance = withObservables(['navigation'], () => ({
  pins: database.collections.get(Pin.table).query(),
}));
const EnhancedPinsComponent = enhance(PinsComponent);

export default EnhancedPinsComponent;
