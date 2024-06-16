import * as React from 'react';
import {useEffect, useRef, useState} from 'react';
import {Animated, Easing, StyleSheet, View} from 'react-native';
import {Card, Chip, IconButton, Text} from 'react-native-paper';

const InfoCard = ({
  title,
  description,
  coverUri,
  children,
  onInfoClick,
  hideInfoAction,
  visited,
  onVisitClick
}: {
  title: string;
  description: string;
  coverUri: string | undefined;
  children: any | undefined; // dynamic content
  onInfoClick?: () => void;
  hideInfoAction?: boolean;
  visited: boolean;
  onVisitClick: () => void;
}) => {
  const [expanded, setExpanded] = useState(false);
  const [contentHeight, setContentHeight] = useState(0);
  const [expandIcon, setExpandIcon] = useState('chevron-down');

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
    setExpandIcon(expanded ? 'chevron-down' : 'chevron-up');
    setExpanded(!expanded);
  };

  const height = animation.interpolate({
    inputRange: [0, 1],
    outputRange: [0, contentHeight],
    extrapolate: 'clamp',
  });

  const styles = StyleSheet.create({
    card: {
      marginTop: 10,
      borderRadius: 5,
    },
    cardCover: {
      borderRadius: 5,
      borderBottomLeftRadius: 0,
      borderBottomRightRadius: 0,
      height: 150,
    },
    spacing: {
      marginTop: 5,
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
      borderWidth: 0,
    },
    extraContent: {
      marginTop: 2,
    },
  });

  return (
    <Card style={styles.card}>
      {coverUri && <Card.Cover style={styles.cardCover} source={{uri: coverUri}} /> }
      <View style={styles.spacing} />
      <Card.Content>
        <Text variant="titleLarge">{title}</Text>
        {children && <View style={styles.extraContent}>{children}</View>}
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
        <View>
          <Chip icon={visited ? 'map-check' : 'bookmark-plus'} onPress={onVisitClick}>{visited ? 'Visitado' : 'Inédito'}</Chip>
        </View>
        {hideInfoAction === true ? (
          <></>
        ) : (
          <IconButton
            icon={'information-variant'}
            onPress={() => (onInfoClick ? onInfoClick() : null)}
            style={styles.expandPress}
          />
        )}
        <IconButton
            icon={expandIcon}
            onPress={handlePress}
            style={styles.expandPress}
        />
      </Card.Actions>
    </Card>
  );
};

export default InfoCard;
