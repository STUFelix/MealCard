package com.example.kaixuan.mealcard;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RequestDetail {
    private String username;
    private String password;
    private String cookie;
    private String days;
    private Handler mHandle;

    private List<Map<String,String>> DList = new ArrayList<Map<String,String>>();

    RequestDetail(){

    }

    RequestDetail(String cookie,String username,String password,String days,Handler handler){
        this.cookie = cookie;
        this.username = username;
        this.password = password;
        this.days = days;
        this.mHandle =handler;
    }

    public  void getDetail(){
        RequestUrl requestUrl =new RetrofitCreate().getRetrofit();
        requestUrl.getDetail(cookie,username,password,days)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TheBean>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(TheBean theBean) {
                        Log.i("RequestDetail","String\t"+theBean.getLength());
                        Log.i("RequestDetail","String\t"+theBean.getDetail());
                        toParseBean(theBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i("RequestDetail",throwable.toString());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("RequestDetail","finish");
                        Message message = Message.obtain();
                        message.obj = DList;
                        message.what = 20001;
                        mHandle.sendMessage(message);
                    }
                });

    }

    public void toParseBean(TheBean theBean){

        for (int i=0;i<theBean.getLength();i++){
            Map<String,String> map =new HashMap<>();
            String date = theBean.getDetail().get(i).getDate();
            String time = theBean.getDetail().get(i).getTime();
            String flow = theBean.getDetail().get(i).getFlow();
            String kind = theBean.getDetail().get(i).getKind();
            String name = theBean.getDetail().get(i).getName();

            map.put("date",date);
            map.put("time",time);
            map.put("flow",flow);
            map.put("kind",kind);
            map.put("name",name);
            DList.add(map);
        }
        Log.i("RequestDetail", "toParseBean: parseBean"+DList);
    }
}

