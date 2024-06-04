import React from 'react';
import {FlatList, View} from 'react-native';
import {Text} from 'react-native-paper';

const PinsComponent = () => {
  return (
    <View>
      <FlatList
        data={[{key: 'Pins'}, {key: 'Component'}]}
        renderItem={({item}) => <Text>{item.key}</Text>}
      />
    </View>
  );
};

export default PinsComponent;
