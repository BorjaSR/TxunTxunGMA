package es.bsalazar.txuntxungma.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.base.BaseActivity;
import es.bsalazar.txuntxungma.app.components.ComponentsFragment;
import es.bsalazar.txuntxungma.app.home.HomeFragment;
import es.bsalazar.txuntxungma.app.login.LoginActivity;
import es.bsalazar.txuntxungma.app.rates.RatesFragment;
import es.bsalazar.txuntxungma.utils.ResultState;

public class MainActivity extends BaseActivity<MainActivityViewModel> {

    public static final int HOME_FRAGMENT = 0;
    public static final int COMPONENTS_FRAGMENT = 1;
    public static final int RATES_FRAGMENT = 2;
    public static final int CALENDAR_FRAGMENT = 3;
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
    public MainActivityViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideMainActivityViewModelFactory(this))
                .get(MainActivityViewModel.class);
    }

    @Override
    public void observeViewModel() {
        viewModel.getRemoveLoginDataResult().observe(this, this::logout);
        viewModel.getFragmentID().observe(this, this::changeFragment);
    }

    private void logout(ResultState resultState) {
        if (resultState == ResultState.OK) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


    private void setInitialFragment() {
        Fragment initialFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, initialFragment)
                .commit();
    }

    private void changeFragment(Integer fragmentID) {
        Fragment newFragment = null;

        switch (fragmentID) {
            case HOME_FRAGMENT:
                newFragment = new HomeFragment();
                break;
            case COMPONENTS_FRAGMENT:
                newFragment = new ComponentsFragment();
                break;
            case RATES_FRAGMENT:
                newFragment = new RatesFragment();
                break;
        }

        if (newFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
