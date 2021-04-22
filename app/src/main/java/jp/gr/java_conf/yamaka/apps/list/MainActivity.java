package jp.gr.java_conf.yamaka.apps.list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SAVE_KEY = "save_data";
    List<String> data = new ArrayList<>();
    //ListView list = findViewById(R.id.list);
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = findViewById(R.id.list);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        data = gson.fromJson(pref.getString(SAVE_KEY, ""), new TypeToken<ArrayList<String>>(){}.getType());

        if(data != null) {
            adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, data);
            list.setAdapter(adapter);
        }

        //ボタン長押し時の処理
        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> av, View view, int position, long id) {
                        adapter.remove((String) ((TextView) view).getText());
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        Gson gson = new Gson();
                        pref.edit().putString(SAVE_KEY, gson.toJson(data)).apply();
                        return true;
                    }
                }
        );
    }

    public void btn_onClick(View view){
        EditText editText = findViewById(R.id.editText);
        if(editText.length() != 0) {
            if(data == null){
                data = new ArrayList<>();
            }
            data.add(editText.getText().toString());
            adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, data);
            ListView list = findViewById(R.id.list);
            list.setAdapter(adapter);

            //データの保存
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            Gson gson = new Gson();
            pref.edit().putString(SAVE_KEY, gson.toJson(data)).apply();

            editText.setText("");
        }
    }

}