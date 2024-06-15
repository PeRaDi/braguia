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
  TouchableHighlight,
  View,
  Text,
} from 'react-native';
import {FlatList, Image} from 'react-native';
import Video from 'react-native-video';
import {mediaDAO} from './MediaDAO';
import {useSelector} from 'react-redux';
import {selectAuth} from '@store/store';
import {Card, IconButton, Button} from 'react-native-paper';
import {TouchableOpacity} from 'react-native-gesture-handler';

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
  videoPlayer: {
    width: '100%',
    height: '90%',
  },
  audioPlayer: {
    width: '100%',
    height: '100%',
  },
});

const MediaPreview = ({media}: {media: Media}) => {
  const {isPremium} = useSelector(selectAuth);
  const [modalVisible, setModalVisible] = useState(false);

  const renderIcon = () => {
    if (media.isImage)
      return (
        <Card.Cover source={{uri: media.fileUrl}} style={styles.cardImage} />
      );
    if (media.isVideo)
      return <IconButton icon="video" size={48} style={styles.icon} />;
    if (media.isRecord)
      return <IconButton icon="microphone" size={48} style={styles.icon} />;
    return null;
  };

  const handlePress = () => {
    setModalVisible(true);
  };

  const renderModalContent = () => {
    if (media.isImage) {
      return <Image source={{uri: media.fileUrl}} style={styles.cardImage} />;
    }
    if (media.isVideo) {
      return (
        <Video
          source={{uri: media.fileUrl}}
          style={styles.videoPlayer}
          controls
        />
      );
    }
    if (media.isRecord) {
      return (
        <Video
          source={{uri: media.fileUrl}}
          style={styles.audioPlayer}
          controls
        />
      );
    }
    return null;
  };

  return (
    <>
      <TouchableHighlight onPress={handlePress}>
        <View>{renderIcon()}</View>
      </TouchableHighlight>
      <Modal
        visible={modalVisible}
        transparent={false}
        animationType="slide"
        onRequestClose={() => {
          setModalVisible(false);
        }}
      >
        <View style={styles.modalContent}>
          {renderModalContent()}
          <Button
            mode="contained"
            onPress={() => setModalVisible(false)}
            style={styles.closeButton}
          >
            Close
          </Button>
        </View>
      </Modal>
    </>
  );
};

const EnhancedMediaPreview = withObservables(['media'], ({media}) => ({
  media,
}))(MediaPreview);

const GalleryComponent = ({
  medias,
  navigation,
}: {
  medias: Media[];
  navigation: any;
}) => {
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    mediaDAO.fetchList({fromCache: true});
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await mediaDAO.fetchList();
    setRefreshing(false);
  }, []);

  const renderItem: ListRenderItem<Media> = ({item}) => {
    const screenWidth = Dimensions.get('window').width;
    const itemSize = screenWidth / 3 - 10;

    return (
      <View style={{width: itemSize, height: itemSize, padding: 2}}>
        <Card key={item.id} style={styles.card}>
          <EnhancedMediaPreview media={item} />
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
    </View>
  );
};

const enhance = withObservables(['navigation'], () => ({
  medias: database.collections.get(Media.table).query(),
}));

const EnhancedGalleryComponent = enhance(GalleryComponent);

export default EnhancedGalleryComponent;
