package es.bsalazar.txuntxungma.app.rates;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import es.bsalazar.txuntxungma.app.base.BaseViewModelFactory;
import es.bsalazar.txuntxungma.data.DataSource;

public class RatesViewModelFactory extends BaseViewModelFactory {

    public RatesViewModelFactory(DataSource dataSource) {
        super(dataSource);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RatesViewModel(dataSource);
    }
}
