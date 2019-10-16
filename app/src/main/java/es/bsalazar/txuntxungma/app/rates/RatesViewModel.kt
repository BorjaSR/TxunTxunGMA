package es.bsalazar.txuntxungma.app.rates

import android.arch.lifecycle.MutableLiveData

import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.app.base.lists.BaseListViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.domain.entities.*
import es.bsalazar.txuntxungma.utils.ShowState

class RatesViewModel(dataSource: DataSource) : BaseListViewModel<Rate>(dataSource) {

    val authLiveData = object : MutableLiveData<Auth>() {}

    fun getRates() { execute(dataSource::getRates) }

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