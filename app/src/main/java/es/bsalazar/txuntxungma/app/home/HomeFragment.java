package es.bsalazar.txuntxungma.app.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.MainActivity;
import es.bsalazar.txuntxungma.app.MainActivityViewModel;
import es.bsalazar.txuntxungma.app.base.BaseFragment;
import es.bsalazar.txuntxungma.app.base.BaseViewModel;

public class HomeFragment extends BaseFragment {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(getActivity(),
                Injector.provideMainActivityViewModelFactory(getActivity()))
                .get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public BaseViewModel setupViewModel() {
        return null;
    }

    @Override
    public void observeViewModel() {
        //NOTHING TO OBSERVE
    }

    @OnClick(R.id.components_card)
    public void goToComponentsFragment(){
        mainActivityViewModel.setFragmentID(MainActivity.COMPONENTS_FRAGMENT);
    }

    @OnClick(R.id.rates_card)
    public void goToRatesFragment(){
        mainActivityViewModel.setFragmentID(MainActivity.RATES_FRAGMENT);

    }
    @OnClick(R.id.calendar_card)
    public void goToCalendarFragment(){
        mainActivityViewModel.setFragmentID(MainActivity.CALENDAR_FRAGMENT);

    }

    @OnClick(R.id.releases_card)
    public void goToReleasesFragment(){
        mainActivityViewModel.setFragmentID(MainActivity.RELEASES_FRAGMENT);
    }
}
