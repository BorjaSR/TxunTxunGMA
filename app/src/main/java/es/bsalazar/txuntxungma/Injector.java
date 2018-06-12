package es.bsalazar.txuntxungma;

import android.content.Context;

import es.bsalazar.txuntxungma.app.login.LoginViewModelFactory;
import es.bsalazar.txuntxungma.data.DataProvider;
import es.bsalazar.txuntxungma.data.DataSource;
import es.bsalazar.txuntxungma.data.local.PreferencesSource;
import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.data.remote.StorageManager;
import es.bsalazar.txuntxungma.domain.use_cases.SaveLoginDataUseCase;

public class Injector {

    //region Data providers
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
    public static LoginViewModelFactory provideLoginViewModelFactory(){
        return new LoginViewModelFactory(provideFirestoreManager());
    }
    //endregion

    //region UC's
    public static SaveLoginDataUseCase provideSaveLoginDataUseCase(Context context){
        return new SaveLoginDataUseCase(provideDataProvider(context));
    }
    //endregion
}
