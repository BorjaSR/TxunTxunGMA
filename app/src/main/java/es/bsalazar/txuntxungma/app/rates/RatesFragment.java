package es.bsalazar.txuntxungma.app.rates;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.base.BaseFragment;
import es.bsalazar.txuntxungma.domain.entities.Rate;

public class RatesFragment extends BaseFragment<RatesViewModel> {

    private boolean scheduleAnim = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setReturnTransition(new Fade());
            setExitTransition(new Fade());

            setAllowEnterTransitionOverlap(false);
            setAllowReturnTransitionOverlap(false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getRates();
    }

    @Override
    public RatesViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideRatesViewModelFactory(mContext))
                .get(RatesViewModel.class);
    }

    @Override
    public void observeViewModel() {
        viewModel.getRatesLiveData().observe(this, this::presentRatesList);
    }



    private void initRecycler() {
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        adapter = new ComponentsAdapter();
//        recyclerView.setAdapter(adapter);
//        adapter.setComponentEditListener(this);
    }

    private void presentRatesList(List<Rate> rates) {
        Log.d("[Rates]", rates.toString());
//        if (scheduleAnim) recyclerView.scheduleLayoutAnimation();
//        scheduleAnim = false;
//        adapter.setComponents(components);
    }
}
