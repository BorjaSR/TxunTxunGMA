package es.bsalazar.txuntxungma.data.local;

import java.util.ArrayList;

import es.bsalazar.txuntxungma.domain.entities.Auth;

public interface IPreferencesSource {

    boolean saveLoginData(Auth auth);

    Auth getLoginData();

    boolean removeLoginData();

    boolean saveReleasesSigned(ArrayList<String> releases);

    ArrayList<String> getReleasesSigned();

    void saveAlarmId(String objectId, Integer alarmId);

    Integer getAlarmId(String objectId);

    boolean removeAlarmId(String objectId);

    Integer getActualAlarmId();

    void incrementAlarmId(Integer actualAlarmID);
}
