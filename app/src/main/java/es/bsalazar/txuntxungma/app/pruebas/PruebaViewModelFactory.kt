package es.bsalazar.txuntxungma.app.pruebas

import android.arch.lifecycle.ViewModel
import android.content.Context
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory
import es.bsalazar.txuntxungma.data.DataSource

class PruebaViewModelFactory(val context: Context, dataSource: DataSource) : BaseViewModelFactory(dataSource) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PruebaViewModel(dataSource) as T
    }
}