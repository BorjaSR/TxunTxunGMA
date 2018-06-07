package es.bsalazar.txuntxungma.data;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by borja.salazar on 16/03/2018.
 */

public class FirestoreManager {

    private final String TAG = "Firestore Manager";

    private FirebaseFirestore db;
    private static FirestoreManager instance;


    //region Constructor
    public static FirestoreManager getInstance() {
        if (instance == null)
            instance = new FirestoreManager();
        return instance;
    }

    private FirestoreManager() {
        db = FirebaseFirestore.getInstance();
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
