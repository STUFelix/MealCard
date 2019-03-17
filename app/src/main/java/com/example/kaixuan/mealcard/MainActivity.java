package com.example.kaixuan.mealcard;

import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener{
    private static final String TAG = "test_MainActivity";

    private ListView flow_listView;
    private String days ="3";//默认值为"3"
    private RadioGroup time_rg;
    private RadioButton button1,button2,button3,button4;

    private String mCookie;
    private String mBalance;

    private RequestBalance requestBalance;
    private RequestDetail requestDetail;
    private TextView tv_balance,tv_today_consumer,tv_consume_details_icon,tv_count_cost;
    private List<Map<String,String>> list_map = new ArrayList<Map<String,String>>();

    private String[] key ={"date","time","flow","name"};
    private int[] item ={R.id.mealcard_item_date,R.id.mealcard_item_time,R.id.mealcard_item_flow,R.id.mealcard_item_name};

    private double count_cost;
    private double count_cost_temp;
    private double[] count_today_cost = new double[10];
    private double count_today_cost_amount;
    private String count_today_string;
    private String date;

    private String username ="17kxxiao";
    private String password= "ZQxMH51";



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
         super.handleMessage(msg);
         switch (msg.what){
             case 10001:
                 String[] temp = msg.obj.toString().split("#");
                 mBalance = temp[0]; mCookie =temp[1];
                 Log.i(TAG, "handleMessage: "+mBalance+mCookie);
                 tv_balance.setText(mBalance);
                 tv_balance.setTextSize(60);

                 RequestDetail requestDetail =new RequestDetail(mCookie,username,password,days,handler);
                 requestDetail.getDetail();
                 break;
             case 20001:
                 count_cost = 0; count_cost_temp=0;count_today_cost_amount=0;count_today_string="";
                 list_map =(List<Map<String,String>>) msg.obj;
                 Iterator<Map<String,String>> iterable =  list_map.iterator();
                 int today_i = 0;
                 while (iterable.hasNext()){
                     Map<String,String> iterable_map =iterable.next();
                     count_cost_temp = Double.parseDouble(iterable_map.get("flow"));
                     if( count_cost_temp < 0) {count_cost +=count_cost_temp;}

                     if(count_cost_temp < 0 && date.equals(iterable_map.get("date"))){
                         count_today_cost[today_i] =Math.abs(count_cost_temp);
                         today_i++;
                     }
                 }

                 for (int i=today_i-1; i>=0; i--){
                     count_today_cost_amount = count_today_cost_amount+ count_today_cost[i];
                     if(i==today_i-1){
                         count_today_string = ""+count_today_cost[i];
                         Log.i(TAG, "handleMessage: count_today_cost"+count_today_cost[i]);
                         continue;
                     }
                     count_today_string = count_today_string+"+"+count_today_cost[i];
                     Log.i(TAG, "handleMessage: count_today_cost"+count_today_cost[i]);
                 }


                 tv_today_consumer.setText("今日消费："+count_today_string+"="+String.format("%.1f",count_today_cost_amount)+"元");
                 tv_today_consumer.setTextSize(15);
                 tv_count_cost.setText("支出总计：\t"+String.format("%.1f",count_cost)+"  元");
                 tv_count_cost.setTextSize(20);
                 SimpleAdapter simpleAdapter =new SimpleAdapter(MainActivity.this,list_map,R.layout.mealcard_list_item,key,item);
                 flow_listView.setAdapter(simpleAdapter);
                 break;
                 default:
                     break;
         }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealcard_main);
        init();

        requestBalance = new RequestBalance(username,password,handler);
        requestBalance.getBalance();

    }


    private void  init(){
        flow_listView = (ListView) findViewById(R.id.card_listView);

        tv_balance = (TextView) findViewById(R.id.mealcard_TV_balance);
        Drawable drawable_balance = getResources().getDrawable(R.drawable.mealcard_wallet);
        drawable_balance.setBounds(0,0,120,120);
        tv_balance.setCompoundDrawables(drawable_balance,null,null,null);

        tv_today_consumer =(TextView) findViewById(R.id.mealcard_TV_todayConsume);


        tv_consume_details_icon = (TextView)findViewById(R.id.mealcard_TV_consumeDetails_icon);
        Drawable drawable_consume_details =getResources().getDrawable(R.drawable.mealcard_time);
        drawable_consume_details.setBounds(0,0,35,35);
        tv_consume_details_icon.setCompoundDrawables(null,null,drawable_consume_details,null);

        tv_count_cost =(TextView) findViewById(R.id.mealcard_TV_countCosts) ;

        time_rg = (RadioGroup) findViewById(R.id.mealcard_rg);
        button1 = (RadioButton) findViewById(R.id.mealcard_rb_1);
        button2 = (RadioButton) findViewById(R.id.mealcard_rb_2);
        button3 = (RadioButton) findViewById(R.id.mealcard_rb_3);
        button4 = (RadioButton) findViewById(R.id.mealcard_rb_4);
        time_rg.setOnCheckedChangeListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
        Date date_temp =new Date(System.currentTimeMillis());
        date = simpleDateFormat.format(date_temp);
        Log.i(TAG, "init: date"+date);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        button1.setChecked(false);
        button2.setChecked(false);
        button3.setChecked(false);
        button4.setChecked(false);
        switch (checkedId){
            case R.id.mealcard_rb_1:
                button1.setChecked(true);
                days = "3";
                requestDetail = new RequestDetail(mCookie,username,password,days,handler);
                requestDetail.getDetail();
                break;
            case R.id.mealcard_rb_2:
                button2.setChecked(true);
                days = "7";
                requestDetail = new RequestDetail(mCookie,username,password,days,handler);
                requestDetail.getDetail();
                break;
            case R.id.mealcard_rb_3:
                button3.setChecked(true);
                days = "31";
                requestDetail = new RequestDetail(mCookie,username,password,days,handler);
                requestDetail.getDetail();
                break;
            case R.id.mealcard_rb_4:
                button4.setChecked(true);
                days = "90";
                requestDetail = new RequestDetail(mCookie,username,password,days,handler);
                requestDetail.getDetail();
                break;
            default:
                break;
        }
    }
}
