import {StyleSheet, View} from "react-native";
import { Text } from "react-native-paper";

export type EmptyParams = {message: string | undefined};

export const EmptyListComponent = ({message}: EmptyParams) => (
    <View style={styles.emptyContainer}>
        <Text style={styles.emptyText}>{message ?? 'Nenhum item disponível'}</Text>
    </View>
);

const styles = StyleSheet.create({
    listContainer: {
        flexGrow: 1,
    },
    emptyContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    emptyText: {
        fontSize: 18,
        color: '#888',
    },
    card: {
        margin: 10,
    },
});