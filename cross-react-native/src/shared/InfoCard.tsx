import * as React from 'react';
import {useState, useRef, useEffect} from 'react';
import {
  View,
  TouchableOpacity,
  Animated,
  Easing,
  StyleSheet,
} from 'react-native';
import {Card, Text, IconButton} from 'react-native-paper';

const InfoCard = ({
  title,
  description,
  coverUri,
}: {
  title: string;
  description: string;
  coverUri: string;
}) => {
  const [expanded, setExpanded] = useState(false);
  const [contentHeight, setContentHeight] = useState(0);
  const animation = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(animation, {
      toValue: expanded ? 1 : 0,
      duration: 300,
      easing: Easing.ease,
      useNativeDriver: false,
    }).start();
  }, [animation, expanded]);

  const handlePress = () => {
    setExpanded(!expanded);
  };

  const height = animation.interpolate({
    inputRange: [0, 1],
    outputRange: [0, contentHeight],
    extrapolate: 'clamp',
  });

  const rotate = animation.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '180deg'],
  });

  const styles = StyleSheet.create({
    spacing: {
      marginTop: 10,
    },
    ghostDescription: {
      position: 'absolute',
      top: -9999,
      opacity: 0,
    },
    descriptionContainer: {
      height,
      overflow: 'hidden',
    },
    expandPress: {
      flexDirection: 'row',
      alignItems: 'center',
    },
  });

  return (
    <Card>
      <Card.Cover source={{uri: coverUri}} />
      <View style={styles.spacing} />
      <Card.Content>
        <Text variant="titleLarge">{title}</Text>
        <View style={styles.spacing} />
        <View
          style={{...styles.ghostDescription}}
          onLayout={event => setContentHeight(event.nativeEvent.layout.height)}>
          <Text variant="bodyMedium">{description}</Text>
        </View>
        <Animated.View style={styles.descriptionContainer}>
          <Text variant="bodyMedium">{description}</Text>
        </Animated.View>
      </Card.Content>
      <Card.Actions>
        <TouchableOpacity onPress={handlePress} style={styles.expandPress}>
          <Animated.View style={{transform: [{rotate}]}}>
            <IconButton icon="chevron-down" size={24} />
          </Animated.View>
        </TouchableOpacity>
      </Card.Actions>
    </Card>
  );
};

export default InfoCard;
