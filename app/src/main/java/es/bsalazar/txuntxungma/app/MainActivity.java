package es.bsalazar.txuntxungma.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.base.BaseActivity;
import es.bsalazar.txuntxungma.app.base.BaseFragment;
import es.bsalazar.txuntxungma.app.components.ComponentsFragment;
import es.bsalazar.txuntxungma.app.events.EventsFragment;
import es.bsalazar.txuntxungma.app.home.HomeFragment;
import es.bsalazar.txuntxungma.app.login.LoginActivity;
import es.bsalazar.txuntxungma.app.rates.RatesFragment;
import es.bsalazar.txuntxungma.utils.Constants;
import es.bsalazar.txuntxungma.utils.ResultState;

public class MainActivity extends BaseActivity<MainActivityViewModel> {

    public static final int HOME_FRAGMENT = 0;
    public static final int COMPONENTS_FRAGMENT = 1;
    public static final int RATES_FRAGMENT = 2;
    public static final int EVENTS_FRAGMENT = 3;
    public static final int RELEASES_FRAGMENT = 4;

    @Override
    public int getView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getTagFromActualFragment() != null &&
                getSupportActionBar() != null &&
                getTagFromActualFragment().equals(Constants.HOME_FRAGMENT))
            getSupportActionBar().setTitle(getString(R.string.fragment_title_home));
    }

    @NonNull
    @Override
    public MainActivityViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideMainActivityViewModelFactory(this))
                .get(MainActivityViewModel.class);
    }

    @Override
    public void observeViewModel() {
        getViewModel().getRemoveLoginDataResult().observe(this, this::logout);
        getViewModel().getFragmentID().observe(this, this::changeFragment);
    }

    private void logout(ResultState resultState) {
        if (resultState == ResultState.OK) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


    private void setInitialFragment() {
        BaseFragment initialFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, initialFragment, initialFragment.provideTag())
                .commit();
    }

    private void changeFragment(Integer fragmentID) {
        BaseFragment newFragment = null;

        switch (fragmentID) {
            case HOME_FRAGMENT:
                newFragment = new HomeFragment();
                getSupportActionBar().setTitle(getString(R.string.fragment_title_home));
                break;
            case COMPONENTS_FRAGMENT:
                newFragment = new ComponentsFragment();
                getSupportActionBar().setTitle(getString(R.string.fragment_title_components));
                break;
            case RATES_FRAGMENT:
                newFragment = new RatesFragment();
                getSupportActionBar().setTitle(getString(R.string.fragment_title_rates));
                break;
            case EVENTS_FRAGMENT:
                newFragment = new EventsFragment();
                getSupportActionBar().setTitle(getString(R.string.fragment_title_events));
                break;
        }

        if (newFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newFragment,  newFragment.provideTag())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
