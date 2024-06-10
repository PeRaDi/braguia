import React from 'react';
import {FlatList, ListRenderItem, View} from 'react-native';
import InfoCard from '../shared/InfoCard';

const cardsData = [
  {
    key: '1',
    title: 'Card title 1',
    description:
      'This is the extended description for card 1. It shows up when the card is expanded.This is the extended description for card 1. It shows up when the card is expanded.This is the extended description for card 1. It shows up when the card is expanded.This is the extended description for card 1. It shows up when the card is expanded.This is the extended description for card 1. It shows up when the card is expanded.This is the extended description for card 1. It shows up when the card is expanded.',
    coverUri: 'https://picsum.photos/700?random=1',
  },
  {
    key: '2',
    title: 'Card title 2',
    description:
      'This is the extended description for card 2. It shows up when the card is expanded.',
    coverUri: 'https://picsum.photos/700?random=2',
  },
  {
    key: '3',
    title: 'Card title 3',
    description:
      'This is the extended description for card 3. It shows up when the card is expanded.',
    coverUri: 'https://picsum.photos/700?random=3',
  },
  {
    key: '4',
    title: 'Card title 4',
    description:
      'This is the extended description for card 4. It shows up when the card is expanded.',
    coverUri: 'https://picsum.photos/700?random=4',
  },
  {
    key: '5',
    title: 'Card title 5',
    description:
      'This is the extended description for card 5. It shows up when the card is expanded.',
    coverUri: 'https://picsum.photos/700?random=5',
  },
];

const renderItem: ListRenderItem<{
  key: string;
  title: string;
  description: string;
  coverUri: string;
}> = ({item}) => {
  return (
    <InfoCard
      key={item.key}
      title={item.title}
      description={item.description}
      coverUri={item.coverUri}
    />
  );
};

const PinsComponent = ({navigation}) => {
  return (
    <View>
      <FlatList
        data={cardsData}
        renderItem={renderItem}
        keyExtractor={item => item.key}
      />
    </View>
  );
};

export default PinsComponent;
