package es.bsalazar.txuntxungma.data;

import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.data.local.IPreferencesSource;
import es.bsalazar.txuntxungma.data.remote.IFirestoreSource;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;

public class DataProvider implements DataSource {

    private IPreferencesSource preferencesSource;
    private IFirestoreSource firestoreManager;

    private static DataProvider instance;

    public static DataProvider getInstance(IPreferencesSource preferencesSource, IFirestoreSource firestoreManager) {
        if (instance == null)
            instance = new DataProvider(preferencesSource, firestoreManager);
        return instance;
    }

    private DataProvider(IPreferencesSource preferencesSource, IFirestoreSource firestoreManager) {
        this.preferencesSource = preferencesSource;
        this.firestoreManager = firestoreManager;
    }

    @Override
    public void saveLoginData(Auth auth, DataCallback<Boolean, BaseError> callback) {
        try {
            callback.onSuccess(preferencesSource.saveLoginData(auth));
        } catch (Exception e) {
            callback.onFailure(BaseError.getStandardError());
        }
    }

    @Override
    public void getLoginData(DataCallback<Auth, BaseError> callback) {
        try {
            callback.onSuccess(preferencesSource.getLoginData());
        } catch (Exception e) {
            callback.onFailure(BaseError.getStandardError());
        }
    }

    @Override
    public void getAuth(AuthRequest request, DataCallback<Boolean, BaseError> callback) {
        firestoreManager.getAuth(request.getRoleID(), document -> callback.onSuccess(request.getEncryptPass().equals(document)));
    }
}
