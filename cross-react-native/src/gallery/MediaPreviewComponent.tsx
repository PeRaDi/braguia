import React from 'react';
import { Modal, StyleSheet, View, Dimensions } from 'react-native';
import { Card, IconButton } from 'react-native-paper';
import Video from 'react-native-video'; // Make sure to install react-native-video

const styles = StyleSheet.create({
  modalContainer: {
    flex: 1,
    backgroundColor: 'black',
    justifyContent: 'center',
    alignItems: 'center',
  },
  media: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
  },
  closeButton: {
    position: 'absolute',
    top: 40,
    right: 20,
    zIndex: 1,
  },
});

const MediaPreviewComponent = ({ visible, media, onClose }) => {
  if (!media) return null;

  const renderMedia = () => {
    if (media.isImage) {
      return <Card.Cover source={{ uri: media.fileUrl }} style={styles.media} />;
    } else if (media.isVideo) {
      return <Video source={{ uri: media.fileUrl }} style={styles.media} controls />;
    } else if (media.isRecord) {
      return <IconButton icon="microphone" size={48} style={styles.media} />;
    }
    return null;
  };

  return (
    <Modal visible={visible} transparent={true} onRequestClose={onClose}>
      <View style={styles.modalContainer}>
        <IconButton icon="close" size={30} onPress={onClose} style={styles.closeButton} />
        {renderMedia()}
      </View>
    </Modal>
  );
};

export default MediaPreviewComponent;
