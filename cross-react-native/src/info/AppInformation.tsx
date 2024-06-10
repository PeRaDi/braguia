import React, {useEffect, useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  Linking,
  Button,
  TouchableOpacity,
} from 'react-native';

import {apiURL} from 'app.json';
import {ActivityIndicator} from 'react-native-paper';
import {AppInfo} from '@src/info/model.ts';

const openURL = (url: string, type: string = '') => {
  Linking.openURL(`${type ? type + ':' : ''}${url}`).catch(err =>
    console.error("Couldn't load page", err),
  );
};

const ClickableLinkText = ({
  url,
  label,
  text,
  style,
}: {
  url: string;
  label: string | undefined;
  text: string;
  style: any;
}) => {
  return (
    <TouchableOpacity onPress={() => openURL(url)}>
      <View style={{flex: 1, flexDirection: 'row'}}>
        {label ? <Text>{label}: </Text> : <></>}
        <Text style={style}>{text}</Text>
      </View>
    </TouchableOpacity>
  );
};

const PhoneLink = ({phoneNumber}: {phoneNumber: string}) => {
  return (
    <TouchableOpacity onPress={() => openURL(phoneNumber, 'tel')}>
      <View style={{flex: 1, flexDirection: 'row'}}>
        <Text>Telefone: </Text>
        <Text style={{color: 'blue'}}>{phoneNumber}</Text>
      </View>
    </TouchableOpacity>
  );
};

const InfoCard = ({appInfo}: {appInfo: AppInfo}) => {
  return (
    <>
      <Text style={styles.appName}> {appInfo.app_name} </Text>
      <Text style={styles.appDesc}> {appInfo.app_desc} </Text>
      <Text style={styles.landingPageText}>
        {' '}
        {appInfo.app_landing_page_text}{' '}
      </Text>

      <Text style={styles.sectionTitle}> Socials </Text>
      {appInfo.socials.map((social, index) => (
        <View key={index} style={styles.itemContainer}>
          <ClickableLinkText
            label=""
            text={social.social_name}
            url={social.social_url}
            style={{...styles.itemTitle, ...styles.link}}
          />
        </View>
      ))}

      <Text style={styles.sectionTitle}> Contacts </Text>
      {appInfo.contacts.map((contact, index) => (
        <View key={index} style={styles.itemContainer}>
          <Text style={styles.itemTitle}> {contact.contact_name} </Text>
          <Text>{contact.contact_desc} </Text>
          <PhoneLink phoneNumber={contact.contact_phone} />
          <ClickableLinkText
            label="Email"
            text={contact.contact_mail}
            url={contact.contact_url}
            style={styles.link}
          />
        </View>
      ))}

      <Text style={styles.sectionTitle}> Partners </Text>
      {appInfo.partners.map((partner, index) => (
        <View key={index} style={styles.itemContainer}>
          <ClickableLinkText
            url={partner.partner_url}
            label={''}
            text={partner.partner_name}
            style={{...styles.itemTitle, ...styles.link}}
          />
          <Text> {partner.partner_desc} </Text>
          <PhoneLink phoneNumber={partner.partner_phone} />
          <ClickableLinkText
            label="Email"
            text={partner.partner_mail}
            url={partner.partner_url}
            style={styles.link}
          />
        </View>
      ))}
    </>
  );
};

const AppInformation = () => {
  const [appsInfo, setAppsInfo] = useState<AppInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<any>(null);

  useEffect(() => {
    fetch(`${apiURL}/app`)
      .then(response => {
        if (response.ok) {
          return response.json() as Promise<AppInfo[]>;
        }
        throw new Error('Network response was not ok');
      })
      .then(info => {
        setAppsInfo(info);
        setError(null);
        setLoading(false);
      })
      .catch(error => {
        setError(error);
        setAppsInfo([]);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#0000ff" />
      </View>
    );
  } else if (error) {
    return (
      <View style={styles.centered}>
        <Text>Error: {error.message}</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      {appsInfo.map((info, index) => (
        <InfoCard key={index} appInfo={info} />
      ))}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f5f5f5',
  },
  appName: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  appDesc: {
    fontSize: 16,
    marginBottom: 8,
  },
  landingPageText: {
    fontSize: 16,
    marginBottom: 16,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginTop: 16,
    marginBottom: 8,
  },
  itemContainer: {
    marginBottom: 16,
    padding: 16,
    backgroundColor: '#fff',
    borderRadius: 2,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 8,
    shadowOffset: {width: 0, height: 2},
    elevation: 2,
  },
  itemTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  link: {
    color: 'blue',
  },
});

export default AppInformation;
