package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.FirestoreManager;

public class LoginViewModel extends BaseViewModel {

    public LoginViewModel(FirestoreManager firestoreManager) {
        super(firestoreManager);
    }
}
