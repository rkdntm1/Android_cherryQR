package net.cherry.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
    SharedPreferences sharedPref; // 데이터를 저장하기위해 SharedPreferences 호출
    SharedPreferences.Editor sharedEditor; //에디터
    Context e; // 담을 데이터 덩어리

    public SharedPreferenceUtils(Context e) {
        //
        sharedPref = e.getSharedPreferences("e4net_pref", Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();
        this.e = e;
    }

    public void saveString(int keyID, String stringValue){
        sharedEditor.putString(e.getResources().getString(keyID), stringValue);
        sharedEditor.apply(); //함수 실행시 수정된 모든 사항들이 커밋되고 푸쉬가 된다.
    }

    public void saveBool(int keyID, boolean value){
        sharedEditor.putBoolean(e.getResources().getString(keyID), value);
        sharedEditor.apply(); //함수 실행시 수정된 모든 사항들이 커밋되고 푸쉬가 된다.
    }

    public String getString(int keyID, String defaultStringVal) {
        return sharedPref.getString(e.getResources().getString(keyID), defaultStringVal);
    }

    public boolean getBool(int keyID, Boolean defaultVal){
        return sharedPref.getBoolean(e.getResources().getString(keyID), defaultVal);
    }
}
