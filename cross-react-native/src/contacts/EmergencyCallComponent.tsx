import {useNavigation} from '@react-navigation/native';
import React, {useEffect, useState} from 'react';
import {StyleSheet, TouchableOpacity, View} from 'react-native';
import {GestureHandlerRootView} from 'react-native-gesture-handler';
import {Button, Icon, Surface, Text} from 'react-native-paper';
import ContactService from './ContactService';

const styles = StyleSheet.create({
  surfaceText: {
    width: 300,
  },
  container: {
    flex: 1,
    justifyContent: 'space-evenly',
    alignItems: 'center',
    flexDirection: 'row',
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    width: 100,
    height: 100,
    backgroundColor: '#B60E0E',
    borderRadius: 50,
  },
  surface: {
    alignItems: 'center',
    justifyContent: 'space-around',
    display: 'flex',
    flexDirection: 'row',
    height: 50,
  },
  destNameCaption: {
    textAlign: 'center',
    fontSize: 13,
    marginTop: 5,
    width: 100,
  },
});

const EmergencyCallComponent = () => {
  const navigation = useNavigation();
  const [contactsInfo, setContactsInfo] = useState<any[]>([]);

  const renderContacts = () => {
    return contactsInfo.map((contact, index) => (
      <View key={contact.id !== -1 ? contact.id : `contact-${index}`}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => ContactService.callNumber(contact.phone)}>
          <Icon source="account" size={50} color="#FFFFFF" />
        </TouchableOpacity>
        <Text style={styles.destNameCaption}>{contact.name}</Text>
      </View>
    ));
  };

  useEffect(() => {
    const fetchSelectedContacts = async () => {
      try {
        const selectedContacts = await ContactService.getSelectedContacts();
        if (selectedContacts.length === 0) {
          setContactsInfo([
            {id: -1, name: 'Não selecionado', phone: -1},
            {id: -1, name: 'Não selecionado', phone: -1},
            {id: -1, name: 'Não selecionado', phone: -1},
          ]);
          return;
        }

        const localContacts = await Promise.all(
          selectedContacts.map(async contactId => {
            const contact = await ContactService.getContactById(contactId);
            if (contact === null) {
              return {
                id: `not-selected-${contactId}`,
                name: 'Não selecionado',
                phone: -1,
              };
            } else {
              return {
                id: contact.recordID,
                name: `${contact.givenName} ${contact.familyName}`,
                phone: contact.phoneNumbers[0].number,
              };
            }
          }),
        );

        setContactsInfo(localContacts);
      } catch (error) {
        console.error('Error fetching contacts:', error);
      }
    };

    fetchSelectedContacts();
  }, []);

  return (
    <GestureHandlerRootView>
      <Surface style={styles.surface} elevation={4}>
        <Text style={styles.surfaceText} numberOfLines={2} ellipsizeMode="tail">
          Selecione o contacto para realizar a chamada de emergência.
        </Text>
        <Button onPress={() => navigation.goBack()}>Voltar</Button>
      </Surface>
      <View style={styles.container}>{renderContacts().slice(0, 2)}</View>
      <View style={styles.container}>
        {renderContacts().slice(2)}
        <View>
          <TouchableOpacity
            style={styles.button}
            onPress={() => ContactService.callNumber('112')}>
            <Icon source="car-emergency" size={50} color="#FFFFFF" />
          </TouchableOpacity>
          <Text style={styles.destNameCaption}>112</Text>
        </View>
      </View>
    </GestureHandlerRootView>
  );
};

export default EmergencyCallComponent;
