package es.bsalazar.txuntxungma.app.components;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.MainActivityViewModel;
import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;

public class ComponentsViewModelFactory extends BaseViewModelFactory {

    public ComponentsViewModelFactory(UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ComponentsViewModel(useCaseHandler);
    }
}
