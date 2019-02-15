package es.bsalazar.txuntxungma.app.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.OnClick;
import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.MainActivity;
import es.bsalazar.txuntxungma.app.MainActivityViewModel;
import es.bsalazar.txuntxungma.app.base.BaseFragment;
import es.bsalazar.txuntxungma.app.login.LoginActivity;
import es.bsalazar.txuntxungma.utils.ResultState;

public class HomeFragment extends BaseFragment<HomeViewModel> {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setReturnTransition(new Fade());
            setExitTransition(new Fade());

            setAllowEnterTransitionOverlap(false);
            setAllowReturnTransitionOverlap(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivityViewModel = ViewModelProviders.of(getActivity(),
                Injector.provideMainActivityViewModelFactory(getActivity()))
                .get(MainActivityViewModel.class);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //refion Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                viewModel.removeLoginData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    @Override
    public HomeViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideHomeViewModelFactory(getContext()))
                .get(HomeViewModel.class);
    }

    @Override
    public void observeViewModel() {
        viewModel.getRemoveLoginDataResult().observe(this, this::handleLogout);
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

    private void handleLogout(ResultState resultState){
        if (resultState == ResultState.OK) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}
