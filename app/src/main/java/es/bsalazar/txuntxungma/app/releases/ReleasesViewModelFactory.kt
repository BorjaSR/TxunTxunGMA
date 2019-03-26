package es.bsalazar.txuntxungma.app.releases

import android.arch.lifecycle.ViewModel
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.data.DataSource

class ReleasesViewModelFactory(dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReleasesViewModel(dataSource) as T
    }
}