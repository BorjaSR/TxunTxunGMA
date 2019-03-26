package es.bsalazar.txuntxungma.app.events

import android.arch.lifecycle.ViewModel
import android.content.Context
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.data.DataSource

class EventsViewModelFactory(val context: Context, dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventsViewModel(context, dataSource) as T
    }
}