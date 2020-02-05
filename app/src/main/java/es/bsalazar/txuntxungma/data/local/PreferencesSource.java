package es.bsalazar.txuntxungma.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.bsalazar.txuntxungma.domain.entities.Auth;

public class PreferencesSource implements IPreferencesSource {

    private static final String LOGIN_DATA_KEY = "LOGIN_DATA_KEY";
    private static final String ALARMS_KEY = "ALARM_KEY_";
    private static final String ALARM_ID_KEY = "ALARM_ID_KEY";
    private static final String RELEASES_SIGNED_KEY = "RELEASES_SIGNED_KEY";

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
    public boolean saveReleasesSigned(ArrayList<String> releases) {
        return sharedPreferences.edit()
                .putString(RELEASES_SIGNED_KEY, new Gson().toJson(releases))
                .commit();
    }

    @Override
    public ArrayList<String> getReleasesSigned() {
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(sharedPreferences.getString(RELEASES_SIGNED_KEY, new Gson().toJson(new ArrayList<String>())), stringListType);
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
