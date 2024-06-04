import React from 'react';
import {FlatList, Text, View} from 'react-native';

function TrailsComponent(): React.JSX.Element {
  return (
    <View>
      <FlatList
        data={[{key: 'a'}, {key: 'b'}]}
        renderItem={({item}) => <Text>{item.key}</Text>}
      />
    </View>
  );
}

export default TrailsComponent;
