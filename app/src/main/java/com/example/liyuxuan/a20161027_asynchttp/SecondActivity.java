package com.example.liyuxuan.a20161027_asynchttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {

    private TextView et;
    private String img1;
    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        et = (TextView) findViewById(R.id.textView);
        im = (ImageView)findViewById(R.id.imgview);

        AsyncHttpClient ac = new AsyncHttpClient();
        //String url = "http://v.juhe.cn/xiangji_weather/real_time_weather.php?areaid=101010100&key=09efacabea3b6da5643e237c03716b97";
        String url = "http://apis.juhe.cn/cook/query.php";
        RequestParams params = new RequestParams();
        params.add("key","90e8a667333aa3c83bbfdcabbd0fa620");
        params.add("menu","红烧肉");
        params.add("rn","10");
        params.add("pu","3");
        ac.get(getApplicationContext(),url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(response.toString());
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray data = result.getJSONArray("data");
                    /*String output = data.getString("title")+"\n"
                            +data.getString("tags");//+"\n"+data.getInt("rh")+"\n"+data.getString("wd")*/
                    JSONObject str1 = data.getJSONObject(0);
                    String  output = str1.getString("title")+"\n"
                            +str1.getString("tags");//+"\n"+data.getInt("rh")+"\n"+data.getString("wd")
                    et.setText(output);
                    Toast.makeText(getApplicationContext(),output,Toast.LENGTH_SHORT).show();
                    JSONArray arr1 = str1.getJSONArray("steps");
                    JSONObject step1 = arr1.getJSONObject(0);
                    img1 = step1.getString("img");
                    Toast.makeText(getApplicationContext(),img1,Toast.LENGTH_SHORT).show();
                    //图片
                    AsyncHttpClient ac2 = new AsyncHttpClient();
                    ac2.get(img1, new AsyncHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(statusCode==200){
                                BitmapFactory factory=new BitmapFactory();
                                Bitmap bitmap=factory.decodeByteArray(responseBody, 0, responseBody.length);
                                im.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                        }
                    });
                    //最后一条项目
                    JSONObject str2 = data.getJSONObject(data.length()-1);
                    output = output+str2.getString("title")+"\n"
                            +str2.getString("tags");
                    et.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"出事儿了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}