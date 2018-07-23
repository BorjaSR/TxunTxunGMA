package es.bsalazar.txuntxungma.data.local;

import es.bsalazar.txuntxungma.domain.entities.Auth;

public interface IPreferencesSource {

    boolean saveLoginData(Auth auth);

    Auth getLoginData();

    boolean removeLoginData();
}
