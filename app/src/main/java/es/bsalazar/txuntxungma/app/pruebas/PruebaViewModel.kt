package es.bsalazar.txuntxungma.app.pruebas

import es.bsalazar.txuntxungma.app.base.lists.BaseListViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.domain.entities.Event

class PruebaViewModel(dataSource: DataSource) : BaseListViewModel<Event>(dataSource) {

    fun getEvents(){ execute(dataSource::getEvents) }
}