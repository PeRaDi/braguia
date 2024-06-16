import {Alert, PermissionsAndroid} from "react-native";
import RNFS from "react-native-fs";
import {name as AppName} from "app.json";

export interface FileParams {
    url: string;
    id: string;
    type: "trail" | "media";
}

async function ensureDirectoryExists(directoryPath: string) {
    try {
        const exists = await RNFS.exists(directoryPath);
        if (!exists) {
            await RNFS.mkdir(directoryPath);
        }
    } catch (error) {
        console.error('Error ensuring directory exists:', error);
    }
}

export const saveFileToDevice = async (params: FileParams[], onComplete: (savedPaths: string[], notSavedPaths: string[]) => void) => {
    try {
        const hasReadPermission = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE);
        const hasWritePermission = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE);

        if (!hasReadPermission || !hasWritePermission) {
            console.log('Requesting storage permission...');
            const status = await PermissionsAndroid.requestMultiple(
                [PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE, PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE]
            );
            if (status[PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE] != "granted" ||
                status[PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE] != "granted"
            ) {
                return;
            }
            console.log('Storage permission granted');
        }

        const downloadDirectory = `${RNFS.ExternalStorageDirectoryPath}/DCIM/${AppName}`;
        const savedPaths: string[] = [];
        const notSavedPaths: string[] = [];

        for (const {id, url, type} of params) {
            try {
                const extension = url.substring(url.lastIndexOf('.'));
                const fileName = `${type}_${id}${extension}`;
                const path = `${downloadDirectory}/${fileName}`;
                await ensureDirectoryExists(downloadDirectory);

                const result = await RNFS.downloadFile({
                    fromUrl: url,
                    toFile: path,
                }).promise;

                if (result.statusCode === 200) {
                    console.log(`Downloaded ${url} to ${path}`);
                    savedPaths.push(path);
                } else {
                    console.error(`Failed to download ${url}`);
                    notSavedPaths.push(path);
                }
            } catch (error) {
                console.error(error);
            }
        }

        onComplete(savedPaths, notSavedPaths);
    } catch (err) {
        console.error(err);
    }
};