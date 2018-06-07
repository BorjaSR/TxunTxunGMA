package es.bsalazar.txuntxungma.data;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageManager {

    private String JPG = ".jpg";

    private StorageReference reference;
    private static StorageManager instance;

    public static StorageManager getInstance() {
        if (instance == null)
            instance = new StorageManager();
        return instance;
    }

    private StorageManager() {
        // Create a storage reference from our app
        reference = FirebaseStorage.getInstance().getReference();
    }

}
