package es.bsalazar.txuntxungma.app.home;

import android.arch.lifecycle.MutableLiveData;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.DataSource;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.RemoveLoginDataUseCase;
import es.bsalazar.txuntxungma.utils.ResultState;
import es.bsalazar.txuntxungma.utils.ShowState;

class HomeViewModel extends BaseViewModel {

    private DataSource dataSource;
    private MutableLiveData<ResultState> removeLoginDataResult = new MutableLiveData<ResultState>(){};

    HomeViewModel(UseCaseHandler useCaseHandler, DataSource dataSource) {
        super(useCaseHandler);
        this.dataSource = dataSource;
    }

    void removeLoginData() {
        dataSource.removeLoginData(new DataCallback<Boolean, BaseError>() {
            @Override
            public void onSuccess(Boolean result) {
                removeLoginDataResult.setValue(ResultState.OK);
            }

            @Override
            public void onFailure(BaseError errorBase) {
                removeLoginDataResult.setValue(ResultState.KO);
            }
        });
    }

    public MutableLiveData<ResultState> getRemoveLoginDataResult() {
        return removeLoginDataResult;
    }
}
