import {database} from '@model/database';
import {Media} from '@model/models';
import {withObservables} from '@nozbe/watermelondb/react';
import React, {useCallback, useEffect, useState} from 'react';
import {
  Dimensions,
  ListRenderItem,
  Modal,
  RefreshControl,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import {FlatList, Image} from 'react-native';
import Video from 'react-native-video';
import {mediaDAO} from './MediaDAO';
import {useSelector} from 'react-redux';
import {selectAuth} from '@store/store';
import {
  Card,
  IconButton,
  Button,
  TouchableRipple,
  Snackbar,
  Divider,
} from 'react-native-paper';
import {PermissionsAndroid} from 'react-native';
import RNFS from 'react-native-fs';
import {apiURL } from "app.json";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    height: '100%',
    display: 'flex',
    alignItems: 'center',
    marginTop: 15,
  },
  card: {
    borderRadius: 5,
    overflow: 'hidden',
    position: 'relative',
    width: '100%',
    height: '100%',
  },
  cardImage: {
    borderRadius: 0,
    width: '100%',
    height: '100%',
  },
  icon: {
    borderRadius: 0,
    width: '100%',
    height: '100%',
    margin: 0,
  },
  modalContent: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'black',
  },
  closeButton: {
    marginTop: 20,
  },
  player: {
    width: '100%',
    height: '90%',
    resizeMode: 'contain',
  },
  selectionBar: {
    position: 'absolute',
    bottom: 0,
    width: '100%',
    padding: 10,
    backgroundColor: '#f4edf7',
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    display: 'flex',
    alignItems: 'center',
  },
});

function mediaFileUrl(media: Media) {
  return media.fileUrl.startsWith('http') ? media.fileUrl : `${apiURL}${media.fileUrl}`;
}

const MediaPreview = ({
  media,
  onLongPress,
  isSelected,
  toggleSelection,
  selectionMode,
}: {
  media: Media;
  onLongPress: () => void;
  isSelected: boolean;
  toggleSelection: () => void;
  selectionMode: boolean;
}) => {
  const {isPremium} = useSelector(selectAuth);
  const [modalVisible, setModalVisible] = useState(false);
  const [fileUrl, setFileUrl] = useState(mediaFileUrl(media));

  const renderIcon = () => {
    if (media.isImage)
      return (
        <Card.Cover source={{uri: fileUrl}} style={styles.cardImage} />
      );
    if (media.isVideo)
      return <IconButton icon="video" size={48} style={styles.icon} />;
    if (media.isRecord)
      return <IconButton icon="microphone" size={48} style={styles.icon} />;
    return null;
  };

  const handlePress = () => {
    if (!selectionMode) {
      setModalVisible(true);
    } else {
      toggleSelection();
    }
  };

  const renderModalContent = () => {
    if (media.isImage) {
      return <Image source={{uri: fileUrl}} style={styles.player} />;
    }
    if (media.isVideo) {
      return (
        <Video source={{uri: fileUrl}} style={styles.player} controls />
      );
    }
    if (media.isRecord) {
      return (
        <Video source={{uri: fileUrl}} style={styles.player} controls />
      );
    }
    return null;
  };

  return (
    <View>
      <TouchableRipple onPress={handlePress} onLongPress={onLongPress}>
        <View style={{opacity: isSelected ? 0.5 : 1}}>{renderIcon()}</View>
      </TouchableRipple>
      <Modal
        visible={modalVisible}
        transparent={false}
        animationType="slide"
        onRequestClose={() => {
          setModalVisible(false);
        }}>
        <View style={styles.modalContent}>
          {renderModalContent()}
          <Button
            mode="contained"
            onPress={() => setModalVisible(false)}
            style={styles.closeButton}>
            Close
          </Button>
        </View>
      </Modal>
    </View>
  );
};

const EnhancedMediaPreview = withObservables(['media'], ({media}) => ({
  media,
}))(MediaPreview);

export const GalleryComponent = ({
  medias,
  navigation,
}: {
  medias: Media[];
  navigation: any;
}) => {
  const [refreshing, setRefreshing] = useState(false);
  const [selectionMode, setSelectionMode] = useState(false);
  const [selectedItems, setSelectedItems] = useState<Set<string>>(new Set());

  useEffect(() => {
    mediaDAO.fetchList({fromCache: true});
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await mediaDAO.fetchList();
    setRefreshing(false);
  }, []);

  const handleLongPress = useCallback((item: Media) => {
    setSelectionMode(true);
    setSelectedItems(prev => new Set(prev).add(item.id));
  }, []);

  const toggleSelection = useCallback((item: Media) => {
    setSelectedItems(prev => {
      const newSet = new Set(prev);
      if (newSet.has(item.id)) {
        newSet.delete(item.id);
      } else {
        newSet.add(item.id);
      }
      if (newSet.size === 0) {
        setSelectionMode(false);
      }
      return newSet;
    });
  }, []);

  const handleSaveToDevice = async () => {
    try {
      console.log('Requesting storage permission...');

      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
        {
          title: 'Storage permission',
          message:
            'A melhor aplicação de roteiros do mundo aceder ao teu armazenamento para guardar medias.',
          buttonNeutral: 'Depois vejo isso',
          buttonNegative: 'Cancelar',
          buttonPositive: 'Toma lá',
        },
      );

      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('Storage permission granted');

        selectedItems.forEach(async (itemUrl, index) => {
          const fileName = `photo_${index}.jpg`;
          const path = `${RNFS.ExternalStorageDirectoryPath}/DCIM/${fileName}`;

          try {
            const result = await RNFS.downloadFile({
              fromUrl: itemUrl,
              toFile: path,
            }).promise;

            if (result.statusCode === 200) {
              console.log(`Downloaded ${fileName} to ${path}`);
            } else {
              console.error(`Failed to download ${fileName}`);
            }
          } catch (downloadError) {
            console.error(`Error downloading ${fileName}:`, downloadError);
          }
        });
      } else {
        console.log('Storage permission denied');
      }
    } catch (err) {
      console.error('Error requesting storage permission:', err);
    }
  };

  const renderItem: ListRenderItem<Media> = ({item}) => {
    const screenWidth = Dimensions.get('window').width;
    const itemSize = screenWidth / 3 - 10;
    const isSelected = selectedItems.has(item.id);

    return (
      <View style={{width: itemSize, height: itemSize, padding: 2}}>
        <Card key={item.id} style={styles.card}>
          <EnhancedMediaPreview
            media={item}
            onLongPress={() => handleLongPress(item)}
            isSelected={isSelected}
            toggleSelection={() => toggleSelection(item)}
            selectionMode={selectionMode}
          />
        </Card>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <FlatList
        keyExtractor={item => item.id}
        data={medias}
        renderItem={renderItem}
        numColumns={3}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        contentContainerStyle={{
          paddingBottom: 10,
        }}
      />
      {selectionMode && (
        <>
          <Divider />
          <View style={styles.selectionBar}>
            <Text>Itens selecionados: {selectedItems.size}</Text>
            <Button mode="contained" onPress={handleSaveToDevice}>
              Guardar no dispositivo
            </Button>
          </View>
          <Divider />
        </>
      )}
    </View>
  );
};

const enhance = withObservables(['navigation'], () => ({
  medias: database.collections.get(Media.table).query(),
}));

const EnhancedGalleryComponent = enhance(GalleryComponent);

export default EnhancedGalleryComponent;
