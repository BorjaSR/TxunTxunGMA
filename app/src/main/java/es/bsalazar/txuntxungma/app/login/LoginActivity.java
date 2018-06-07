package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModelProviders;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.base.BaseActivity;
import es.bsalazar.txuntxungma.app.base.BaseViewModel;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginViewModel> {


    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideLoginViewModelFactory())
                .get(LoginViewModel.class);
    }

    @Override
    public void observeViewModel() {

    }
}

