const {onRequest} = require("firebase-functions/v2/https");
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendWallpaperRequestNotification = functions.firestore
    .document('wallpaperRequest/{requestId}')
    .onCreate(async (snapshot, context) => {
        try {
            const request = snapshot.data();
            console.log(request);
            // Get the recipient user's FCM token
            const recipientUserId = request.receiverUser.userId;
            const recipientUserDoc = await admin.firestore().collection('users').doc(recipientUserId).get();
            const recipientUser = recipientUserDoc.data();
            const recipientFCMToken = recipientUser.fcmToken;

            // Notification payload
            const payload = {
                notification: {
                    title: 'New Wallpaper Request',
                    body: `${request.senderUser.username} wants to change your wallpaper`,
                    //clickAction: 'FLUTTER_NOTIFICATION_CLICK'
                },
                data: {
                    requestId: context.params.requestId
                }
            };

            // Send the notification
            await admin.messaging().sendToDevice(recipientFCMToken, payload);

            console.log('Notification sent successfully');
        } catch (error) {
            console.error('Error sending notification:', error);
        }
    });
exports.sendCoupleRequestNotification = functions.firestore
    .document('requests/{wallDealRequestId}')
    .onCreate(async (snapshot, context) => {
        try {
            const request = snapshot.data();
            console.log(request);
            // Get the recipient user's FCM token
            const recipientUserId = request.receiverUser.userId;
            const recipientUserDoc = await admin.firestore().collection('users').doc(recipientUserId).get();
            const recipientUser = recipientUserDoc.data();
            const recipientFCMToken = recipientUser.fcmToken;

            // Notification payload
            const payload = {
                notification: {
                    title: 'New Couple Request',
                    body: `${request.senderUser.username} wants to be a couple with you`,
                    //clickAction: 'FLUTTER_NOTIFICATION_CLICK'
                },
                data: {
                    requestId: context.params.wallDealRequestId
                }
            };

            // Send the notification
            await admin.messaging().sendToDevice(recipientFCMToken, payload);

            console.log('Notification sent successfully');
        } catch (error) {
            console.error('Error sending notification:', error);
        }
    });