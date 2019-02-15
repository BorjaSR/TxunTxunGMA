package es.bsalazar.txuntxungma.data.remote;

import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.Rate;

public interface IFirestoreSource {

    void getAuth(String roleID, final FirestoreSource.OnDocumentLoadedListener<String> callback);

    void getComponents(final FirestoreSource.OnCollectionChangedListener<Component> callback);

    void saveComponent(final Component component, final FirestoreSource.OnDocumentSavedListener<Component> listener);

    void updateComponent(final Component component, final FirestoreSource.OnDocumentSavedListener<Component> listener);

    void deleteComponent(String componentId);

    void getRates(final FirestoreSource.OnCollectionChangedListener<Rate> callback);

    void saveRate(final Rate rate, final FirestoreSource.OnDocumentSavedListener<Rate> listener);

    void updateRate(final Rate rate, final FirestoreSource.OnDocumentSavedListener<Rate> listener);

    void deleteRate(String rateId);
}
