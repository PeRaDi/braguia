import React from 'react';
import {SectionList, StyleSheet, View} from 'react-native';
import {Icon, MD3Colors, Text} from 'react-native-paper';
import {SectionListData} from 'react-native/Libraries/Lists/SectionList';

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
});

interface SectionItem {
  icon: string;
  title: string;
  subTitle: string;
}

const SettingsComponent = () => {
  const sections: SectionListData<SectionItem>[] = [
    {
      title: 'Emergência',
      data: [
        {
          icon: 'contacts',
          title: 'Contatos de emergência',
          subTitle: 'Selecione 3 contatos de emergência',
        },
        {
          icon: 'phone-forward',
          title: 'Chamada de emergência',
          subTitle: 'Faça uma chamada de emergência',
        },
      ],
    },
    {
      title: 'Conta',
      data: [
        {
          icon: 'card-account-details',
          title: 'Perfil',
          subTitle: 'Detalhes do perfil',
        },
        {
          icon: 'logout',
          title: 'Logout',
          subTitle: 'Terminar sessão',
        },
      ],
    },
    {
      title: 'Outros',
      data: [
        {
          icon: 'crosshairs-gps',
          title: 'Localização',
          subTitle: 'Serviço de localização GPS',
        },
        {
          icon: 'information-variant',
          title: 'Sobre',
          subTitle: 'Informações sobre a aplicação',
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

  const renderItem = ({item}) => {
    return (
      <View style={styles.itemContainer}>
        <Icon source={item.icon} size={20} color={MD3Colors.primary60} />
        <View style={styles.itemSubContainer}>
          <Text style={styles.itemTitle}>{item.title}</Text>
          <Text style={styles.itemSubtitle}>{item.subTitle}</Text>
        </View>
      </View>
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
