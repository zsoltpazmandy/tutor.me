package zsoltpazmandy.tutorme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by zsolt on 29/07/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String message;
    String title;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        message = remoteMessage.getNotification().getBody();
        title = remoteMessage.getNotification().getTitle();
        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        Intent notifIntent = new Intent(this,MainActivity.class);
        notifIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri beep = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMessagingService.this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.school24)
                .setSound(beep)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());


    }
}
