package es.bsalazar.txuntxungma.app.releases

import android.arch.lifecycle.ViewModel
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.app.events.EventsViewModel
import es.bsalazar.txuntxungma.app.rates.RatesViewModel
import es.bsalazar.txuntxungma.data.DataSource

class ReleasesViewModelFactory(dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RatesViewModel(dataSource) as T
    }
}