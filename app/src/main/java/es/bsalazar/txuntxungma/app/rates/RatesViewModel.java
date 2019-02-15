package es.bsalazar.txuntxungma.app.rates;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import es.bsalazar.txuntxungma.app.base.BaseViewModel;
import es.bsalazar.txuntxungma.data.DataSource;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Rate;
import es.bsalazar.txuntxungma.utils.ShowState;

class RatesViewModel extends BaseViewModel {

    private MutableLiveData<List<Rate>> ratesLiveData = new MutableLiveData<List<Rate>>() {
    };

    RatesViewModel(DataSource dataSource) {
        super(dataSource);
    }

    private boolean initialize;

    void getRates() {
        loadingProgress.setValue(ShowState.SHOW);
        initialize = true;
        dataSource.getRates(new FirestoreSource.OnCollectionChangedListener<Rate>() {
            @Override
            public void onCollectionChange(List<Rate> collection) {
                if (initialize) {
                    loadingProgress.setValue(ShowState.HIDE);
                    ratesLiveData.setValue(collection);
                    initialize = false;
                }
            }

            @Override
            public void onDocumentAdded(int index, Rate object) {
                if (!initialize) {

                }
            }

            @Override
            public void onDocumentChanged(int index, Rate object) {
                if (!initialize) {

                }
            }

            @Override
            public void onDocumentRemoved(int index, Rate object) {
                if (!initialize) {

                }
            }
        });
    }

    public MutableLiveData<List<Rate>> getRatesLiveData() {
        return ratesLiveData;
    }
}
