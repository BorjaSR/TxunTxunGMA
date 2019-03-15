package es.bsalazar.txuntxungma.data.remote;

public class FirebaseResponse<T> {
    private int index;
    private T response;

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
