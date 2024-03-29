package com.dicoding.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        //TODO 14 DONE : If notification preference on, get nearest active task from repository and show notification with pending intent
       val sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val pref = sharedPreference.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        if (pref) {
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val repository = TaskRepository.getInstance(applicationContext)
            val nearestActiveTask = repository.getNearestActiveTask()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val name = channelName?: "Channel Name"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
                notificationManager.createNotificationChannel(mChannel)
            }

            val notifBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(nearestActiveTask.title)
                .setContentText(String.format(
                    applicationContext.getString(R.string.notify_content),
                    DateConverter.convertMillisToString(nearestActiveTask.dueDateMillis)
                ))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(task = nearestActiveTask))
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                notificationManager.notify(0, notifBuilder.build())
            }
        }
        return Result.success()
    }

}
