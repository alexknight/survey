package com.hw.survey.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.adapter.NearAddressAdapter;
import com.hw.survey.adapter.SearchAddressAdapter;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.hw.survey.util.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Nate
 * @desc 服务地点选择界面
 * @date 2015-12-20
 */
public class LocationActivity extends Activity
        implements BDLocationListener,OnGetGeoCoderResultListener, OnGetPoiSearchResultListener {

    @InjectView(R.id.mapView)
    MapView mMapView;
    @InjectView(R.id.near_list_empty_ll)
    LinearLayout near_list_empty_ll;
    @InjectView(R.id.near_address_list)
    ListView near_address_list;
    @InjectView(R.id.search_address_list_view)
    ListView search_address_list_view;
    @InjectView(R.id.search_et)
    EditText search_et;
    @InjectView(R.id.search_empty_tv)
    TextView search_empty_tv;
    @InjectView(R.id.search_ll)
    LinearLayout search_ll;
    @InjectView(R.id.btn_confirm)
    Button btn_confirm;
    @InjectView(R.id.out_code_btn)
    Button btn_out_code;
    @InjectView(R.id.home_btn)
    Button btn_home;
    @InjectView(R.id.work_place_btn)
    Button btn_work_place;
    @InjectView(R.id.one_btn_loc_area)
    View one_btn_loc_area;

    @InjectView(R.id.search_btn)
    Button btn_search;

    private ProgressDialog progress;

    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BaiduMap mBaiduMap = null;
    private MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    private boolean isFirstLoc = true;// 是否首次定位
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocClient;// 定位相关
    private PoiSearch mPoiSearch = null;
    private String cityName = "";


    private MapStatus currentStatus = null;

    private MKOfflineMap mOfflineMap = null;

    private NearAddressAdapter nearAddressAdapter = null;
    private SearchAddressAdapter searchAddressAdapter = null;
    private List<PoiInfo> nearAddresses = new ArrayList<PoiInfo>();
    private List<PoiInfo> searchAddresses = new ArrayList<PoiInfo>();
//
//    private List<PoiInfo> nearAddresses1 = new ArrayList<>();
//    private List<PoiInfo> searchAddresses1 = new ArrayList<PoiInfo>();
//
//    private List<PoiInfo> nearAddresses2 = new ArrayList<>();
//    private List<PoiInfo> searchAddresses2 = new ArrayList<PoiInfo>();
//
//    private List<PoiInfo> nearAddresses3 = new ArrayList<>();
//    private List<PoiInfo> searchAddresses3 = new ArrayList<PoiInfo>();

    private String currentSearchText = "";

    private LatLng GEO_SHANGHAI = new LatLng(29.56825, 106.55690);

    private int posFamily;
    private int posPerson;
    private int posTrip;
    private double defaultX;
    private double defaultY;

    private void initData(){
//        String[][] stringsA = {
//                {"重庆市长寿区","静怡南街"},
//                {"重庆市长寿区","静怡北街"},
//                {"石桥铺科创路64号渝高广场B座13楼-6号","静云轩"},
//                {"兴科二路13号","逸静花园"},
//                {"重庆市合川区合阳大道2号步步高百货F3","静美颜工作室"},
//                {"静亭路82号","逸静花园小区"},
//                {"六角丘巷与南津街交叉口东南50米","静衣阁"},
//                {"铝城南路4-6幢附6号","静花缘"},
//                {"重庆市铜梁区龙门苑东南(龙门堤街南)","静花缘"},
//                {"重庆市铜梁区","静禅瑜伽"},
//                {"新城正南街","静悦瑜珈(潼南店)"},
//                {"渝北三村32号红鼎国际名苑C座8层","静悦瑜珈(江北总店)"}
//        };
//
//        double[][] doublesA = {
//                {107.00495756999042,29.82856225791542},
//                {107.00567621439816,29.829635363006055},
//                {106.55690074787164,29.571050045330505},
//                {106.55580481514981,29.56938514493584},
//                {106.55690074787162,29.5682542533809},
//                {106.55796973142814,29.571489825710277},
//                {107.00495756999042,29.82856225791542},
//                {107.00567621439816,29.829635363006055},
//                {106.55690074787164,29.571050045330505},
//                {106.55580481514981,29.56938514493584},
//                {106.55690074787162,29.5682542533809},
//                {106.55796973142814,29.571489825710277}
//        };
//
//        String[][] stringsB = {
//                {"重庆市渝中区人民支路19号","静园小区"},
//                {"重庆市垫江县工农北路283号","白银静园"},
//                {"112路内环;145路;152路;181路;262路;338路;421路;862路;881路","大溪沟静园"},
//                {"重庆市万州区","静园路"},
//                {"重庆市涪陵区","静园路"},
//                {"重庆市璧山区沿河西路南段9号","静园宾馆"},
//                {"静园路1271号B幢1层B24-1","静园茶楼"},
//                {"藏龙街112","静园茶社"},
//                {"重庆市九龙坡区龙城静园(兴堰路东100米)","龙城静园"},
//                {"一横街桂溪街43号","静园养生馆"}
//        };
//
//        double[][] doublesB = {
//                {106.56275769979474,29.570421784288488},
//                {107.00567621439816,29.829635363006055},
//                {106.55690074787164,29.571050045330505},
//                {106.55580481514981,29.56938514493584},
//                {106.55690074787162,29.5682542533809},
//                {106.55796973142814,29.571489825710277},
//                {107.00495756999042,29.82856225791542},
//                {107.00567621439816,29.829635363006055},
//                {106.55690074787164,29.571050045330505},
//                {106.55580481514981,29.56938514493584}
//        };
//
//        for (int i = 0; i < 12 ; i++){
//            PoiInfo poiInfo = new PoiInfo();
//            poiInfo.address = stringsA[i][0];
//            poiInfo.name = stringsA[i][1];
//            poiInfo.location = new LatLng(doublesA[i][1],doublesA[i][0]);
//            searchAddresses1.add(poiInfo);
//        }
//
//        for (int i = 0; i < 10 ; i++){
//            PoiInfo poiInfo = new PoiInfo();
//            poiInfo.address = stringsB[i][0];
//            poiInfo.name = stringsB[i][1];
//            poiInfo.location = new LatLng(doublesB[i][1],doublesB[i][0]);
//            searchAddresses2.add(poiInfo);
//        }
//
//
//
//        String[][] strings1 = {
//                {"重庆市渝中区人民路236号","重庆中国三峡博物馆"},
//                {"人民路232号","重庆市政府"},
//                {"中山四路85号","巴渝文化会馆"},
//                {"渝中区上清寺中山四路上曾家岩50号(近重庆市政府)","重庆雾都宾馆"},
//                {"重庆渝中区人民支路96号附2号(近雾都宾馆)","曾家岩南山泉水豆花"},
//                {"重庆市渝中区嘉陵江滨江路272号","名流公馆"}
//        };
//
//        double[][] doubles1 = {
//                {106.55690074787162,29.5682542533809},
//                {106.55796973142812,29.56938514493583},
//                {106.55690074787164,29.571050045330505},
//                {106.55580481514981,29.56938514493584},
//                {106.55690074787162,29.5682542533809},
//                {106.55796973142814,29.571489825710277}
//
//        };
//
//        String[][] strings2 = {
//                {"重庆市渝中区人民路","重庆市政府大楼"},
//                {"重庆市渝中区人民路236号","重庆中国三峡博物馆"},
//                {"重庆市渝中区人民路220号","人民村小区"},
//                {"重庆市渝中区人民路214号","重庆市渝中区公安分局广场支队"},
//                {"渝中区上曾家岩52号","曾家岩小学"},
//                {"重庆市渝中区嘉陵江滨江路272号","名流公馆"}
//        };
//
//        double[][] doubles2 = {
//                {106.55805956197912,29.569306611213474},
//                {106.55690074787162,29.5682542533809},
//                {106.55948786773949,29.56989561262728},
//                {106.55908363026016,29.56999770585275},
//                {106.55911057942545,29.57120710997398},
//                {106.55796973142814,29.571489825710277}
//
//        };
//
//        String[][] strings3 = {
//                {"江北北滨路陈家馆A宗E宗（紧邻江北嘴CBD）","龙湖春森彼岸"},
//                {"北滨一路龙湖星悦荟附近","龙湖星悦荟大厦"},
//                {"北滨路258号","龙湖星悦荟"},
//                {"重庆市江北区五里店北滨一路黎明社区西南100米","团结新村99号楼"},
//                {"北滨路龙湖春森彼岸3号","金色贝乐幼儿园"},
//                {"大兴村长安厂富强二村2-1(建设银行对面)","渝舍德明火锅"}
//        };
//
//        double[][] doubles3 = {
//                {106.55160074536451,29.575094381249873},
//                {106.55409803468142,29.575934640217476},
//                {106.55334345805329,29.575699054303776},
//                {106.55548142516633,29.577897834566564},
//                {106.55235532199264,29.575848258780265},
//                {106.55339735638387,29.57862028037866}
//
//        };
//
//        for (int i = 0; i < 6 ; i++){
//            PoiInfo poiInfo = new PoiInfo();
//            poiInfo.address = strings1[i][0];
//            poiInfo.name = strings1[i][1];
//            poiInfo.location = new LatLng(doubles1[i][1],doubles1[i][0]);
//            nearAddresses1.add(poiInfo);
//        }
//
//        for (int i = 0; i < 6 ; i++){
//            PoiInfo poiInfo = new PoiInfo();
//            poiInfo.address = strings2[i][0];
//            poiInfo.name = strings2[i][1];
//            poiInfo.location = new LatLng(doubles2[i][1],doubles2[i][0]);
//            nearAddresses2.add(poiInfo);
//        }
//
//        for (int i = 0; i < 6 ; i++){
//            PoiInfo poiInfo = new PoiInfo();
//            poiInfo.address = strings3[i][0];
//            poiInfo.name = strings3[i][1];
//            poiInfo.location = new LatLng(doubles3[i][1],doubles3[i][0]);
//            nearAddresses3.add(poiInfo);
//        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_service_location);
        ButterKnife.inject(this);
        initViewsAndEvents();
    }

    protected void initViewsAndEvents() {
        cityName = "重庆";//这个可以通过定位获取
        // 隐藏比例尺和缩放图标

        initData();
//        nearAddresses.addAll(nearAddresses1);

        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);
        posTrip = intent.getIntExtra("posTrip", -1);
        defaultX = intent.getDoubleExtra("x",0);
        defaultY = intent.getDoubleExtra("y",0);

        if(posTrip >= 0){
            one_btn_loc_area.setVisibility(View.VISIBLE);
        }
        btn_out_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LocationActivity.this).setTitle("市外地址").setSingleChoiceItems(getResources().getTextArray(R.array.out_code), -1, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialogInterface, int pos)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("address", getResources().getStringArray(R.array.out_code)[pos]);
                        bundle.putString("addressDetail",getResources().getStringArray(R.array.out_code)[pos]);
                        bundle.putInt("type",1);
                        Intent intent1 = new Intent();
                        intent1.putExtras(bundle);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(posFamily >= 0){
                        Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
                        Bundle bundle = new Bundle();
                        bundle.putString("address", family.address);
                        bundle.putString("addressDetail", family.addressDetail);
                        bundle.putInt("type",1);
                        Intent intent2 = new Intent();
                        intent2.putExtras(bundle);
                        setResult(RESULT_OK, intent2);
                        finish();
                    }else {
                        Toast.makeText(LocationActivity.this,"未设置家庭地址，无法一键选择",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(LocationActivity.this,"未设置家庭地址，无法一键选择",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_work_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posFamily >= 0 && posPerson >= 0){
                    Person person = MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson);

                    if(!(TextUtils.isEmpty(person.address) )){
                        Bundle bundle = new Bundle();
                        bundle.putString("address", person.address);
                        bundle.putString("addressDetail", person.addressDetail);
                        bundle.putInt("type",1);
                        Intent intent2 = new Intent();
                        intent2.putExtras(bundle);
                        setResult(RESULT_OK, intent2);
                        finish();
                        return;
                    }
                }
                Toast.makeText(LocationActivity.this,"未设置成员工作/家庭地址，无法一键选择",Toast.LENGTH_LONG).show();
            }
        });

        mOfflineMap = new MKOfflineMap();
        mOfflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int i, int i1) {

            }
        });
        mOfflineMap.importOfflineData();


        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);
        mBaiduMap = mMapView.getMap();


        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                search_ll.setVisibility(View.GONE);
                mBaiduMap.clear();
                mBaiduMap.addOverlay(new MarkerOptions().position(arg0.target)
                        .icon(mCurrentMarker));
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(arg0.target));

                currentStatus = arg0;

                TextView tv = new TextView(LocationActivity.this);
                tv.setText(String.format("%.6f,%.6f", arg0.target.latitude, arg0.target.longitude));
                InfoWindow infoWindow = new InfoWindow(tv,arg0.target,-47);
                mBaiduMap.showInfoWindow(infoWindow);
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String s = search_et.getText().toString();
                    if(TextUtils.isEmpty(s) || TextUtils.isEmpty(s.trim())){
                        search_ll.setVisibility(View.GONE);
                        Toast.makeText(LocationActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        mPoiSearch.searchInCity((new PoiCitySearchOption())
                                .city(cityName).keyword(s).pageNum(0)
                                .pageCapacity(20));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

//        search_et.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence cs, int start, int before,
//                                      int count) {
//                try{
//                    if (cs == null || cs.length() <= 0 ) {
//                        search_ll.setVisibility(View.GONE);
//                        return;
//                    }
//                    /**
//                     * 使用建议搜索服务获取建议列表
//                     */
//
//                    currentSearchText = cs.toString();
//                    if(!TextUtils.isEmpty(currentSearchText.trim())){
//                        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                                .city(cityName).keyword(cs.toString()).pageNum(0)
//                                .pageCapacity(20));
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }

//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.marker_icon);
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
        mLocClient = new LocationClient(this);
        // 设置地图缩放级别为15
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().zoom(15).build()));
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gpss
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);// 扫描间隔 单位毫秒
        option.setTimeOut(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);





        mLocClient.setLocOption(option);
        mLocClient.start();

        if(defaultX > 0 && defaultY > 0){
            GEO_SHANGHAI = new LatLng(defaultX,defaultY);
            isFirstLoc = false;
        }else {
            this.progress = ProgressDialog.show(this, null, "正在定位中....");
            this.progress.setCancelable(true);
            this.progress.setCanceledOnTouchOutside(false);
        }

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(GEO_SHANGHAI).zoom(18.0F);
        currentStatus = builder.build();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        mBaiduMap.addOverlay(new MarkerOptions().position(builder.build().target)
                .icon(mCurrentMarker));

        nearAddressAdapter = new NearAddressAdapter(this,
                R.layout.item_near_address, nearAddresses);
        near_address_list.setAdapter(nearAddressAdapter);
        near_address_list.setEmptyView(near_list_empty_ll);

        near_address_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PoiInfo poiInfo = nearAddresses.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("Ing", poiInfo.location.latitude + "");
                bundle.putString("Iat", poiInfo.location.longitude + "");
                bundle.putString("Address", poiInfo.name);
                bundle.putString("DetailedAddress", poiInfo.address);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getNearByPoiResult(GEO_SHANGHAI.longitude,GEO_SHANGHAI.latitude);

        searchAddressAdapter = new SearchAddressAdapter(this,
                R.layout.item_search_address, searchAddresses);
        search_address_list_view.setAdapter(searchAddressAdapter);
        search_address_list_view.setEmptyView(search_empty_tv);

        search_address_list_view
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        PoiInfo poiInfo = searchAddresses.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("Ing", poiInfo.location.latitude + "");
                        bundle.putString("Iat", poiInfo.location.longitude + "");
                        bundle.putString("Address", poiInfo.name);
                        bundle.putString("DetailedAddress", poiInfo.address);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Ing", currentStatus.target.latitude + "");
                bundle.putString("Iat", currentStatus.target.longitude + "");
                bundle.putString("Address", "");
                bundle.putString("DetailedAddress", "");
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newLatLng(GEO_SHANGHAI);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {

    }

    private void getNearByPoiResult(double longitude, double latitude){
        nearAddresses.clear();
        nearAddresses.addAll(DBUtils.getInstance(LocationActivity.this).searchNearByPOIs(longitude,latitude));
        nearAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            nearAddresses.clear();
            nearAddresses.addAll(DBUtils.getInstance(LocationActivity.this).searchNearByPOIs(currentStatus.target.longitude,currentStatus.target.latitude));
            nearAddressAdapter.notifyDataSetChanged();
            return;
        }

        List<PoiInfo> list = result.getPoiList();
        if (list != null && list.size() > 0) {
            nearAddresses.clear();
            nearAddresses.addAll(list);
            for (PoiInfo p :
                    nearAddresses) {
                Log.e("Info", "{"+'"'+p.address + '"'+','+'"' + p.name + '"' + "}"+ "\n" + "{"+p.location.longitude + "," + p.location.latitude+ "}");

            }
            nearAddressAdapter.notifyDataSetChanged();
        }else {
            nearAddresses.clear();
            nearAddresses.addAll(DBUtils.getInstance(LocationActivity.this).searchNearByPOIs(currentStatus.target.longitude,currentStatus.target.latitude));
            nearAddressAdapter.notifyDataSetChanged();
        }

    }

    // 定位图标点击，重新设置为初次定位
    @OnClick(value = R.id.location_iv)
    public void reLocation(View view) {
        //Toast.makeText(LocationActivity.this, "正在定位中...", Toast.LENGTH_SHORT).show();
        this.progress = ProgressDialog.show(this, null, "正在定位中....");
        this.progress.setCancelable(true);
        this.progress.setCanceledOnTouchOutside(false);
        isFirstLoc = true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            if (location.getLongitude() == 4.9E-324){
                return;
            }
            if (LocationActivity.this.progress != null) {
                LocationActivity.this.progress.dismiss();
            }
            Log.e("Location","经纬度：" + location.getLatitude() + "," + location.getLongitude());


            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (location.getAddress().address!=null){
                System.out.println("");
            }

            if(location.getCity() != null){
                cityName = location.getCity();
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                        .newLatLng(ll);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {
        System.out.println();
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        System.out.println();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            search_ll.setVisibility(View.VISIBLE);

            searchAddresses.clear();
            searchAddresses.addAll(DBUtils.getInstance(LocationActivity.this).searchPOIs(search_et.getText().toString()));

            searchAddressAdapter.notifyDataSetChanged();
            return;
        }

        if (result.error.name().equals("PERMISSION_UNFINISHED")){
            finish();
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<PoiInfo> list = result.getAllPoi();
            search_ll.setVisibility(View.VISIBLE);
            if (list != null && list.size() > 0) {
                searchAddresses.clear();
                searchAddresses.addAll(list);

                for (PoiInfo p :
                        searchAddresses) {
                    Log.e("Search", "{"+'"'+p.address + '"'+','+'"' + p.name + '"' + "}"+ "\n" + "{"+p.location.longitude + "," + p.location.latitude+ "}");
                }
                searchAddressAdapter.notifyDataSetChanged();
            }
            return;
        }
        search_ll.setVisibility(View.VISIBLE);

        searchAddresses.clear();
        searchAddresses.addAll(DBUtils.getInstance(LocationActivity.this).searchPOIs(search_et.getText().toString()));

        searchAddressAdapter.notifyDataSetChanged();
    }



}
