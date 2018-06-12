package es.bsalazar.txuntxungma.data.remote;

public interface IFirestoreSource {

    void getAuth(String roleID, final FirestoreSource.OnDocumentLoadedListener<String> callback);
}
