import React, {useEffect, useState} from 'react';
import {
  ListRenderItem,
  PermissionsAndroid,
  View,
  Text,
  StyleSheet,
} from 'react-native';
import Contacts from 'react-native-contacts';
import ContactCardComponent from './ContactCardComponent';
import {FlatList, GestureHandlerRootView} from 'react-native-gesture-handler';
import {Button, Snackbar, Surface} from 'react-native-paper';
import {useNavigation} from '@react-navigation/native';
import ContactService from './ContactService';

const styles = StyleSheet.create({
  surface: {
    alignItems: 'center',
    justifyContent: 'space-around',
    display: 'flex',
    flexDirection: 'row',
  },
});

const SelectContactComponent = () => {
  const [contacts, setContacts] = useState<Contacts.Contact[] | null>(null);
  const [activeSwitch, setActiveSwitch] = useState<String[]>([]);
  const [visible, setVisible] = useState(false);

  const onToggleSnackBar = () => setVisible(!visible);
  const onDismissSnackBar = () => setVisible(false);

  const navigation = useNavigation();

  const requestReadContactsPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_CONTACTS,
        {
          title: 'Read contacts permission.',
          message:
            'A melhor aplicação de roteiros do mundo pretende ler os teus contactos.',
          buttonNeutral: 'Depois vejo isso',
          buttonNegative: 'Cancelar',
          buttonPositive: 'Toma lá',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        getContactsFromDevice();
      } else {
        navigation.goBack();
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const getContactsFromDevice = () => {
    ContactService.getContactsFromDevice().then(contactList => {
      setContacts(contactList);
    });
  };

  useEffect(() => {
    requestReadContactsPermission();
    // Fetch selected contacts and set them as active
    const fetchSelectedContacts = async () => {
      const selectedContacts = await ContactService.getSelectedContacts();
      setActiveSwitch(selectedContacts);
    };
    fetchSelectedContacts();
  }, []);

  const handleToggleSwitch = (
    recordId: string,
    isOn: boolean,
    setIsOn: (arg0: boolean) => void,
  ) => {
    // Use a new array to avoid mutation issues
    let newContacts = [...activeSwitch];
    if (isOn) {
      newContacts = newContacts.filter(id => id !== recordId);
      setIsOn(false);
    } else if (activeSwitch.length < 3) {
      newContacts.push(recordId);
      setIsOn(true);
    } else {
      onToggleSnackBar();
    }
    // Update state with new array
    ContactService.clearSelectedContacts();
    ContactService.saveSelectedContacts(newContacts);
    setActiveSwitch(newContacts);
  };

  const renderItem: ListRenderItem<Contacts.Contact> = ({item}) => {
    if (item.phoneNumbers.length === 0) {
      return null;
    }
    const isSwitchOn = activeSwitch.includes(item.recordID); // Check if the contact is selected
    return (
      <ContactCardComponent
        recordId={item.recordID}
        name={item.displayName}
        number={item.phoneNumbers[0].number}
        avatar={item.thumbnailPath}
        onToggleSwitch={handleToggleSwitch}
        isSwitchOn={isSwitchOn} // Pass this prop to initialize the switch state
      />
    );
  };

  return (
    <GestureHandlerRootView>
      <View>
        <Surface style={styles.surface} elevation={4}>
          <Text>Contactos selecionados: {activeSwitch.length}</Text>
          <Button
            onPress={() => {
              navigation.goBack();
            }}>
            Voltar
          </Button>
        </Surface>
        {contacts ? (
          <FlatList
            data={contacts}
            renderItem={renderItem}
            keyExtractor={item => item.recordID}
          />
        ) : (
          <Text>Loading contacts...</Text>
        )}
      </View>
      <Snackbar
        visible={visible}
        onDismiss={onDismissSnackBar}
        action={{
          label: 'OK',
          onPress: () => {
            onToggleSnackBar();
          },
        }}>
        Só podem haver 3 contactos de emergência selecionados.
      </Snackbar>
    </GestureHandlerRootView>
  );
};

export default SelectContactComponent;
