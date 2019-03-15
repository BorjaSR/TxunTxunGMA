package es.bsalazar.txuntxungma.app.rates

import android.arch.lifecycle.ViewModel

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.data.DataSource

class RatesViewModelFactory(dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RatesViewModel(dataSource) as T
    }
}
