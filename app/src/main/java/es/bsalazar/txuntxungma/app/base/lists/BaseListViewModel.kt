package es.bsalazar.txuntxungma.app.base.lists

import android.arch.lifecycle.MutableLiveData
import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.utils.ShowState

open class BaseListViewModel<T>(dataSource: DataSource) : BaseViewModel(dataSource) {

    val listLiveData = object : MutableLiveData<List<T>>() {}
    val addItemLiveData = object : MutableLiveData<FirebaseResponse<T>>() {}
    val modifyItemLiveData = object : MutableLiveData<FirebaseResponse<T>>() {}
    val deleteItemLiveData = object : MutableLiveData<FirebaseResponse<T>>() {}

    var init = true

    fun execute(execution: (FirestoreSource.OnCollectionChangedListener<T>) -> (Unit)) {
        loadingProgress.value = ShowState.SHOW
        init = true
        execution(object : FirestoreSource.OnCollectionChangedListener<T> {
            override fun onCollectionChange(collection: MutableList<T>?) {
                collectionChange(collection)
            }

            override fun onDocumentAdded(index: Int, document: T) {
                documentAdded(index, document)
            }

            override fun onDocumentChanged(index: Int, document: T) {
                documentChanged(index, document)
            }

            override fun onDocumentRemoved(index: Int, document: T) {
                documentRemoved(index, document)
            }
        })
    }

    open fun collectionChange(collection: MutableList<T>?) {
        if (init) {
            listLiveData.value = collection
            loadingProgress.value = ShowState.HIDE
            init = false
        }
    }

    open fun documentAdded(index: Int, document: T) {
        if (!init) addItemLiveData.value = FirebaseResponse(index, document)
    }

    open fun documentChanged(index: Int, document: T) {
        if (!init) modifyItemLiveData.value = FirebaseResponse(index, document)
    }

    open fun documentRemoved(index: Int, document: T) {
        if (!init) deleteItemLiveData.value = FirebaseResponse(index, document)
    }
}