package es.bsalazar.txuntxungma.data;

import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.BaseError;
import es.bsalazar.txuntxungma.domain.entities.DataCallback;
import es.bsalazar.txuntxungma.domain.requests.AuthRequest;

public interface DataSource {

    void saveLoginData(Auth auth, DataCallback<Boolean, BaseError> callback);

    void getLoginData(DataCallback<Auth, BaseError> callback);

    void getAuth(AuthRequest request, DataCallback<Boolean, BaseError> callback);
}
