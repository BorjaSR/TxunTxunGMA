package es.bsalazar.txuntxungma.data.remote;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirestoreSource implements IFirestoreSource {

    private final String TAG = "Firestore Manager";

    public static final String ROLES_COLLECTION = "roles";

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
