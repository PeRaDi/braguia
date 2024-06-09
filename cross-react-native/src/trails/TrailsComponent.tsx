import React, {useCallback, useEffect, useState} from 'react';
import {
  FlatList,
  ListRenderItem,
  RefreshControl,
  StyleSheet,
  View,
} from 'react-native';
import InfoCard from '../shared/InfoCard';
import {Trail} from '@model/models.ts';
import {trailDAO} from '@trails/TrailDAO.ts';
import {Text} from 'react-native-paper';
import {formatDuration} from '@shared/utils.ts';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    marginTop: 10,
    marginHorizontal: 10,
  },
  trailCardExtra: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});

const TrailsComponent = () => {
  const [refreshing, setRefreshing] = useState(false);
  const [trailsData, setTrailsData] = useState([] as Trail[]);

  useEffect(() => {
    fetchFromDB().then(data => setTrailsData(data));
  }, []);

  const fetchFromDB = async () => {
    return await trailDAO.fetchList({fromCache: true});
  };

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    setTrailsData(await trailDAO.fetchList());
    setRefreshing(false);
  }, [refreshing]);

  const renderItem: ListRenderItem<Trail> = ({item}) => {
    return (
      <InfoCard
        key={item.name}
        title={item.name}
        description={item.description}
        coverUri={item.imageUrl}>
        <View style={styles.trailCardExtra}>
          <Text>Duração: {formatDuration(item.duration)}</Text>
          <Text>Dificuldade: {item.difficulty} </Text>
        </View>
      </InfoCard>
    );
  };

  return (
    <View style={styles.container}>
      <FlatList
        keyExtractor={item => item.name}
        data={trailsData}
        renderItem={renderItem}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      />
    </View>
  );
};

export default TrailsComponent;
