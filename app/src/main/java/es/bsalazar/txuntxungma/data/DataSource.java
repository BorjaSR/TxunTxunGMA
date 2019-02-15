package es.bsalazar.txuntxungma.data;

import es.bsalazar.txuntxungma.data.remote.FirestoreSource;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.entities.Rate;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;

public interface DataSource {

    void saveLoginData(Auth auth, DataCallback<Boolean, BaseError> callback);

    void getLoginData(DataCallback<Auth, BaseError> callback);

    void removeLoginData(DataCallback<Boolean, BaseError> callback);

    void getAuth(AuthRequest request, DataCallback<String, BaseError> callback);

    void getComponents(FirestoreSource.OnCollectionChangedListener<Component> callback);

    void saveComponent(Component component, FirestoreSource.OnDocumentSavedListener<Component> callback);

    void updateComponent(Component component, FirestoreSource.OnDocumentSavedListener<Component> callback);

    void deleteComponent(String componentId);

    void getRates(FirestoreSource.OnCollectionChangedListener<Rate> callback);

    void saveRate(Rate rate, FirestoreSource.OnDocumentSavedListener<Rate> callback);

    void updateRate(Rate rate, FirestoreSource.OnDocumentSavedListener<Rate> callback);

    void deleteRate(String rateId);
}
