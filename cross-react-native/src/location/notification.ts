import notifee, {EventType} from '@notifee/react-native';
import {NavigationContainerRef} from "@react-navigation/native";

let navigationRef: NavigationContainerRef<any> | null = null;

export const setNavigationRef = (ref: NavigationContainerRef<any> | null) => {
    navigationRef = ref;
};

// You can now use navigationRef to navigate programmatically
export const navigateToScreen = (screenName: string, params?: any) => {
    try {
        console.log({ screenName, params });
        navigationRef?.navigate(screenName, params);
    }catch(error) {
        console.error(error);
    }
};

export const InitializeLocationTrackingNotificationService = () => {

    //this handler will listen to background events:
    notifee.onBackgroundEvent(async ({type, detail}) => {
        const {notification, pressAction} = detail;
        // console.log('type ', type);
        // console.log('notification data ', detail);

        //Check if the user has pressed the notification
        if ([EventType.ACTION_PRESS].includes(type) && notification && pressAction?.id === 'ver' && notification.data) {
            const {screen, params} = notification.data;
            // await notifee.cancelNotification(notification.id!);
            // await notifee.stopForegroundService();
            if (screen && params) {
                navigateToScreen(screen.toString(), {trailId: params});  // TODO: Add pinId param
            }
        }
    });

    notifee.registerForegroundService(notification => {
        return new Promise((resolve, reject) => {
            // console.log("Foreground: ", notification);
            // return resolve();
            // notifee.onForegroundEvent(async ({type, detail}) => {
            //     console.log("Foreground: ", notification);
            //   if (type === EventType.ACTION_PRESS && detail.pressAction.id === 'stop') {
            //     await notifee.stopForegroundService();
            //   }
            // });
        });
    });

}