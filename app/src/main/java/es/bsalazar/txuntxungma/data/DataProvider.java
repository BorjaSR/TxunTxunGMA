package es.bsalazar.txuntxungma.data;

import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.data.local.IPreferencesSource;
import es.bsalazar.txuntxungma.data.remote.IFirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Event;
import es.bsalazar.txuntxungma.domain.entities.Rate;
import es.bsalazar.txuntxungma.domain.entities.Release;
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
    public void removeLoginData(DataCallback<Boolean, BaseError> callback) {
        try{
            callback.onSuccess(preferencesSource.removeLoginData());
        }catch (Exception e){
            callback.onFailure(BaseError.getStandardError());
        }
    }

    @Override
    public void getAuth(AuthRequest request, DataCallback<String, BaseError> callback) {
        firestoreManager.getAuth(request.getRoleID(), callback::onSuccess);
    }

    @Override
    public void getComponents(FirestoreSource.OnCollectionChangedListener<Component> callback) {
        firestoreManager.getComponents(callback);
    }

    @Override
    public void saveComponent(Component component, FirestoreSource.OnDocumentSavedListener<Component> callback) {
        firestoreManager.saveComponent(component, callback);
    }

    @Override
    public void updateComponent(Component component, FirestoreSource.OnDocumentSavedListener<Component> callback) {
        firestoreManager.updateComponent(component, callback);
    }

    @Override
    public void deleteComponent(String componentId) {
        firestoreManager.deleteComponent(componentId);
    }

    @Override
    public void getRates(FirestoreSource.OnCollectionChangedListener<Rate> callback) {
        firestoreManager.getRates(callback);
    }

    @Override
    public void saveRate(Rate rate, FirestoreSource.OnDocumentSavedListener<Rate> callback) {
        firestoreManager.saveRate(rate, callback);
    }

    @Override
    public void updateRate(Rate rate, FirestoreSource.OnDocumentSavedListener<Rate> callback) {
        firestoreManager.updateRate(rate, callback);
    }

    @Override
    public void deleteRate(String rateId) {
        firestoreManager.deleteRate(rateId);
    }

    @Override
    public void getEvents(FirestoreSource.OnCollectionChangedListener<Event> callback) {
        firestoreManager.getEvents(callback);
    }

    @Override
    public void saveEvent(Event event, FirestoreSource.OnDocumentSavedListener<Event> callback) {
        firestoreManager.saveEvent(event, callback);
    }

    @Override
    public void updateEvent(Event event, FirestoreSource.OnDocumentSavedListener<Event> callback) {
        firestoreManager.updateEvent(event, callback);
    }

    @Override
    public void deleteEvent(String eventId) {
        firestoreManager.deleteEvent(eventId);
    }

    @Override
    public void getReleases(FirestoreSource.OnCollectionChangedListener<Release> callback) {
        firestoreManager.getReleases(callback);
    }

    @Override
    public void saveRelease(Release release, FirestoreSource.OnDocumentSavedListener<Release> callback) {
        firestoreManager.saveRelease(release, callback);
    }

    @Override
    public void updateRelease(Release release, FirestoreSource.OnDocumentSavedListener<Release> callback) {
        firestoreManager.updateRelease(release, callback);
    }

    @Override
    public void deleteRelease(String releaseId) {
        firestoreManager.deleteRelease(releaseId);
    }
}
