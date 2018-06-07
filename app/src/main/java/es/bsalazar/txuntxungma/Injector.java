package es.bsalazar.txuntxungma;

import es.bsalazar.txuntxungma.app.login.LoginLoginViewModelFactory;
import es.bsalazar.txuntxungma.data.FirestoreManager;
import es.bsalazar.txuntxungma.data.StorageManager;

public class Injector {

    public static FirestoreManager provideFirestoreManager(){
        return FirestoreManager.getInstance();
    }

    public static StorageManager provideStorageManager(){
        return StorageManager.getInstance();
    }

    public static LoginLoginViewModelFactory provideLoginViewModelFactory(){
        return new LoginLoginViewModelFactory(provideFirestoreManager());
    }
}
