package app.zowneo.recycleviewforjson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zowneo.recycleviewforjson.adapter.recy_item_Adapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    public String date;
    public String title;
    public TextView datejson;
    public RecyclerView recyclerView;
    public List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recy);
        okhttpDate();
    }

    private void okhttpDate() {
        Log.i("TAG", "--OK--");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder().url("这里写自己的服务器完整json路径").build();
//                try {
//                    Response sponse = client.newCall(request).execute();
//                    date = sponse.body().string();
                    date = "{\n" +
                            "  \"code\": \"200\",\n" +
                            "  \"tips\": \"ok\",\n" +
                            "  \"message\": \"success\",\n" +
                            "  \"devices\": [\n" +
                            "    {\n" +
                            "      \"deviceid\": 1,\n" +
                            "      \"userid\": 3,\n" +
                            "      \"username\": \"demo\",\n" +
                            "      \"devicename\": \"TPYBoard v202\",\n" +
                            "      \"deviceaddre\": \"5C:CF:7F:0D:85:65\",\n" +
                            "      \"addtime\": 1436879777\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"deviceid\": 2,\n" +
                            "      \"userid\": 3,\n" +
                            "      \"username\": \"demo\",\n" +
                            "      \"devicename\": \"MMP\",\n" +
                            "      \"deviceaddre\": \"a5:a9:a9:ac:a0:a6\",\n" +
                            "      \"addtime\": 1848919392\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
//                    //解析
                    jsonJXDate(date);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }

    private void jsonJXDate(String date) {
        if (date != null) {
            try {
                JSONObject jsonObject = new JSONObject(date);
                String resultCode = jsonObject.getString("message");
                if (resultCode.equals("success")) {
                    JSONArray resultJsonArray = jsonObject.getJSONArray("devices");
                    for (int i = 0; i < resultJsonArray.length(); i++) {
                        jsonObject = resultJsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();

                        //获取到json数据中的activity数组里的内容name
                        String name = jsonObject.getString("devicename");
                        //获取到json数据中的activity数组里的内容startTime
                        String shijian = jsonObject.getString("deviceaddre");
                        //存入map
                        map.put("name", name);
                        map.put("shijian", shijian);
                        //ArrayList集合
                        list.add(map);

                    }

                }

//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    title = jsonObject.getString("devicename");
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("title", title);
//                    list.add(map);
//                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //添加分割线
                    recyclerView.addItemDecoration(new DividerItemDecoration(
                            MainActivity.this, DividerItemDecoration.VERTICAL));
                    recy_item_Adapter recy=new recy_item_Adapter(list,MainActivity.this);
                    //设置布局显示格式
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(recy);
                    break;


            }
        }
    };
}
