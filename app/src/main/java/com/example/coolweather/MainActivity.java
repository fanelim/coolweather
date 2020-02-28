package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.Base;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainActivity extends AppCompatActivity {

    public String TAG = "MainActivity";
    TextView template;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HeConfig.init("你当时注册是所用的名字", "你注册时所用到的密匙");
        HeConfig.switchToFreeServerNode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fun("温宿");//你所在的城市！
    }

    //解析方法

    public void  Fun(String Id){
        HeWeather.getWeatherNow(MainActivity.this, Id, Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                Log.i(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    Basic base = dataObject.getBasic();
                    Log.i(TAG,"当前地区："+base.getCnty()+"——"+base.getAdmin_area()+"——"+base.getParent_city()+"——"+base.getLocation());
                    Log.i(TAG, " 当前温度为：: " + now.getTmp());
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }
}
