package es.bsalazar.txuntxungma.app.base;

import android.arch.lifecycle.ViewModelProvider;

import es.bsalazar.txuntxungma.data.remote.FirestoreSource;

public class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory  {

    protected FirestoreSource firestoreSource;

    public BaseViewModelFactory(FirestoreSource firestoreSource) {
        this.firestoreSource = firestoreSource;
    }
}
