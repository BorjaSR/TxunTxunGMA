package es.bsalazar.txuntxungma.app;

import android.arch.lifecycle.MutableLiveData;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;
import es.bsalazar.txuntxungma.domain.threading.UseCase;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.GetLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.PerformLoginUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.RemoveLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.SaveLoginDataUseCase;
import es.bsalazar.txuntxungma.utils.LogUtils;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;

class MainActivityViewModel extends BaseViewModel {

    private RemoveLoginDataUseCase removeLoginDataUseCase;
    private MutableLiveData<ResultState> removeLoginDataResult = new MutableLiveData<ResultState>(){};

    MainActivityViewModel(UseCaseHandler useCaseHandler,
                          RemoveLoginDataUseCase removeLoginDataUseCase) {

        super(useCaseHandler);
        this.removeLoginDataUseCase = removeLoginDataUseCase;
    }

    void removeLoginData() {
        loadingProgress.setValue(ShowState.SHOW);

        useCaseHandler.execute(removeLoginDataUseCase, new RemoveLoginDataUseCase.RequestValues(), new UseCase.UseCaseCallback<RemoveLoginDataUseCase.ResponseValue>() {
            @Override
            public void onSuccess(RemoveLoginDataUseCase.ResponseValue response) {
                removeLoginDataResult.setValue(ResultState.OK);
            }

            @Override
            public void onError(String error, Integer errorCode) {
                removeLoginDataResult.setValue(ResultState.KO);
            }
        });
    }

    public MutableLiveData<ResultState> getRemoveLoginDataResult() {
        return removeLoginDataResult;
    }
}
