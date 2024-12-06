package fr.isen.baudillon.isensmartcompanion


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "default_channel"
        val channelName = "Default Notifications"
        val description = "Channel for default notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            this.description = description
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotification(context: Context, eventTitle:String) {
    val channelId = "default_channel"

    // Vérification de la permission
    if (!hasNotificationPermission(context)) {
        Log.e("Notification", "Permission non accordée pour POST_NOTIFICATIONS")
        return
    }

    try {
        // Intent pour ouvrir MainActivity
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construire la notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notificon) // Remplacez par votre icône
            .setContentTitle("Notifications Activées")
            .setContentText("Vous avez activé les notifications pour $eventTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Supprimer après clic
            .build()

        // Afficher la notification
        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    } catch (e: SecurityException) {
        Log.e("Notification", "Erreur de sécurité : ${e.message}")
    }
}

fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        // Permissions not needed for older versions
        true
    }
}

fun delayNotification(context: Context, eventTitle: String) {
    Handler(Looper.getMainLooper()).postDelayed({
        showNotification(context, eventTitle)
    }, 10000)
}



