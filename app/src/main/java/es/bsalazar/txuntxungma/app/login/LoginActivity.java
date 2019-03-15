package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import butterknife.BindView;
import butterknife.OnClick;
import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.MainActivity;
import es.bsalazar.txuntxungma.app.base.BaseActivity;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;
import es.bsalazar.txuntxungma.utils.Tools;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginViewModel> {

    @BindView(R.id.spinner_role)
    Spinner spinner_role;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login_progress)
    LinearLayout progress;

    @Override
    public int getView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_role.setAdapter(adapter);
        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getViewModel().setRoleSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        password.setOnEditorActionListener((v, actionId, event) -> {
            performLogin();
            return false;
        });

        getViewModel().getLoginData();
    }

    @Override
    public LoginViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideLoginViewModelFactory(getApplicationContext()))
                .get(LoginViewModel.class);
    }

    @Override
    public void observeViewModel() {
        getViewModel().getLoadingProgress().observe(this, this::toogleLoading);
        getViewModel().getLoginResult().observe(this, this::handleLoginResult);
        getViewModel().getSaveAuth().observe(this, this::handleAuth);
    }

    @OnClick(R.id.sign_in_button)
    public void performLogin() {
        getViewModel().performLogin(Tools.encryptMD5(password.getText().toString()));
    }

    public void toogleLoading(ShowState showState) {
        progress.setVisibility(showState == ShowState.SHOW ? View.VISIBLE : View.GONE);
    }

    public void handleLoginResult(ResultState resultState) {
        if (resultState == ResultState.OK){
          startActivity(new Intent(this, MainActivity.class));
          finish();
        } else
            showSnackbar(getString(R.string.wrong_password));
    }

    public void handleAuth(Auth auth) {
        if (auth != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

