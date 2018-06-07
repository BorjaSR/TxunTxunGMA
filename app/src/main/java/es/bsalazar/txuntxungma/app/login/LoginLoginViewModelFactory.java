package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.data.FirestoreManager;

public class LoginLoginViewModelFactory extends BaseViewModelFactory {

    public LoginLoginViewModelFactory(FirestoreManager firestoreManager) {
        super(firestoreManager);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(firestoreManager);
    }
}
