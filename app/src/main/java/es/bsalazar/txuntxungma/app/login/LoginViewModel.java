package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.MutableLiveData;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;

class LoginViewModel extends BaseViewModel {

    private int roleSelected = 0;
    private MutableLiveData<ResultState> loginResult = new MutableLiveData<ResultState>() {
    };

    LoginViewModel(FirestoreSource firestoreSource) {
        super(firestoreSource);
    }

    void performLogin(String pass) {
        loadingProgress.setValue(ShowState.SHOW);
        firestoreSource.getAuth(String.valueOf(roleSelected), document -> {
            loadingProgress.setValue(ShowState.HIDE);
            if (document != null && pass.equals(document))
                loginResult.setValue(ResultState.OK);
            else
                loginResult.setValue(ResultState.KO);
        });
    }

    void setRoleSelected(int roleSelected) {
        this.roleSelected = roleSelected;
    }

    MutableLiveData<ResultState> getLoginResult() {
        return loginResult;
    }
}
