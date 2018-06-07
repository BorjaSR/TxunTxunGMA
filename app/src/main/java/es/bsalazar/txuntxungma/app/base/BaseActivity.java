package es.bsalazar.txuntxungma.app.base;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {

    public T viewModel;
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public abstract int getLayoutID();

    public abstract T setupViewModel();

    public abstract void observeViewModel();

    public void showSnackbar(String msg) {
        if (getWindow().getCurrentFocus() != null)
            Snackbar.make(getWindow().getCurrentFocus(), msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
