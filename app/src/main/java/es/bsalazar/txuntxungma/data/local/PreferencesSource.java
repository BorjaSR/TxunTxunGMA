package es.bsalazar.txuntxungma.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import es.bsalazar.txuntxungma.domain.entities.Auth;

public class PreferencesSource implements IPreferencesSource {

    private final String LOGIN_DATA_KEY = "LOGIN_DATA_KEY";
    private final String ALARMS_KEY = "ALARM_KEY_";
    private final String ALARM_ID_KEY = "ALARM_ID_KEY";

    private static PreferencesSource instance;
    private SharedPreferences sharedPreferences;

    public static PreferencesSource getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesSource(context);
        return instance;
    }

    private PreferencesSource(Context context) {
        sharedPreferences = context.getSharedPreferences("MY_PREFERENCE_KEY", Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveLoginData(Auth auth) {
        return sharedPreferences.edit()
                .putString(LOGIN_DATA_KEY, auth.toString())
                .commit();
    }

    @Override
    public Auth getLoginData() {
        return new Auth(sharedPreferences.getString(LOGIN_DATA_KEY, ""));
    }

    @Override
    public boolean removeLoginData() {
        return sharedPreferences.edit().remove(LOGIN_DATA_KEY).commit();
    }

    @Override
    public void saveAlarmId(String objectId, Integer alarmId) {
        sharedPreferences.edit()
                .putInt(ALARMS_KEY.concat(objectId), alarmId)
                .apply();
    }

    @Override
    public Integer getAlarmId(String objectId) {
        return sharedPreferences.getInt(ALARMS_KEY.concat(objectId), -1);
    }

    @Override
    public boolean removeAlarmId(String objectId) {
        return sharedPreferences.edit().remove(ALARMS_KEY.concat(objectId)).commit();
    }

    @Override
    public Integer getActualAlarmId() {
        return sharedPreferences.getInt(ALARM_ID_KEY, 0);
    }

    @Override
    public void incrementAlarmId(Integer actualAlarmID) {
        if (actualAlarmID == 1000)
            sharedPreferences.edit()
                    .putInt(ALARM_ID_KEY, 0)
                    .apply();
        else
            sharedPreferences.edit()
                    .putInt(ALARM_ID_KEY, actualAlarmID + 1)
                    .apply();
    }
}
