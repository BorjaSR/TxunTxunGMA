package es.bsalazar.txuntxungma.app.pruebas

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.bsalazar.txuntxungma.Injector
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.app.base.lists.BaseListFragment
import es.bsalazar.txuntxungma.domain.entities.Event

class PruebaFragment : BaseListFragment<Event, PruebaViewModel, PruebaAdapter>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEvents()
    }

    override fun provideTag() = "Prueba Fragment"

    override fun createAdapter() = PruebaAdapter()

    override fun setupViewModel() = ViewModelProviders.of(this,
            Injector.provideEventsViewModelFactory(context))
            .get(PruebaViewModel::class.java)
}