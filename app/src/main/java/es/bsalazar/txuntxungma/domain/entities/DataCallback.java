package es.bsalazar.txuntxungma.domain.entities;

public interface DataCallback<R, E> {
    void onSuccess(R response);
    void onFailure(E errorBase);
}
