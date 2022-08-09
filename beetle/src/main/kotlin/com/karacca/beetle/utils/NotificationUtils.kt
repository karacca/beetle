package com.karacca.beetle.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.karacca.beetle.R
import com.karacca.beetle.data.model.Issue
import kotlin.random.Random

/**
 * @author karacca
 * @date 8.08.2022
 */

internal object NotificationUtils {

    private const val CHANNEL_ID = "beetle"

    fun sendNotification(context: Context, issue: Issue) {
        createNotificationChannel(context)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(issue.htmlUrl) },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_send)
            .setContentTitle(context.getString(R.string.send_feedback_success_title, issue.number))
            .setContentText(context.getString(R.string.send_feedback_success_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_name_description)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
