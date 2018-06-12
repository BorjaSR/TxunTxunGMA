package es.bsalazar.txuntxungma.app.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.utils.ShowState;

public class BaseViewModel extends ViewModel {

    protected FirestoreSource firestoreSource;
    protected MutableLiveData<ShowState> loadingProgress = new MutableLiveData<ShowState>() {};
    protected MutableLiveData<String> snackbarMessage = new MutableLiveData<String>() {};

    public BaseViewModel(FirestoreSource firestoreSource) {
        this.firestoreSource = firestoreSource;
    }

    public MutableLiveData<ShowState> getLoadingProgress() {
        return loadingProgress;
    }

    public MutableLiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }
}
