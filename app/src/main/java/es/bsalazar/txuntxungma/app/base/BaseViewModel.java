package es.bsalazar.txuntxungma.app.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import es.bsalazar.txuntxungma.data.FirestoreManager;
import es.bsalazar.txuntxungma.utils.ShowState;

public class BaseViewModel extends ViewModel {

    protected FirestoreManager firestoreManager;
    protected MutableLiveData<ShowState> loadingProgress = new MutableLiveData<ShowState>() {};
    protected MutableLiveData<String> snackbarMessage = new MutableLiveData<String>() {};

    public BaseViewModel(FirestoreManager firestoreManager) {
        this.firestoreManager = firestoreManager;
    }

    public MutableLiveData<ShowState> getLoadingProgress() {
        return loadingProgress;
    }

    public MutableLiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }
}
