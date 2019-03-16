package com.example.kaixuan.mealcard;

import android.os.Handler;
import android.os.Message;
import android.util.Log;



import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class RequestBalance {

    private String username;
    private String password;

    private String mCookie;
    private String mBalance;
    private String str;

    private Handler mHandler;

    RequestBalance(String username,String password,Handler mHandler){
        this.username = username;
        this.password = password;
        this.mHandler =mHandler;
    }

    public  void getBalance(){

        RequestUrl requestUrl =new RetrofitCreate().getRetrofit();

        requestUrl.getBalance(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<YKTBean>>() {
                            @Override
                            public void onSubscribe(Disposable disposable) {

                            }

                            @Override
                            public void onNext(Response<YKTBean> balanceBeanResponse) {
                                mCookie = balanceBeanResponse.headers().get("Cookie");
                                mBalance = balanceBeanResponse.body().getBalance();
                                str = mBalance +"#"+mCookie;
                                Log.i("RequestBalance", "onNext: "+str);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Log.i("RequestBalance","error :"+throwable);
                            }

                            @Override
                            public void onComplete() {
                                Message message =Message.obtain();
                                message.obj =str;
                                message.what =10001;
                                mHandler.sendMessage(message);
                                Log.i("RequestBalance","finish");
                            }
                });
    }

}
