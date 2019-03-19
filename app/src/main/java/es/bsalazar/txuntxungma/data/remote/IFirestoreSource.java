package es.bsalazar.txuntxungma.data.remote;

import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.domain.entities.Event;
import es.bsalazar.txuntxungma.domain.entities.Rate;
import es.bsalazar.txuntxungma.domain.entities.Release;

public interface IFirestoreSource {

    void getAuth(String roleID, final FirestoreSource.OnDocumentLoadedListener<String> callback);

    void getComponents(final FirestoreSource.OnCollectionChangedListener<Component> callback);

    void getRates(final FirestoreSource.OnCollectionChangedListener<Rate> callback);

    void getEvents(final FirestoreSource.OnCollectionChangedListener<Event> callback);

    void getReleases(final FirestoreSource.OnCollectionChangedListener<Release> callback);

    void saveComponent(final Component component, final FirestoreSource.OnDocumentSavedListener<Component> listener);

    void saveRate(final Rate rate, final FirestoreSource.OnDocumentSavedListener<Rate> listener);

    void saveEvent(final Event event, final FirestoreSource.OnDocumentSavedListener<Event> listener);

    void saveRelease(final Release release, final FirestoreSource.OnDocumentSavedListener<Release> listener);

    void updateComponent(final Component component, final FirestoreSource.OnDocumentSavedListener<Component> listener);

    void updateRate(final Rate rate, final FirestoreSource.OnDocumentSavedListener<Rate> listener);

    void updateEvent(final Event event, final FirestoreSource.OnDocumentSavedListener<Event> listener);

    void updateRelease(final Release release, final FirestoreSource.OnDocumentSavedListener<Release> listener);

    void deleteComponent(String componentId);

    void deleteRate(String rateId);

    void deleteEvent(String eventId);

    void deleteRelease(String releaseId);
}
