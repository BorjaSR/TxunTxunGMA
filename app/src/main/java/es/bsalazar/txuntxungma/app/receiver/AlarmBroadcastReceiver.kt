package es.bsalazar.txuntxungma.app.receiver

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import es.bsalazar.txuntxungma.Injector
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.app.MainActivity
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.domain.entities.BaseError
import es.bsalazar.txuntxungma.domain.entities.DataCallback
import es.bsalazar.txuntxungma.domain.entities.Event
import es.bsalazar.txuntxungma.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.contract

class AlarmBroadcastReceiver : BroadcastReceiver() {

    val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dataSource : DataSource by lazy { Injector.provideDataProvider(mContext) }
    lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context!!
        val eventID = intent?.getStringExtra(Constants.EXTRA_KEY_EVENTS_ID)

        dataSource.getEvent(eventID, object : DataCallback<Event, BaseError>{
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onSuccess(response: Event?) {
                response?.let { launchNotification(response) }
            }

            override fun onFailure(errorBase: BaseError?) {
                dataSource.removeAlarmId(eventID)
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun launchNotification(event: Event){
        val mainIntent = Intent(mContext, MainActivity::class.java)

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(mContext).run {
            addNextIntentWithParentStack(mainIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Create the notification to be shown
        val mBuilder = NotificationCompat.Builder(mContext, "my_app")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("${event.name} a las ${hourFormat.format(event.date)}")
                .setContentText(mContext.getString(R.string.notification_description))
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)

        // Get the Notification manager service
        val am = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show a notification
        am.notify(dataSource.getAlarmId(event.id)!!, mBuilder.build())

        dataSource.removeAlarmId(event.id)
    }
}