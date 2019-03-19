package es.bsalazar.txuntxungma.app.events

import android.arch.lifecycle.ViewModel
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.data.DataSource

class EventsViewModelFactory(dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventsViewModel(dataSource) as T
    }
}