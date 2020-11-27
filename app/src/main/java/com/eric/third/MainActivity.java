package com.eric.third;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.eric.third.util.DomHelper;
import com.eric.third.util.PullHelper;
import com.eric.third.util.SaxHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnsax;
    private Button btndom;
    private Button btnpullread;
    private Button btnpullwrite;
    private Button btnjson;
    private ListView list;
    private ArrayList<Person> persons;
    private ArrayAdapter<Person> mAdapter;
    private String myjson = "[\n" +
            "    { \"id\":\"1\",\"name\":\"基神\",\"age\":\"18\" },\n" +
            "    { \"id\":\"2\",\"name\":\"B神\",\"age\":\"19\"  },\n" +
            "    { \"id\":\"3\",\"name\":\"曹神\",\"age\":\"20\" }\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
    }

    private void setViews() {
        list = findViewById(R.id.list);
        btnsax = findViewById(R.id.btnsax);
        btndom = findViewById(R.id.btndom);
        btnpullread = findViewById(R.id.btnpullread);
        btnpullwrite = findViewById(R.id.btnpullwrite);
        btnjson = findViewById(R.id.btnjson);

        btnsax.setOnClickListener(this);
        btndom.setOnClickListener(this);
        btnpullread.setOnClickListener(this);
        btnpullwrite.setOnClickListener(this);
        btnjson.setOnClickListener(this);
    }

    private ArrayList<Person> readxmlForSAX() throws Exception {
        //获取文件资源建立输入流对象
        InputStream is = getAssets().open("person1.xml");
        //①创建XML解析处理器
        SaxHelper ss = new SaxHelper();
        //②得到SAX解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //③创建SAX解析器
        SAXParser parser = factory.newSAXParser();
        //④将xml解析处理器分配给解析器,对文档进行解析,将事件发送给处理器
        parser.parse(is, ss);
        is.close();
        return ss.getPersons();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsax:
                try {
                    persons = readxmlForSAX();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_expandable_list_item_1, persons);
                list.setAdapter(mAdapter);
                break;
            case R.id.btndom:
                DomHelper ds = new DomHelper();
                persons = ds.queryXML(getApplicationContext());
                mAdapter = new ArrayAdapter<Person>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, persons);
                list.setAdapter(mAdapter);
                break;
            case R.id.btnpullread:
                //获取文件资源建立输入流对象
                try {
                    InputStream is = getAssets().open("person3.xml");
                    persons = PullHelper.getPersons(is);
                    is.close();
                    if(persons.equals(null)){
                        Toast.makeText(getApplicationContext(), "呵呵", Toast.LENGTH_SHORT).show();
                    }
                    for(Person p1 :persons)
                    {
                        Log.i("逗比", p1.toString());
                    }
                    mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, persons);
                    list.setAdapter(mAdapter);
                } catch (Exception e) {e.printStackTrace();}
                break;
            case R.id.btnpullwrite:
                Context context = getApplicationContext();
                List<Person> persons = new ArrayList<>();
                persons.add(new Person(21,"逗比1",70));
                persons.add(new Person(31,"逗比2",50));
                persons.add(new Person(11,"逗比3",30));
                File xmlFile = new File(context.getFilesDir(),"jay.xml");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(xmlFile);
                    PullHelper.save(persons, fos);
                    fos.close();
                    Toast.makeText(context, "文件写入完毕", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnjson:
                persons = parseEasyJson(myjson);
                mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, persons);
                list.setAdapter(mAdapter);
                break;
        }
    }

    private ArrayList<Person> parseEasyJson(String json){
        persons = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Person person = new Person();
                person.setId(i);
                person.setName(jsonObject.getString("name"));
                person.setAge(Integer.parseInt(jsonObject.getString("age")));
                persons.add(person);
            }
        }catch (Exception e){e.printStackTrace();}
        System.out.println(persons.toString());
        return persons;
    }

    private void parseDiffJson(String json) {
        try {
            JSONObject jsonObject1 = new JSONObject(json);
            Log.e("Json", json);
            JSONArray jsonArray = jsonObject1.getJSONArray("ch");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                //取出name
                String sname = jsonObject.getString("names");
                JSONArray jarray1 = jsonObject.getJSONArray("data");
                JSONArray jarray2 = jsonObject.getJSONArray("times");
                Log.e("Json", sname);
                Log.e("Json", jarray1.toString());
                Log.e("Json", jarray2.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

