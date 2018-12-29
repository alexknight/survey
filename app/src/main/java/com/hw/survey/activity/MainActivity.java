package com.hw.survey.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.view.View;
import android.widget.Spinner;

import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.hw.survey.R;
import com.hw.survey.city.Qu;
import com.hw.survey.city.SheQu;
import com.hw.survey.city.Street;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.User;
import com.hw.survey.util.PermissionUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hw.survey.MyApplication.currentUsers;

public class MainActivity extends Activity implements OnMenuItemClickListener {

    EditText etName;
    EditText etPhone;

    Spinner spinnerQu;
    Spinner spinnerStreet;
    Spinner spinnerShequ;
    List<Qu> listQu;
    List<Street> listStreet;
    List<SheQu> listSheQu;
    User currentUser;

    ArrayAdapter adapterQu;
    ArrayAdapter adapterStreet;
    ArrayAdapter adapterSheQu;

    boolean isFirstSelect1 = true;
    boolean isFirstSelect2 = true;
    int defaultStreet = 0;
    int defaultShequ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 百度地图初始化
        String sdpath = Environment.getDownloadCacheDirectory().getAbsolutePath();
        SDKInitializer.initialize(getApplicationContext());

        etName = findViewById(R.id.editTextName);
        etPhone = findViewById(R.id.editTextPhone);
        initData();

