package es.bsalazar.txuntxungma.app;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.RemoveLoginDataUseCase;

public class MainActivityViewModelFactory extends BaseViewModelFactory {

    private RemoveLoginDataUseCase removeLoginDataUseCase;

    public MainActivityViewModelFactory(UseCaseHandler useCaseHandler,
                                        RemoveLoginDataUseCase removeLoginDataUseCase) {
        super(useCaseHandler);
        this.removeLoginDataUseCase = removeLoginDataUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(useCaseHandler, removeLoginDataUseCase);
    }
}
