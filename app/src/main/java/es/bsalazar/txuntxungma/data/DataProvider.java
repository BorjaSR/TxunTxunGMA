package es.bsalazar.txuntxungma.data;

import java.util.List;

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
        try {
            callback.onSuccess(preferencesSource.removeLoginData());
        } catch (Exception e) {
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
        firestoreManager.getEvents(new FirestoreSource.OnCollectionChangedListener<Event>() {
            @Override
            public void onCollectionChange(List<Event> collection) {
                for(Event event : collection)
                    event.setAlarmActivated(getAlarmId(event.getId()) != -1);

                callback.onCollectionChange(collection);
            }

            @Override
            public void onDocumentAdded(int index, Event document) {
                callback.onDocumentAdded(index, document);
            }

            @Override
            public void onDocumentChanged(int index, Event document) {
                callback.onDocumentChanged(index, document);

            }

            @Override
            public void onDocumentRemoved(int index, Event document) {
                callback.onDocumentRemoved(index, document);
            }
        });
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

    @Override
    public Integer saveAlarmId(String objectId) {
        int id = getAndIncrementAlarmId();
        preferencesSource.saveAlarmId(objectId, id);
        return id;
    }

    @Override
    public Integer getAlarmId(String objectId) {
        return preferencesSource.getAlarmId(objectId);
    }

    @Override
    public boolean removeAlarmId(String objectId) {
        return preferencesSource.removeAlarmId(objectId);
    }

    @Override
    public Integer getAndIncrementAlarmId() {
        int id = preferencesSource.getActualAlarmId();
        preferencesSource.incrementAlarmId(id);
        return id;
    }

    @Override
    public void getEvent(String eventId, DataCallback<Event, BaseError> callback) {
        firestoreManager.getEvent(eventId, callback::onSuccess);
    }
}
