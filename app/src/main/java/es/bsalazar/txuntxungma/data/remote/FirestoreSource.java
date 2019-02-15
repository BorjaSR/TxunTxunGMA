package es.bsalazar.txuntxungma.data.remote;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.Rate;

public class FirestoreSource implements IFirestoreSource {

    private final String TAG = "Firestore Manager";

    private static final String ROLES_COLLECTION = "roles";
    private static final String COMPONENTS_COLLECTION = "components";
    private static final String RATES_COLLECTION = "rates";

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

        Map<String, Object> componentMap = rate.getMap();

        db.collection(RATES_COLLECTION)
                .add(componentMap)
                .addOnSuccessListener(documentReference -> {
                    rate.setId(documentReference.getId());
                    listener.onDocumentSaved(rate);
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

    //endregion

    //region Interfaces
    public interface OnCollectionChangedListener<T> {
        void onCollectionChange(List<T> collection);
        void onDocumentAdded(int index, T object);
        void onDocumentChanged(int index, T object);
        void onDocumentRemoved(int index, T object);
    }

    public interface OnDocumentLoadedListener<T> {
        void onDocumentLoaded(T document);
    }

    public interface OnDocumentSavedListener<T> {
        void onDocumentSaved(T document);
    }
    //endregion
}
