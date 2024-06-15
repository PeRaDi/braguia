import React from 'react';
import { View, StyleSheet } from 'react-native';
import {Card, Text, IconButton, Icon} from 'react-native-paper';

const GPSLocationUI = ({ latitude, longitude, altitude, showActions }: { latitude: number, longitude: number, altitude: number, showActions: boolean | undefined}) => {
    return (
        <View style={{...styles.row, justifyContent: 'space-between', width: '100%'}}>
            <View style={styles.row}>
                <Icon source='google-maps' size={24} color='black' />
                {/*<Text style={styles.label}>Localização:</Text>*/}
                <Text style={styles.value}>{latitude},{longitude},{altitude}</Text>
            </View>
            {showActions && <View style={styles.actions}>
                <IconButton
                    icon="content-copy"
                    size={20}
                    onPress={() => {
                        // navigator.clipboard.writeText(`Latitude: ${latitude}, Longitude: ${longitude}`);
                        // alert('Coordinates copied to clipboard!');
                    }}
                />
                <IconButton
                    icon="share-variant"
                    size={20}
                    onPress={() => console.log('Share location')}
                />
            </View>}
        </View>
    );
};

const styles = StyleSheet.create({
    card: {
        margin: 0,
        width: '100%',
        borderRadius: 0,
    },
    row: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: 0,
    },
    col: {
        flexDirection: 'column',
        alignItems: 'center',
        marginVertical: 0,
    },
    text: {
        marginLeft: 10,
        fontSize: 18,
        fontWeight: 'bold',
    },
    label: {
        fontWeight: 'bold',
    },
    value: {
        marginLeft: 5,
    },
    actions: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        marginTop: 0,
    },
});

export default GPSLocationUI;
