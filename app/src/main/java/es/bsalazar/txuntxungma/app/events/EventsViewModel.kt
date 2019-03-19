package es.bsalazar.txuntxungma.app.events

import android.arch.lifecycle.MutableLiveData
import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.domain.entities.Auth
import es.bsalazar.txuntxungma.domain.entities.BaseError
import es.bsalazar.txuntxungma.domain.entities.DataCallback
import es.bsalazar.txuntxungma.domain.entities.Event
import es.bsalazar.txuntxungma.utils.ShowState

class EventsViewModel(dataSource: DataSource) : BaseViewModel(dataSource) {

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

    fun getEvents(){
        loadingProgress.value = ShowState.SHOW
        var init = true
        dataSource.getEvents(object : FirestoreSource.OnCollectionChangedListener<Event> {
            override fun onCollectionChange(collection: MutableList<Event>?) {
                if(init){
                    eventsLiveData.value = collection
                    loadingProgress.value = ShowState.HIDE
                    init = false
                }
            }

            override fun onDocumentAdded(index: Int, document: Event) {
                if(!init) addEventLiveData.value = FirebaseResponse(index, document)
            }

            override fun onDocumentChanged(index: Int, document: Event) {
                if(!init) modifyEventLiveData.value = FirebaseResponse(index, document)
            }

            override fun onDocumentRemoved(index: Int, document: Event) {
                if(!init) deleteEventLiveData.value = FirebaseResponse(index, document)
            }
        })
    }

    fun saveEvent(event: Event) = dataSource.saveEvent(event) {}

    fun modifyEvent(event: Event) = dataSource.updateEvent(event) {}

    fun deleteEvent(event: Event) = dataSource.deleteEvent(event.id)
}