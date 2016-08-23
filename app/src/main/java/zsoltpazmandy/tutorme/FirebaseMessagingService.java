package zsoltpazmandy.tutorme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The class is responsible for displaying remote messages as push notifications using
 * Google's Firebase platform. showNotification is called whenever a message is received.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getData().get("body");
        showNotification(from, messageBody);
    }

    private void showNotification(String from, String messageBody) {

        Intent notifIntent = new Intent(this, Chat.class);
        notifIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri beep = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMessagingService.this)
                .setAutoCancel(true)
                .setContentTitle("tutor.me message")
                .setContentText(from + ": " + messageBody)
                .setSmallIcon(R.drawable.school24)
                .setSound(beep)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


}
