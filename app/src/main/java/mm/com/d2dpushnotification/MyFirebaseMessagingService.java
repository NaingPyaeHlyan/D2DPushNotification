package mm.com.d2dpushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String channel = "admin_channel";
    private int notiId = new Random().nextInt(3000);


    @Override
    public void onNewToken(@NonNull String s) {

        String token = Objects.requireNonNull(FirebaseInstanceId.getInstance().getInstanceId().getResult()).getToken();
        Log.w("Token", token);

//
//        FirebaseMessaging.getInstance().subscribeToTopic("userId")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = task.getResult().toString();
//                        Log.i("Subscribe", msg);
//                    }
//                });

    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

//
//        if (remoteMessage.getData().size() > 0){
//            Toast.makeText(this, "onMessageReceived", Toast.LENGTH_SHORT).show();
//            return;
//        }else {
//            Toast.makeText(this, "Not MessageReceived", Toast.LENGTH_SHORT).show();
//        }
//
//        if (remoteMessage.getNotification() != null){
//            Toast.makeText(this, "Message Notification Body: " + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
//            return;
//        }


        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
     /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them.
      */

     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
         setupChannel(manager);
        }

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        Intent intent = new Intent(this, MessageShowActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("TITLE", title);        intent.putExtra("MESSAGE", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.origamibird)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        // Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notiBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        manager.notify(notiId, notiBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannel(NotificationManager notificationManager){

        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to Device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(channel, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null){
            notificationManager.createNotificationChannel(adminChannel);
        }

    }

}
