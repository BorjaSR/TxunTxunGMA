package es.bsalazar.txuntxungma.app.base;

import android.arch.lifecycle.ViewModelProvider;

import es.bsalazar.txuntxungma.data.FirestoreManager;

public class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory  {

    protected FirestoreManager firestoreManager;

    public BaseViewModelFactory(FirestoreManager firestoreManager) {
        this.firestoreManager = firestoreManager;
    }
}
