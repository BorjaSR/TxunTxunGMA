package es.bsalazar.txuntxungma.app.home;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.app.components.ComponentsViewModel;
import es.bsalazar.txuntxungma.data.DataSource;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;

public class HomeViewModelFactory extends BaseViewModelFactory {

    private DataSource dataSource;

    public HomeViewModelFactory(UseCaseHandler useCaseHandler, DataSource dataSource) {
        super(useCaseHandler);
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(useCaseHandler, dataSource);
    }
}
