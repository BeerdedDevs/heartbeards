const functions = require('firebase-functions');


// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

/**
 * Triggers when a user uploads a new beard picture
 */
exports.sendNewBeardUploadedNotification = functions.database.ref('/timeline/{id}').onCreate(event => {
  const timelineId = event.params.id;
  const data = event.data.val();
  console.log(data);

  console.log('A new picture was uploaded');

  // Get the list of device notification tokens.
  const getDeviceTokensPromise = admin.database().ref(`/notificationTokens`).once('value');

  // Get the follower profile.
  const beardUploader = data.name

  return getDeviceTokensPromise.then(tokenSnapshot => {
    console.log(tokenSnapshot)
    // Check if there are any device tokens.
    if (!tokenSnapshot.hasChildren()) {
      console.log('There are no notification tokens to send to.');
      return Promise.reject();
    }
    console.log('There are', tokenSnapshot.numChildren(), 'tokens to send notifications to.');
    console.log('Fetched beard uploader profile', beardUploader);

    // Notification details.
    const payload = {
      notification: {
        title: 'A new beard was just uploaded!',
        body: `${beardUploader} uploaded a new sexy beard pic. Check it out!`
      }
    };

    // Listing all tokens.
    const tokens = Object.keys(tokenSnapshot.val());

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.warn('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokenSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});
