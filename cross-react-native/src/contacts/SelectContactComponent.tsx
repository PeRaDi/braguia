import React, {act, useEffect, useState} from 'react';
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
  const [activeSwitch, setActiveSwitch] = useState<string[]>([]);
  const [visible, setVisible] = React.useState(false);

  const onToggleSnackBar = () => setVisible(!visible);
  const onDismissSnackBar = () => setVisible(false);

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
        console.log('Tau');
        getContactsFromDevice();
      } else {
        console.log('fdp');
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const getContactsFromDevice = () => {
    Contacts.getAll()
      .then(contactList => {
        contactList.sort((a, b) => {
          a.displayName = a.givenName + ' ' + a.familyName;
          return a.displayName.localeCompare(b.displayName);
        });
        setContacts(contactList);
      })
      .catch(e => {
        console.log(e);
      });
  };

  useEffect(() => {
    requestReadContactsPermission();
  }, []);

  const handleToggleSwitch = (
    recordId: string,
    isOn: any,
    setIsOn: (arg0: boolean) => void,
  ) => {
    let newContacts = activeSwitch;
    if (isOn) {
      const indexOfRecord = newContacts.indexOf(recordId);
      newContacts.splice(indexOfRecord, 1);
      setIsOn(false);

      console.log(newContacts);
    } else if (activeSwitch.length < 3) {
      newContacts.push(recordId);
      setIsOn(true);

      console.log(newContacts);
    } else {
      onToggleSnackBar();
    }
    setActiveSwitch(newContacts);
  };

  const renderItem: ListRenderItem<Contacts.Contact> = ({item}) => {
    if (item.phoneNumbers.length === 0) {
      return null;
    }
    return (
      <ContactCardComponent
        recordId={item.recordID}
        name={item.displayName}
        number={item.phoneNumbers[0].number}
        avatar={item.thumbnailPath}
        onToggleSwitch={handleToggleSwitch}
      />
    );
  };

  const navigation = useNavigation();

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
