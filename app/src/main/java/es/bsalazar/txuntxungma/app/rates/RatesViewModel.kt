package es.bsalazar.txuntxungma.app.rates

import android.arch.lifecycle.MutableLiveData

import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.domain.entities.*
import es.bsalazar.txuntxungma.utils.ShowState

class RatesViewModel(dataSource: DataSource) : BaseViewModel(dataSource) {

    val ratesLiveData = object : MutableLiveData<List<Rate>>() {}
    val authLiveData = object : MutableLiveData<Auth>() {}

    val addRateResponse = object : MutableLiveData<FirebaseResponse<Rate>>() {}
    val updateRateResponse = object : MutableLiveData<FirebaseResponse<Rate>>() {}
    val deleteRateResponse = object : MutableLiveData<FirebaseResponse<Rate>>() {}


    private var initialize: Boolean = false

    fun getRates() {
        loadingProgress.value = ShowState.SHOW
        initialize = true
        dataSource.getRates(object : FirestoreSource.OnCollectionChangedListener<Rate> {
            override fun onCollectionChange(collection: List<Rate>) {
                if (initialize) {
                    loadingProgress.value = ShowState.HIDE
                    ratesLiveData.value = collection
                    initialize = false
                }
            }

            override fun onDocumentAdded(index: Int, document: Rate) {
                if (!initialize)
                    addRateResponse.value = FirebaseResponse(index, document)

            }

            override fun onDocumentChanged(index: Int, document: Rate) {
                if (!initialize)
                    updateRateResponse.value = FirebaseResponse(index, document)
            }

            override fun onDocumentRemoved(index: Int, document: Rate) {
                if (!initialize)
                    deleteRateResponse.value = FirebaseResponse(index, document)
            }
        })
    }

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

    fun saveRate(rate: Rate) = dataSource.saveRate(rate) {}

    fun modifyRate(rate: Rate) = dataSource.updateRate(rate) {}

    fun deleteRate(rate: Rate) = dataSource.deleteRate(rate.id)
}