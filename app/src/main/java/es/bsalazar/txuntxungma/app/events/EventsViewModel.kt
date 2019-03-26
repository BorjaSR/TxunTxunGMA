package es.bsalazar.txuntxungma.app.events

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.app.receiver.AlarmBroadcastReceiver
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.domain.entities.Auth
import es.bsalazar.txuntxungma.domain.entities.BaseError
import es.bsalazar.txuntxungma.domain.entities.DataCallback
import es.bsalazar.txuntxungma.domain.entities.Event
import es.bsalazar.txuntxungma.utils.Constants
import es.bsalazar.txuntxungma.utils.ShowState

class EventsViewModel(val context: Context, dataSource: DataSource) : BaseViewModel(dataSource) {

    val eventsLiveData = object : MutableLiveData<List<Event>>() {}
    val authLiveData = object : MutableLiveData<Auth>() {}

    val addEventLiveData = object : MutableLiveData<FirebaseResponse<Event>>() {}
    val modifyEventLiveData = object : MutableLiveData<FirebaseResponse<Event>>() {}
    val deleteEventLiveData = object : MutableLiveData<FirebaseResponse<Event>>() {}

    fun getLoginData() {
        dataSource.getLoginData(object : DataCallback<Auth, BaseError> {
            override fun onSuccess(response: Auth?) {
                authLiveData.value = response
            }

            override fun onFailure(errorBase: BaseError?) {
                authLiveData.value = null
            }
        })
    }

    fun getEvents() {
        loadingProgress.value = ShowState.SHOW
        var init = true
        dataSource.getEvents(object : FirestoreSource.OnCollectionChangedListener<Event> {
            override fun onCollectionChange(collection: MutableList<Event>?) {
                if (init) {
                    checkForAllAlarms(collection)
                    eventsLiveData.value = collection
                    loadingProgress.value = ShowState.HIDE
                    init = false
                }
            }

            override fun onDocumentAdded(index: Int, document: Event) {
                if (!init) addEventLiveData.value = FirebaseResponse(index, document)
            }

            override fun onDocumentChanged(index: Int, document: Event) {
                if (!init) {
                    checkForAlarmToEdit(document)
                    modifyEventLiveData.value = FirebaseResponse(index, document)
                }
            }

            override fun onDocumentRemoved(index: Int, document: Event) {
                if (!init) {
                    checkForAlarmToDelete(document)
                    deleteEventLiveData.value = FirebaseResponse(index, document)
                }
            }
        })
    }

    fun saveEvent(event: Event) = dataSource.saveEvent(event) {}

    fun modifyEvent(event: Event) = dataSource.updateEvent(event) {}

    fun deleteEvent(event: Event) = dataSource.deleteEvent(event.id)

    fun activateAlarmForEvent(event: Event) {
        var alarmId = dataSource.getAlarmId(event.id)
        if (alarmId == -1)//If not exist alarm previously
            alarmId = dataSource.saveAlarmId(event.id)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val broadcastIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        broadcastIntent.putExtra(Constants.EXTRA_KEY_EVENTS_ID, event.id)
        val pIntent = PendingIntent.getBroadcast(context, alarmId, broadcastIntent, 0)

        // Set an alarm to trigger 5 second after this code is called
        alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                event.date - (2 * 60 * 60 * 1000), //2 horas antes
                pIntent
        )

        for (i in 0..eventsLiveData.value?.size!!) {
            if (eventsLiveData.value!![i].id.equals(event.id)) {
                eventsLiveData.value!![i].alarmActivated = true
                modifyEventLiveData.value = FirebaseResponse(i, eventsLiveData.value!![i])
                break
            }
        }
    }

    fun defuseAlarmForEvent(event: Event) {
        val alarmId = dataSource.getAlarmId(event.id)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val broadcastIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        broadcastIntent.putExtra(Constants.EXTRA_KEY_EVENTS_ID, event.id)
        val pIntent = PendingIntent.getBroadcast(context, alarmId, broadcastIntent, 0)

        // Set an alarm to trigger 5 second after this code is called
        alarmMgr.cancel(pIntent)

        for (i in 0..eventsLiveData.value?.size!!) {
            if (eventsLiveData.value!![i].id.equals(event.id)) {
                eventsLiveData.value!![i].alarmActivated = false
                modifyEventLiveData.value = FirebaseResponse(i, eventsLiveData.value!![i])
                break
            }
        }

        dataSource.removeAlarmId(event.id)
    }

    fun checkForAllAlarms(events: List<Event>?) {
        if(events != null){
            for (event in events){
                val alarmId = dataSource.getAlarmId(event.id)

                if (alarmId != -1) {
                    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val broadcastIntent = Intent(context, AlarmBroadcastReceiver::class.java)
                    broadcastIntent.putExtra(Constants.EXTRA_KEY_EVENTS_ID, event.id)
                    val pIntent = PendingIntent.getBroadcast(context, alarmId, broadcastIntent, 0)

                    // Set an alarm to trigger 5 second after this code is called
                    alarmMgr.set(
                            AlarmManager.RTC_WAKEUP,
                            event.date - (2 * 60 * 60 * 1000), //2 horas antes
                            pIntent
                    )
                }
            }
        }
    }

    fun checkForAlarmToEdit(event: Event) {
        val alarmId = dataSource.getAlarmId(event.id)

        if (alarmId != -1) {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val broadcastIntent = Intent(context, AlarmBroadcastReceiver::class.java)
            broadcastIntent.putExtra(Constants.EXTRA_KEY_EVENTS_ID, event.id)
            val pIntent = PendingIntent.getBroadcast(context, alarmId, broadcastIntent, 0)

            // Set an alarm to trigger 5 second after this code is called
            alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    event.date - (2 * 60 * 60 * 1000), //2 horas antes
                    pIntent
            )
        }
    }

    fun checkForAlarmToDelete(event: Event) {
        val alarmId = dataSource.getAlarmId(event.id)

        if (alarmId != -1) {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val broadcastIntent = Intent(context, AlarmBroadcastReceiver::class.java)
            broadcastIntent.putExtra(Constants.EXTRA_KEY_EVENTS_ID, event.id)
            val pIntent = PendingIntent.getBroadcast(context, alarmId, broadcastIntent, 0)

            // Set an alarm to trigger 5 second after this code is called
            alarmMgr.cancel(pIntent)

            dataSource.removeAlarmId(event.id)
        }
    }
}