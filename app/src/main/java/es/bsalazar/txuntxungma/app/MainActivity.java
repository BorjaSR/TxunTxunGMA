package es.bsalazar.txuntxungma.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.base.BaseActivity;
import es.bsalazar.txuntxungma.app.login.LoginActivity;
import es.bsalazar.txuntxungma.utils.ResultState;

public class MainActivity extends BaseActivity<MainActivityViewModel> {


    @Override
    public int getView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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

    @Override
    public MainActivityViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideMainActivityViewModelFactory(this))
                .get(MainActivityViewModel.class);
    }

    @Override
    public void observeViewModel() {
        viewModel.getRemoveLoginDataResult().observe(this, this::logout);
    }

    private void logout(ResultState resultState) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
