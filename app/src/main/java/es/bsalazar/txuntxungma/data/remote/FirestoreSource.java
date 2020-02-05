package es.bsalazar.txuntxungma.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.Event;
import es.bsalazar.txuntxungma.domain.entities.Rate;
import es.bsalazar.txuntxungma.domain.entities.Release;
import es.bsalazar.txuntxungma.domain.entities.ReleaseComponentsList;

public class FirestoreSource implements IFirestoreSource {

    private final String TAG = "Firestore Manager";

    private static final String ROLES_COLLECTION = "roles";
    private static final String COMPONENTS_COLLECTION = "components";
    private static final String RATES_COLLECTION = "rates";
    private static final String EVENTS_COLLECTION = "events";
    private static final String RELEASES_COLLECTION = "releases";
    private static final String RELEASE_COMPONENTS_LIST_COLLECTION = "release_components_list";

    private FirebaseFirestore db;
    private static FirestoreSource instance;


    //region Constructor
    public static FirestoreSource getInstance() {
        if (instance == null)
            instance = new FirestoreSource();
        return instance;
    }

    private FirestoreSource() {
        db = FirebaseFirestore.getInstance();
    }
    //endregion

    //region GETTERS
    @Override
    public void getAuth(String roleID, final OnDocumentLoadedListener<String> callback) {
        db.collection(ROLES_COLLECTION)
                .document(roleID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            callback.onDocumentLoaded(String.valueOf(document.get("access")));
                        } else {
                            callback.onDocumentLoaded(null);
                        }
                    } else {
                        callback.onDocumentLoaded(null);
                    }
                });
    }

    @Override
    public void getComponents(OnCollectionChangedListener<Component> listener) {
        db.collection(COMPONENTS_COLLECTION)
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listener.onDocumentAdded(documentChange.getNewIndex(), new Component(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                                listener.onDocumentChanged(documentChange.getNewIndex(), new Component(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                                listener.onDocumentRemoved(documentChange.getOldIndex(), new Component(documentChange.getDocument().getId(), documentChange.getDocument()));
                            }
                        }

                        final ArrayList<Component> categories = new ArrayList<>();
                        for (DocumentSnapshot doc : value)
                            categories.add(new Component(doc.getId(), doc));
                        listener.onCollectionChange(categories);
                    }
                });
    }

    @Override
    public void getRates(OnCollectionChangedListener<Rate> listener) {
        db.collection(RATES_COLLECTION)
                .orderBy("description", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                listener.onDocumentAdded(documentChange.getNewIndex(), new Rate(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                                listener.onDocumentChanged(documentChange.getNewIndex(), new Rate(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                                listener.onDocumentRemoved(documentChange.getOldIndex(), new Rate(documentChange.getDocument().getId(), documentChange.getDocument()));
                            }
                        }

                        final ArrayList<Rate> rates = new ArrayList<>();
                        for (DocumentSnapshot doc : value)
                            rates.add(new Rate(doc.getId(), doc));
                        listener.onCollectionChange(rates);
                    }
                });
    }

    @Override
    public void getEvents(OnCollectionChangedListener<Event> callback) {
//        final long[] lastModify = {0};
        db.collection(EVENTS_COLLECTION)
                .whereGreaterThan("date", System.currentTimeMillis())
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }


                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                callback.onDocumentAdded(documentChange.getNewIndex(), new Event(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
//                                if (System.currentTimeMillis() - lastModify[0] > 1000)
                                    callback.onDocumentChanged(documentChange.getOldIndex(), new Event(documentChange.getDocument().getId(), documentChange.getDocument()));
//                                lastModify[0] = System.currentTimeMillis();

                            } else if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                                callback.onDocumentRemoved(documentChange.getOldIndex(), new Event(documentChange.getDocument().getId(), documentChange.getDocument()));
                            }
                        }

                        final ArrayList<Event> events = new ArrayList<>();
                        for (DocumentSnapshot doc : value)
                            events.add(new Event(doc.getId(), doc));
                        callback.onCollectionChange(events);
                    }
                });
    }

    @Override
    public void getReleases(OnCollectionChangedListener<Release> callback) {
        db.collection(RELEASES_COLLECTION)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                callback.onDocumentAdded(documentChange.getNewIndex(), new Release(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                                callback.onDocumentChanged(documentChange.getNewIndex(), new Release(documentChange.getDocument().getId(), documentChange.getDocument()));

                            } else if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                                callback.onDocumentRemoved(documentChange.getOldIndex(), new Release(documentChange.getDocument().getId(), documentChange.getDocument()));
                            }
                        }

                        final ArrayList<Release> releases = new ArrayList<>();
                        for (DocumentSnapshot doc : value)
                            releases.add(new Release(doc.getId(), doc));
                        callback.onCollectionChange(releases);
                    }
                });
    }

    @Override
    public void getEvent(String eventId, OnDocumentLoadedListener<Event> callback) {
        db.collection(EVENTS_COLLECTION)
                .document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            callback.onDocumentLoaded(new Event(eventId, document));
                        } else {
                            callback.onDocumentLoaded(null);
                        }
                    } else {
                        callback.onDocumentLoaded(null);
                    }
                });
    }

    //endregion

    //region SAVES
    @Override
    public void saveComponent(final Component component, final OnDocumentSavedListener<Component> listener) {
        Map<String, Object> componentMap = component.getMap();

        db.collection(COMPONENTS_COLLECTION)
                .add(componentMap)
                .addOnSuccessListener(documentReference -> {
                    component.setId(documentReference.getId());
                    listener.onDocumentSaved(component);
                })
                .addOnFailureListener(e -> {
                    listener.onDocumentSaved(null);
                    Log.w(TAG, "Error writing document", e);
                });
    }

    @Override
    public void saveRate(Rate rate, OnDocumentSavedListener<Rate> listener) {
        Map<String, Object> rateMap = rate.getMap();

        db.collection(RATES_COLLECTION)
                .add(rateMap)
                .addOnSuccessListener(documentReference -> {
                    rate.setId(documentReference.getId());
                    listener.onDocumentSaved(rate);
                })
                .addOnFailureListener(e -> {
                    listener.onDocumentSaved(null);
                    Log.w(TAG, "Error writing document", e);
                });
    }

    @Override
    public void saveEvent(Event event, OnDocumentSavedListener<Event> listener) {
        Map<String, Object> eventMap = event.getMap();

        db.collection(EVENTS_COLLECTION)
                .add(eventMap)
                .addOnSuccessListener(documentReference -> {
                    event.setId(documentReference.getId());
                    listener.onDocumentSaved(event);
                })
                .addOnFailureListener(e -> {
                    listener.onDocumentSaved(null);
                    Log.w(TAG, "Error writing document", e);
                });
    }

    @Override
    public void saveRelease(Release release, OnDocumentSavedListener<Release> listener) {
        Map<String, Object> releaseMap = release.getMap();

        db.collection(RELEASES_COLLECTION)
                .add(releaseMap)
                .addOnSuccessListener(documentReference -> {
                    release.setId(documentReference.getId());
                    listener.onDocumentSaved(release);
                })
                .addOnFailureListener(e -> {
                    listener.onDocumentSaved(null);
                    Log.w(TAG, "Error writing document", e);
                });
    }
    //endregion

    //region UPDATE
    @Override
    public void updateComponent(Component component, OnDocumentSavedListener<Component> listener) {
        Map<String, Object> componentMap = component.getMap();

        db.collection(COMPONENTS_COLLECTION).document(component.getId())
                .set(componentMap)
                .addOnSuccessListener(aVoid -> listener.onDocumentSaved(new Component()))
                .addOnFailureListener(e -> listener.onDocumentSaved(null));
    }

    @Override
    public void updateRate(Rate rate, OnDocumentSavedListener<Rate> listener) {
        Map<String, Object> componentMap = rate.getMap();

        db.collection(RATES_COLLECTION).document(rate.getId())
                .set(componentMap)
                .addOnSuccessListener(aVoid -> listener.onDocumentSaved(new Rate()))
                .addOnFailureListener(e -> listener.onDocumentSaved(null));
    }

    @Override
    public void updateEvent(Event event, OnDocumentSavedListener<Event> listener) {
        Map<String, Object> eventMap = event.getMap();

        db.collection(EVENTS_COLLECTION).document(Objects.requireNonNull(event.getId()))
                .set(eventMap)
                .addOnSuccessListener(aVoid -> listener.onDocumentSaved(event))
                .addOnFailureListener(e -> listener.onDocumentSaved(null));
    }

    @Override
    public void updateRelease(Release release, OnDocumentSavedListener<Release> listener) {
        Map<String, Object> componentMap = release.getMap();

        db.collection(RELEASES_COLLECTION).document(Objects.requireNonNull(release.getId()))
                .set(componentMap)
                .addOnSuccessListener(aVoid -> listener.onDocumentSaved(release))
                .addOnFailureListener(e -> listener.onDocumentSaved(null));
    }
    //endregion

    //region DELETE

    @Override
    public void deleteComponent(String componentId) {
        db.collection(COMPONENTS_COLLECTION).document(componentId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    public void deleteRate(String rateId) {
        db.collection(RATES_COLLECTION).document(rateId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    public void deleteEvent(String eventId) {
        db.collection(EVENTS_COLLECTION).document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    public void deleteRelease(String releaseId) {
        db.collection(RELEASES_COLLECTION).document(releaseId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    //endregion

    //region Interfaces
    public interface OnCollectionChangedListener<T> {
        void onCollectionChange(List<T> collection);

        void onDocumentAdded(int index, T document);

        void onDocumentChanged(int index, T document);

        void onDocumentRemoved(int index, T document);
    }

    public interface OnDocumentLoadedListener<T> {
        void onDocumentLoaded(T document);
    }

    public interface OnDocumentSavedListener<T> {
        void onDocumentSaved(T document);
    }
    //endregion
}
