package es.bsalazar.txuntxungma.app.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.GetLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.PerformLoginUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.SaveLoginDataUseCase;

public class LoginViewModelFactory extends BaseViewModelFactory {

    private GetLoginDataUseCase getLoginDataUseCase;
    private SaveLoginDataUseCase saveLoginDataUseCase;
    private PerformLoginUseCase performLoginUseCase;

    public LoginViewModelFactory(UseCaseHandler useCaseHandler,
                                 GetLoginDataUseCase getLoginDataUseCase,
                                 SaveLoginDataUseCase saveLoginDataUseCase,
                                 PerformLoginUseCase performLoginUseCase) {
        super(useCaseHandler);
        this.getLoginDataUseCase = getLoginDataUseCase;
        this.saveLoginDataUseCase = saveLoginDataUseCase;
        this.performLoginUseCase = performLoginUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(useCaseHandler, getLoginDataUseCase, saveLoginDataUseCase, performLoginUseCase);
    }
}
