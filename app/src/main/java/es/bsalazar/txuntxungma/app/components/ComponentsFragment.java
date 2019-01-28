package es.bsalazar.txuntxungma.app.components;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.MainActivityViewModel;
import es.bsalazar.txuntxungma.app.base.BaseFragment;

public class ComponentsFragment extends BaseFragment<ComponentsViewModel> {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.components_fragment, container, false);
    }

    @Override
    public ComponentsViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideComponentsViewModelFactory(getContext()))
                .get(ComponentsViewModel.class);
    }

    @Override
    public void observeViewModel() {

    }
}
