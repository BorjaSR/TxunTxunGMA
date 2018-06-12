package es.bsalazar.txuntxungma.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import es.bsalazar.txuntxungma.domain.entities.Auth;

public class PreferencesSource implements IPreferencesSource {

    private final String LOGIN_DATA_KEY = "LOGIN_DATA_KEY";

    private static PreferencesSource instance;
    private SharedPreferences sharedPreferences;

    public static PreferencesSource getInstance(Context context){
        if (instance == null)
            instance = new PreferencesSource(context);
        return instance;
    }

    private PreferencesSource(Context context) {
        sharedPreferences = context.getSharedPreferences("MY_PREFERENCE_KEY", Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveLoginData(Auth auth){
        return sharedPreferences.edit()
                .putString(LOGIN_DATA_KEY, auth.toString())
                .commit();
    }

    @Override
    public Auth getLoginData(){
        return new Auth(sharedPreferences.getString(LOGIN_DATA_KEY, ""));
    }

}
