import React from 'react';
import {Image, StyleSheet} from 'react-native';
import {Divider, List, Surface, Switch} from 'react-native-paper';
import ContactCardProps from './types/ContactCardProps';

const ContactCardComponent = (props: ContactCardProps) => {
  const [isSwitchOn, setIsSwitchOn] = React.useState(false);

  const renderContactCardIcon = () => {
    if (props.avatar === '') {
      return <List.Icon style={styles.iconImage} icon="account" />;
    } else {
      return <Image source={{uri: props.avatar}} style={styles.iconImage} />;
    }
  };

  const renderSwitch = () => {
    return (
      <Switch
        value={isSwitchOn}
        onValueChange={() =>
          props.onToggleSwitch(props.recordId, isSwitchOn, setIsSwitchOn)
        }
      />
    );
  };

  return (
    <Surface>
      <List.Item
        style={styles.item}
        title={props.name}
        description={props.number}
        left={renderContactCardIcon}
        right={renderSwitch}
      />
      <Divider />
    </Surface>
  );
};

const styles = StyleSheet.create({
  iconImage: {
    width: 40,
    height: 40,
    borderRadius: 20, // Adjusted for a perfect circle
  },
  item: {
    marginLeft: 10,
    marginRight: 10,
  },
});

export default ContactCardComponent;
