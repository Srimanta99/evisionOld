package com.evision.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.evision.R
import com.evision.SplashActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.cart_layout.view.*





class MyFirebaseMessagingService:FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val message = data.get("message")
        val title=data.get("title")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "Sculptee_channel"

        val intent = Intent(this, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications", NotificationManager.IMPORTANCE_HIGH)
            //  notificationChannel.description = "Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor =  R.color.blue_btn
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
     //   val  iconbitmap: Bitmap = BitmapFactory.decodeResource(applicationContext.getResources(), R.mipmap.ic_launcher);

        notificationBuilder.setAutoCancel(true)
                //.setColor(ContextCompat.getColor(this, R.color.noticolor))
                .setContentTitle(title)
                // .setStyle( NotificationCompat.BigTextStyle().bigText(decodebody))
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .setContentIntent(pIntent)
                //.setLargeIcon(iconbitmap)
                //.setSmallIcon(R.drawable.app_icon_noti)
                //  .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true)
                // stopForeground(false)
                .setOngoing(false)

        notificationBuilder.setStyle( NotificationCompat.BigTextStyle().bigText(message));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.appiconnoti);
            notificationBuilder.setColor(getResources().getColor(R.color.blue_btn));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        //  val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.birthday1)
        // notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeIcon))
        val r = java.util.Random()
        val random= r.nextInt(100 - 10 + 1) + 10
        notificationManager.notify(random, notificationBuilder.build())
      //  notificationManager.cancel(random)
       // notificationManager.notify(random, notificationBuilder.build())

       /* remoteMessage?.notification?.let {
            Log.d("noti", "Message Notification Body: ${it.body}")
            //Message Services handle notification
            val notification = NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.from)
                    .setContentText(it.body)
                    .setSmallIcon(R.drawable.ic_icon)
                    .build()
            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(*//*notification id*//*0, notification)
        }*/
    }

    override fun onNewToken(token: String) {
        //handle token
    }
}