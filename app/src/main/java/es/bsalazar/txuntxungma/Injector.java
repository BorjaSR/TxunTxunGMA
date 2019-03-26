package es.bsalazar.txuntxungma;

import android.content.Context;

import es.bsalazar.txuntxungma.app.MainActivityViewModelFactory;
import es.bsalazar.txuntxungma.app.components.ComponentsViewModelFactory;
import es.bsalazar.txuntxungma.app.events.EventsViewModelFactory;
import es.bsalazar.txuntxungma.app.home.HomeViewModelFactory;
import es.bsalazar.txuntxungma.app.login.LoginViewModelFactory;
import es.bsalazar.txuntxungma.app.rates.RatesViewModelFactory;
import es.bsalazar.txuntxungma.app.releases.ReleasesViewModelFactory;
import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.data.local.PreferencesSource;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.data.remote.StorageManager;
import es.bsalazar.txuntxungma.domain.threading.UseCaseHandler;
import es.bsalazar.txuntxungma.domain.use_cases.GetLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.PerformLoginUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.RemoveLoginDataUseCase;
import es.bsalazar.txuntxungma.domain.use_cases.SaveLoginDataUseCase;

public class Injector {

    //region Data providers
    private static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static DataProvider provideDataProvider(Context context){
        return DataProvider.getInstance(
                providePreferencesSource(context),
                provideFirestoreManager());
    }

    private static PreferencesSource providePreferencesSource(Context context){
        return PreferencesSource.getInstance(context);
    }

    private static FirestoreSource provideFirestoreManager(){
        return FirestoreSource.getInstance();
    }

    public static StorageManager provideStorageManager(){
        return StorageManager.getInstance();
    }
    //endregion

    //region ViewModel Factories
    public static LoginViewModelFactory provideLoginViewModelFactory(Context context){
        return new LoginViewModelFactory(provideUseCaseHandler(),
                provideGetLoginDataUseCase(context),
                provideSaveLoginDataUseCase(context),
                providePerformLoginUseCase(context));
    }

    public static MainActivityViewModelFactory provideMainActivityViewModelFactory(Context context){
        return new MainActivityViewModelFactory(provideUseCaseHandler(),
                provideRemoveLoginDataUseCase(context));
    }

    public static ComponentsViewModelFactory provideComponentsViewModelFactory(Context context){
        return new ComponentsViewModelFactory(provideUseCaseHandler());
    }

    public static HomeViewModelFactory provideHomeViewModelFactory(Context context){
        return new HomeViewModelFactory(provideUseCaseHandler(), provideDataProvider(context));
    }

    public static RatesViewModelFactory provideRatesViewModelFactory(Context context){
        return new RatesViewModelFactory(provideDataProvider(context));
    }

    public static EventsViewModelFactory provideEventsViewModelFactory(Context context){
        return new EventsViewModelFactory(context, provideDataProvider(context));
    }

    public static ReleasesViewModelFactory provideReleasesViewModelFactory(Context context){
        return new ReleasesViewModelFactory(provideDataProvider(context));
    }
    //endregion

    //region UC's
    private static SaveLoginDataUseCase provideSaveLoginDataUseCase(Context context){
        return new SaveLoginDataUseCase(provideDataProvider(context));
    }

    private static GetLoginDataUseCase provideGetLoginDataUseCase(Context context){
        return new GetLoginDataUseCase(provideDataProvider(context));
    }

    private static RemoveLoginDataUseCase provideRemoveLoginDataUseCase(Context context){
        return new RemoveLoginDataUseCase(provideDataProvider(context));
    }

    private static PerformLoginUseCase providePerformLoginUseCase(Context context){
        return new PerformLoginUseCase(provideDataProvider(context));
    }
    //endregion
}