        spinnerQu = findViewById(R.id.spinner_qu);
        adapterQu = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, listQu);
        spinnerQu.setAdapter(adapterQu);
        spinnerStreet = findViewById(R.id.spinner_street);
        adapterStreet = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, listStreet);
        spinnerStreet.setAdapter(adapterStreet);
        spinnerShequ = findViewById(R.id.spinner_shequ);
        adapterSheQu = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, listSheQu);
        spinnerShequ.setAdapter(adapterSheQu);

        spinnerQu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listStreet.clear();
                listStreet.addAll(listQu.get(position).lists);
                adapterStreet.notifyDataSetChanged();


                if(position > 0){
                    currentUser.qu = listQu.get(position).name;
                    currentUser.selectQu = position;
                    if(isFirstSelect1){
                        isFirstSelect1 = false;
                        spinnerStreet.setSelection(defaultStreet,false);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listSheQu.clear();
                listSheQu.addAll(listStreet.get(position).lists);
                adapterSheQu.notifyDataSetChanged();
                if(position > 0){
                    currentUser.street = listStreet.get(position).name;
                    currentUser.selectStreet = position;
                    if(isFirstSelect2){
                        isFirstSelect2 = false;
                        spinnerShequ.setSelection(defaultShequ,false);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.btn_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUsers.getUsers().size() < 1){
                    Toast.makeText(MainActivity.this,"无已有用户",Toast.LENGTH_LONG).show();
                    return;
                }
                PopupMenu popup = new PopupMenu(MainActivity.this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                android.view.Menu menu_more = popup.getMenu();
                //填充菜单
                for (int i = 0; i < currentUsers.getUsers().size(); i++) {
                    menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + i, i, currentUsers.getUsers().get(i).name);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        User us = currentUsers.getUsers().get(i-1);
                        fillView(us);
                        return true;
                    }
                });
                //显示(这一行代码不要忘记了)
                popup.show();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.selectShequ = spinnerShequ.getSelectedItemPosition();
                currentUser.shequ = listSheQu.get(spinnerShequ.getSelectedItemPosition()).name;
                currentUser.name = etName.getText().toString();
                currentUser.phone = etPhone.getText().toString();

                if(currentUser.selectQu == 0){
                    Toast.makeText(MainActivity.this, "请选择区！", Toast.LENGTH_LONG).show();
                    return;
                }
                if(currentUser.selectStreet == 0){
                    Toast.makeText(MainActivity.this, "请选择街道！", Toast.LENGTH_LONG).show();
                    return;
                }
                if(currentUser.selectShequ == 0){
                    Toast.makeText(MainActivity.this, "请选择社区！", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(currentUser.name)){
                    Toast.makeText(MainActivity.this, "请填写调查员！", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(currentUser.phone)){
                    Toast.makeText(MainActivity.this, "请填写联系方式！", Toast.LENGTH_LONG).show();
                    return;
                }
                int p = currentUsers.getUserByName(currentUser.name);
                if( p > -1 && !currentUsers.getUsers().get(p).isSame(currentUser)){
                    Toast.makeText(MainActivity.this, "该调查员已注册，请选择调查员姓名进行登录！", Toast.LENGTH_LONG).show();
                    return;
                }else if(p > -1 && currentUsers.getUsers().get(p).isSame(currentUser)){
                    currentUsers.setSelectId(p);
//                    if(currentUsers.getSelectUser().isXiaoQuFinish){
//                        Intent intent = new Intent(MainActivity.this, FamilyListActivity.class);
//                        startActivity(intent);
//                        return;
//                    }
                    Intent intent = new Intent(MainActivity.this, FamilyListActivity.class);
                    startActivity(intent);
                    return;
                }else {
                    User user = new User();
                    user.selectQu = currentUser.selectQu;
                    user.selectStreet = currentUser.selectStreet;
                    user.selectShequ = currentUser.selectShequ;
                    user.qu = currentUser.qu;
                    user.street = currentUser.street;
                    user.shequ = currentUser.shequ;
                    user.phone = currentUser.phone;
                    user.name = currentUser.name;
                    currentUsers.getUsers().add(user);
                    currentUsers.setSelectId(currentUsers.getUsers().size() - 1);

                }
                DataUtil.saveUsers(MainActivity.this.getApplicationContext(), currentUsers);
                Intent intent = new Intent(MainActivity.this, FamilyListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OfflineMapActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();




        try{
            String[] s = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE};
            PermissionUtil.checkPermission(this,s,1);
        }catch (Exception e){

        }
    }

    private void fillView(User user){
        isFirstSelect1 = true;
        isFirstSelect2 = true;
        defaultStreet = user.selectStreet;
        defaultShequ = user.selectShequ;
        spinnerQu.setSelection(user.selectQu,false);
        try {
            Thread.sleep(100);
            spinnerStreet.setSelection(user.selectStreet,false);
            Thread.sleep(100);
            spinnerShequ.setSelection(user.selectShequ,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        etName.setText(user.name);
        etPhone.setText(user.phone);
    }

    private void initView(){
        if(currentUsers != null && currentUsers.getUsers().size() > 0 && currentUsers.getSelectId() > -1){
            spinnerQu.setSelection(currentUsers.getSelectUser().selectQu,false);

            etName.setText(currentUsers.getSelectUser().name);
            etPhone.setText(currentUsers.getSelectUser().phone);
        }
    }

    private void initData(){
        currentUser = new User();

        List<Qu> list =null;
        Qu province = null;

        List<Street> cities = null;
        Street city = null;

        List<SheQu> districts = null;
        SheQu district = null;

        // 创建解析器，并制定解析的xml文件
        XmlResourceParser parser = getResources().getXml(R.xml.city);
        try{
            int type = parser.getEventType();
            while(type!=1) {
                String tag = parser.getName();//获得标签名
                switch (type) {
                    case XmlResourceParser.START_DOCUMENT:
                        list = new ArrayList<Qu>();
                        break;
                    case XmlResourceParser.START_TAG:
                        if ("qu".equals(tag)) {
                            province=new Qu();
                            cities = new ArrayList<Street>();
                            int n =parser.getAttributeCount();
                            for(int i=0 ;i<n;i++){
                                //获得属性的名和值
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if("name".equals(name)){
                                    province.name = value;
                                }
                            }
                        }
                        if ("street".equals(tag)){//城市
                            city = new Street();
                            districts = new ArrayList<SheQu>();
                            int n =parser.getAttributeCount();
                            for(int i=0 ;i<n;i++){
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if("name".equals(name)){
                                    city.name = value;
                                }
                            }
                        }
                        if ("shequ".equals(tag)){
                            district = new SheQu();
                            district.name = (parser.nextText());
                            districts.add(district);
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if ("street".equals(tag)){
                            city.lists = districts;
                            cities.add(city);
                        }
                        if("qu".equals(tag)){
                            province.lists = cities;
                            list.add(province);
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        }catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */
        catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        listQu = list;
        listStreet = new ArrayList<>();
        listSheQu = new ArrayList<>();

        if(currentUsers != null && currentUsers.getUsers().size() > 0 && currentUsers.getSelectId() > -1){
            defaultShequ = currentUsers.getSelectUser().selectShequ;
            defaultStreet = currentUsers.getSelectUser().selectStreet;
        }
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        etName.setText(item.getTitle());
        return false;
    }
}
