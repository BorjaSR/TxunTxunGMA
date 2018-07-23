package es.bsalazar.txuntxungma.app.base;

import android.arch.lifecycle.ViewModelProvider;

import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;

public class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory  {

    protected UseCaseHandler useCaseHandler;

    public BaseViewModelFactory(UseCaseHandler useCaseHandler) {
        this.useCaseHandler = useCaseHandler;
    }
}
