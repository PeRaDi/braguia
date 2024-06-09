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
import {withObservables} from '@nozbe/watermelondb/react';
import {database} from '@model/database.ts';

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

const TrailCard = ({trail}: {trail: Trail}) => (
  <InfoCard
    key={trail.id}
    title={trail.name}
    description={trail.description}
    coverUri={trail.imageUrl}>
    <View style={styles.trailCardExtra}>
      <Text>Duração: {formatDuration(trail.duration)}</Text>
      <Text>Dificuldade: {trail.difficulty} </Text>
    </View>
  </InfoCard>
);

const EnhancedTrailCard = withObservables(['trail'], ({trail}) => ({
  trail,
}))(TrailCard);

const TrailsComponent = ({trails}: {trails: Trail[]}) => {
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    trailDAO.fetchList({fromCache: true});
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await trailDAO.fetchList();
    setRefreshing(false);
  }, []);

  const renderItem: ListRenderItem<Trail> = ({item}) => {
    return <EnhancedTrailCard key={item.id} trail={item} />;
  };

  return (
    <View style={styles.container}>
      <FlatList
        keyExtractor={item => item.id}
        data={trails}
        renderItem={renderItem}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      />
    </View>
  );
};

const enhance = withObservables([], () => ({
  trails: database.collections.get(Trail.table).query(),
}));
const EnhancedTrailsComponent = enhance(TrailsComponent);

export default EnhancedTrailsComponent;
