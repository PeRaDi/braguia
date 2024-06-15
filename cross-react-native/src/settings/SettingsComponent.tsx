import React from 'react';
import {SectionList, StyleSheet, TouchableOpacity, View} from 'react-native';
import {
  Button,
  Icon,
  MD3Colors,
  SegmentedButtons,
  Switch,
  Text,
} from 'react-native-paper';
import {SectionListData} from 'react-native/Libraries/Lists/SectionList';
import {useSelector} from 'react-redux';
import {selectAuth} from '@store/store.ts';
import authService from '@src/auth/auth-service.ts';
import {useLocationContext} from '@src/location/LocationContext.tsx';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 22,
  },
  sectionHeader: {
    paddingVertical: 15,
    paddingHorizontal: 10,
    fontSize: 20,
    fontWeight: 'bold',
    backgroundColor: 'rgba(247,247,247,1.0)',
  },
  itemContainer: {
    flex: 1,
    paddingVertical: 10,
    paddingHorizontal: 20,
    flexDirection: 'row',
    alignItems: 'center',
  },
  itemSubContainer: {
    flex: 1,
    flexDirection: 'column',
    paddingHorizontal: 20,
  },
  itemTitle: {
    fontSize: 18,
  },
  itemSubtitle: {
    fontSize: 14,
  },
  buttonContainer: {
    alignItems: 'center',
  },
  buttons: {
    flexDirection: 'row',
    // justifyContent: 'space-around',
    alignItems: 'center',
    marginVertical: 12,
    width: '100%',
  },
});

interface SectionItem {
  icon: string;
  key: string;
  title: string;
  subTitle: string;
  onPress?: () => void;
}

const SettingsComponent = ({navigation}) => {
  const {isAuthenticated} = useSelector(selectAuth);
  const {observing, getLocationUpdates, stopLocationUpdates} =
    useLocationContext();
  const [localtionOnOff, setLocaltionOnOff] = React.useState(
    observing ? 'on' : 'off',
  );

  const sections: SectionListData<SectionItem>[] = [
    {
      title: 'Emergência',
      data: [
        {
          key: 'contacts',
          icon: 'contacts',
          title: 'Contatos de emergência',
          subTitle: 'Selecione 3 contatos de emergência',
          onPress: () => navigation.navigate('SelectContact'),
        },
        {
          key: 'chamadas',
          icon: 'phone-forward',
          title: 'Chamada de emergência',
          subTitle: 'Faça uma chamada de emergência',
        },
      ],
    },
    {
      title: 'Conta',
      data: [
        ...(isAuthenticated
          ? [
              {
                key: 'perfil',
                icon: 'card-account-details',
                title: 'Perfil',
                subTitle: 'Detalhes do perfil',
                onPress: () => navigation.navigate('UserProfile'),
              },
              {
                key: 'logout',
                icon: 'logout',
                title: 'Logout',
                subTitle: 'Terminar sessão',
                onPress: () => authService.logout().then(() => null),
              },
            ]
          : [
              {
                key: 'login',
                icon: 'login',
                title: 'Login',
                subTitle: 'Iniciar sessão',
                onPress: () => navigation.navigate('Login'),
              },
            ]),
      ],
    },
    {
      title: 'Outros',
      data: [
        {
          key: 'localizacao',
          icon: 'crosshairs-gps',
          title: 'Localização',
          subTitle: 'Serviço de localização GPS',
          onPress: () => navigation.navigate('LocationSettings'),
        },
        {
          key: 'sobre',
          icon: 'information-variant',
          title: 'Sobre',
          subTitle: 'Informações sobre a aplicação',
          onPress: () => navigation.navigate('About'),
        },
      ],
    },
  ];

  const renderSectionHeader = ({section}) => (
    <View>
      <Text key={section.title} style={styles.sectionHeader}>
        {section.title}
      </Text>
    </View>
  );

  const renderItem = ({item}: {item: SectionItem}) => {
    if (item.key === 'localizacao') {
      return (
        <View style={styles.itemContainer}>
          <Icon size={20} source={item.icon} color={MD3Colors.primary60} />
          <View style={styles.itemSubContainer}>
            <Text style={styles.itemTitle}>{item.title}</Text>
            <Text style={styles.itemSubtitle}>{item.subTitle}</Text>
          </View>
          <Switch
            value={observing}
            onValueChange={value =>
              value ? getLocationUpdates() : stopLocationUpdates()
            }
          />
        </View>
      );
    }
    return (
      <TouchableOpacity
        style={styles.itemContainer}
        onPress={() => (item.onPress ? item.onPress() : null)}>
        <Icon source={item.icon} size={20} color={MD3Colors.primary60} />
        <View style={styles.itemSubContainer}>
          <Text style={styles.itemTitle}>{item.title}</Text>
          <Text style={styles.itemSubtitle}>{item.subTitle}</Text>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      <SectionList
        sections={sections}
        renderItem={renderItem}
        renderSectionHeader={renderSectionHeader}
        keyExtractor={item => item.title}
      />
    </View>
  );
};

export default SettingsComponent;
