package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.MutableLiveData;

import java.util.Calendar;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;
import es.bsalazar.txuntxungma.domain.threading.UseCase;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.GetLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.PerformLoginUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.SaveLoginDataUseCase;
import es.bsalazar.txuntxungma.utils.LogUtils;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;

class LoginViewModel extends BaseViewModel {

    private MutableLiveData<ResultState> loginResult = new MutableLiveData<ResultState>() {
    };
    private MutableLiveData<Auth> saveAuth = new MutableLiveData<Auth>() {
    };

    private GetLoginDataUseCase getLoginDataUseCase;
    private SaveLoginDataUseCase saveLoginDataUseCase;
    private PerformLoginUseCase performLoginUseCase;
    private int roleSelected = 0;

    LoginViewModel(UseCaseHandler useCaseHandler,
                   GetLoginDataUseCase getLoginDataUseCase,
                   SaveLoginDataUseCase saveLoginDataUseCase,
                   PerformLoginUseCase performLoginUseCase) {

        super(useCaseHandler);
        this.getLoginDataUseCase = getLoginDataUseCase;
        this.saveLoginDataUseCase = saveLoginDataUseCase;
        this.performLoginUseCase = performLoginUseCase;
    }

    void performLogin(String pass) {
        loadingProgress.setValue(ShowState.SHOW);
        AuthRequest authRequest = new AuthRequest(String.valueOf(roleSelected), pass);
        PerformLoginUseCase.RequestValues requestValues = new PerformLoginUseCase.RequestValues(authRequest);

        useCaseHandler.execute(performLoginUseCase, requestValues, new UseCase.UseCaseCallback<PerformLoginUseCase.ResponseValue>() {
            @Override
            public void onSuccess(PerformLoginUseCase.ResponseValue response) {
                loadingProgress.setValue(ShowState.HIDE);
                if (response.getLoginSuccess()) {
                    loginResult.setValue(ResultState.OK);
                    saveLoginData();
                } else
                    loginResult.setValue(ResultState.KO);
            }

            @Override
            public void onError(String error, Integer errorCode) {

            }
        });
    }

    private void saveLoginData() {
        Auth auth = new Auth(System.currentTimeMillis(), String.valueOf(roleSelected));
        SaveLoginDataUseCase.RequestValues requestValues = new SaveLoginDataUseCase.RequestValues(auth);

        useCaseHandler.execute(saveLoginDataUseCase, requestValues, new UseCase.UseCaseCallback<SaveLoginDataUseCase.ResponseValue>() {
            @Override
            public void onSuccess(SaveLoginDataUseCase.ResponseValue response) {
                LogUtils.log("saveLoginDataUC", "onSuccess");
            }

            @Override
            public void onError(String error, Integer errorCode) {
                LogUtils.log("saveLoginDataUC", "onError");
            }
        });
    }

    void getLoginData() {

        useCaseHandler.execute(getLoginDataUseCase, new GetLoginDataUseCase.RequestValues(), new UseCase.UseCaseCallback<GetLoginDataUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetLoginDataUseCase.ResponseValue response) {
                saveAuth.setValue(response.getAuth());
            }

            @Override
            public void onError(String error, Integer errorCode) {
                saveAuth.setValue(null);
            }
        });
    }

    void setRoleSelected(int roleSelected) {
        this.roleSelected = roleSelected;
    }

    MutableLiveData<ResultState> getLoginResult() {
        return loginResult;
    }

    MutableLiveData<Auth> getSaveAuth() {
        return saveAuth;
    }
}
