package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;

public class LoginViewModelFactory extends BaseViewModelFactory {

    public LoginViewModelFactory(FirestoreSource firestoreSource) {
        super(firestoreSource);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(firestoreSource);
    }
}
