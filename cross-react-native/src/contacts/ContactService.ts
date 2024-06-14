import Contacts from 'react-native-contacts';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {Linking} from 'react-native';

export class ContactService {
  async getContactsFromDevice(): Promise<Contacts.Contact[]> {
    return new Promise((resolve, _reject) => {
      Contacts.getAll()
        .then(contactList => {
          contactList.sort((a, b) => {
            a.displayName = a.givenName + ' ' + a.familyName;
            return a.displayName.localeCompare(b.displayName);
          });
          resolve(contactList);
        })
        .catch(e => {
          console.log(e);
        });
    });
  }
  async getSelectedContacts(): Promise<string[]> {
    const selectedContacts = await AsyncStorage.getItem('selectedContacts');
    return selectedContacts ? JSON.parse(selectedContacts) : [];
  }

  async saveSelectedContacts(contacts: String[]): Promise<void> {
    await AsyncStorage.setItem('selectedContacts', JSON.stringify(contacts));
  }

  async clearSelectedContacts(): Promise<void> {
    await AsyncStorage.removeItem('selectedContacts');
  }

  async getContactById(id: string): Promise<Contacts.Contact | null> {
    return new Promise((resolve, _reject) => {
      Contacts.getContactById(id)
        .then(contact => {
          if (contact) resolve(contact);
          resolve(null);
        })
        .catch(e => {
          console.log(e);
        });
    });
  }

  callNumber = (number: string) => {
    Linking.openURL(`tel:${number}`);
  };
}

export default new ContactService();
