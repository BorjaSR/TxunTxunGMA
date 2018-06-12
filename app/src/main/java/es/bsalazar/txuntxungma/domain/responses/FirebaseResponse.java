package es.bsalazar.txuntxungma.domain.responses;

public class FirebaseResponse<T> {
    private int index;
    private T response;

    public FirebaseResponse() {
    }

    public FirebaseResponse(int index) {
        this.index = index;
    }

    public FirebaseResponse(int index, T response) {
        this.index = index;
        this.response = response;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
