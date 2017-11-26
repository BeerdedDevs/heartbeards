const functions = require('firebase-functions');


// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//

/**
 * Triggers when a user uploads a new beard picture
 */
exports.sendFollowerNotification = functions.database.ref('/timeline').onWrite(event => {
  const followerUid = event.params.followerUid;
  const followedUid = event.params.followedUid;

  console.log('A new picture was uploaded');

  // Get the list of device notification tokens.
  const getDeviceTokensPromise = admin.database().ref(`/notificationTokens`).once('value');

  // Get the follower profile.
  const beardUploaderPromise = admin.auth().getUser(followerUid);

  return Promise.all([getDeviceTokensPromise, beardUploaderPromise]).then(results => {
    const tokensSnapshot = results[0];
    const beardUploader = results[1];

    // Check if there are any device tokens.
    if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log('Fetched beard uploader profile', beardUploader);

    // Notification details.
    const payload = {
      notification: {
        title: 'A new beard was just uploaded!',
        body: `${beardUploader.displayName} uploaded a new sexy beard pic. Check it out!`,
        icon: follower.photoURL
      }
    };

    // Listing all tokens.
    const tokens = Object.keys(tokensSnapshot.val());

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});
