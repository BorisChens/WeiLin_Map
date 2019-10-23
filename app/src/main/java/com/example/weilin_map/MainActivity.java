package com.example.weilin_map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static final String TAG="Request";

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mlocationClient;
    private MylocationListener mlistener;
    //上下文
    private Context context;

    private double mLatitude;
    private double mLongitude;
    private float mCurrentX;

    private Button mGetMylocationBN;

    private Button GET_FROM;

    private Button POST_TO;

    private RequestQueue mQueue;

    private StringRequest stringRequest;

    private Gson gson;

    private JsonObject jsonObject;

    private JsonElement jsonElement;

    String dimension=String.valueOf(mLatitude);

    String longtitude=String.valueOf(mLongitude);
  //  PopupMenu popup=null;

    //自定义图标
    private BitmapDescriptor mIconLocation;

    private MyOrientationListener myOrientationListener;
    //定位图层显示方式
    private MyLocationConfiguration.LocationMode locationMode;

    private Double dimension_real;

    private Double longtitude_real;

    public static final String BAIDU_LBS_TYPE = "bd09ll";

    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.app_toolbar);
        GET_FROM=(Button)findViewById(R.id.okhttp_get);
        POST_TO=(Button)findViewById(R.id.okhttp_post);
        gson=new Gson();
        setSupportActionBar(toolbar);
        this.context = this;

        initView();
        //判断是否为Android 6.0 以上的系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showLocMap();
        } else {
            initLocation();//initLocation为定位方法
        }

        POST_TO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.okhttp_post){
                    sendRequestWithOkHttp();
                }
            }
        });
        mQueue= Volley.newRequestQueue(MainActivity.this);
        stringRequest=new StringRequest("http://119.23.190.83:8080/zhaqsq/request/getBydevice?devicenumber=862177040005277",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        System.out.println("response" + response);
                        jsonBean jbean = gson.fromJson(response, jsonBean.class);
                        System.out.println("--------------------------");
                        List<extend> extendList = jbean.getExtend_01();
                      //  System.out.println("extends=" + extendList);
                       /*
                        jsonObject=(JsonObject)new JsonParser().parse(response).getAsJsonObject();
                        //jsonElement=(JsonElement)new JsonParser().parse(response);
                        String dimension=jsonObject.get("extend").getAsJsonObject().get("request").getAsJsonObject().get("dimension").getAsString();
                        String longtitude=jsonObject.get("extend").getAsJsonObject().get("request").getAsJsonObject().get("longtitude").getAsString();
                        //Log.d(TAG, "dimension"+dimension);
                        //Log.d(TAG, "longtitude"+longtitude);
                        System.out.println("dimension"+dimension);
                        System.out.println("longtitude"+longtitude);

                        //JsonElement something=new JsonParser().parse(response);
                       // String dimension=something.getAsJsonObject()
                           */
                       /*
                        jsonObject=(JsonObject)new JsonParser().parse(response).getAsJsonObject();
                        int code=jsonObject.get("code").getAsInt();
                        System.out.println("code:"+code);
                        String msg=jsonObject.get("msg").getAsString();
                        System.out.println("msg:"+msg);
                        */
                       JsonParser jp=new JsonParser();
                       jsonObject=jp.parse(response).getAsJsonObject();
                       String dimenson=jsonObject.get("extend").getAsJsonObject().get("request").getAsJsonObject().get("dimension").getAsString();
                       System.out.println("dimension:"+dimenson);
                        String longtitude=jsonObject.get("extend").getAsJsonObject().get("request").getAsJsonObject().get("longitude").getAsString();
                        System.out.println("longitude:"+longtitude);

                        double dimension_1=Double.parseDouble(dimenson);
                        double longitude_1=Double.parseDouble(longtitude);

                        double dimension_2=Math.floor(dimension_1);
                        double longitude_2=Math.floor(longitude_1);

                        double dimension_real=(dimension_2%100+dimension_1-dimension_2)/60+Math.floor(dimension_2/100);
                        double longitude_real=(longitude_2%100+longitude_1-longitude_2)/60+Math.floor(longitude_2/100);
                        System.out.println(dimension_real);
                        System.out.println(longitude_real);

                        /*
                        GPS坐标转换为百度坐标
                         */
                        Gps gp=gps84_To_Gcj02(dimension_real,longitude_real);
                        Gps gp2=gcj02_To_Bd09(gp.getWgLat(),gp.getWgLon());
                        System.out.println("latitude:"+gp2.getWgLat());
                        System.out.println("longtitude:"+gp2.getWgLon());

                        //创建marker
                        LatLng l1=new LatLng(gp2.getWgLat(),gp2.getWgLon());
                       // MarkerOptions point=new MarkerOptions().getPosition(l1).icon
                        MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(l1);
                        mBaiduMap.animateMapStatus(update);
                      //先开启图层
                    //    mBaiduMap.setMyLocationEnabled(true);
                        //显示我的位置
                      MyLocationData.Builder builder=new MyLocationData.Builder();
                      builder.latitude(dimension_real);
                      builder.longitude(longitude_real);
                      MyLocationData data=builder.build();
                      mBaiduMap.setMyLocationData(data);
                     // mBaiduMap.setMyLocationEnabled(true);
                        //程序结束后关闭图层
                     //   mBaiduMap.setMyLocationEnabled(false);

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.getMessage(),error);
                    }

    });

        GET_FROM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.okhttp_get){
                 //   GetRequest();
                    mQueue.add(stringRequest);
                }
            }
        });

    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.weilin_map);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //根据给定增量缩放地图级别
        //越大越好
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        MapStatus mMapStatus;//地图当前状态
        MapStatusUpdate mMapStatusUpdate;//地图将要变化成的状态
        mMapStatus = new MapStatus.Builder().overlook(-45).build();
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mGetMylocationBN = (Button) findViewById(R.id.return_loc);
        mGetMylocationBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyLocation();
                Toast.makeText(MainActivity.this,"您已回到当前位置",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 定位方法
     */
    private void initLocation() {
        //普通模式
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mlocationClient = new LocationClient(this);
        mlistener = new MylocationListener();

        //注册监听器
        mlocationClient.registerLocationListener(mlistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒，当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mlocationClient.setLocOption(mOption);

        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        //图标可以换换
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.nav_location);

        myOrientationListener = new MyOrientationListener(context);
        //通过接口回调来实现实时方向的改变
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }
/*
开始定位
 */
    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        myOrientationListener.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    public void getMyLocation() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate update_me = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(update_me);
    }
/*
    public void onPopupMenuClick(View v) {
        // 创建PopupMenu对象
        popup = new PopupMenu(this, v);
        // 将R.menu.menu_main菜单资源加载到popup菜单中
        getMenuInflater().inflate(R.menu.map_menu, popup.getMenu());
        // 为popup菜单的菜单项单击事件绑定事件监听器
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.id_map_common:
                                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                                break;
                            case R.id.id_map_site:
                                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                                break;
                            case R.id.id_map_traffic:
                                if (mBaiduMap.isTrafficEnabled()) {
                                    mBaiduMap.setTrafficEnabled(false);
                                    item.setTitle("实时交通(off)");
                                } else {
                                    mBaiduMap.setTrafficEnabled(true);
                                    item.setTitle("实时交通(on)");
                                }
                                break;
                            case R.id.id_map_mlocation:
                                getMyLocation();
                                break;
                            case R.id.id_map_model_common:
                                //普通模式
                                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                                break;
                            case R.id.id_map_model_following:
                                //跟随模式
                                locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                                break;
                            case R.id.id_map_model_compass:
                                //罗盘模式
                                locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                                break;
                        }
                        return true;
                    }
                });
        popup.show();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.id_map_traffic:
                if (mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通(off)");
                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(on)");
                }
                break;
            case R.id.id_map_mlocation:
                getMyLocation();
                break;
            case R.id.id_map_model_common:
                //普通模式
                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.id_map_model_following:
                //跟随模式
                locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case R.id.id_map_model_compass:
                //罗盘模式
                locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;

                default:
        }

        return true;
    }

    /**
     * 所有的定位信息都通过接口回调来实现
     */
    public class MylocationListener extends BDAbstractLocationListener {
        //定位请求回调接口
        private boolean isFirstIn = true;

        //定位请求回调函数,这里面会得到定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //BDLocation 回调的百度坐标类，内部封装了如经纬度、半径等属性信息
            //MyLocationData 定位数据,定位数据建造器
            /**
             * 可以通过BDLocation配置如下参数
             * 1.accuracy 定位精度
             * 2.latitude 百度纬度坐标
             * 3.longitude 百度经度坐标
             * 4.satellitesNum GPS定位时卫星数目 getSatelliteNumber() gps定位结果时，获取gps锁定用的卫星数
             * 5.speed GPS定位时速度 getSpeed()获取速度，仅gps定位结果时有速度信息，单位公里/小时，默认值0.0f
             * 6.direction GPS定位时方向角度
             * */
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            //可在此处输入坐标
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(mLatitude)//百度纬度坐标
                    .longitude(mLongitude)//百度经度坐标
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationData(data);
            //配置定位图层显示方式,三个参数的构造器
            /**
             * 1.定位图层显示模式
             * 2.是否允许显示方向信息
             * 3.用户自定义定位图标
             * */
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                //地理坐标基本数据结构
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //改变地图状态
                mBaiduMap.setMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(context, "您当前的位置为：" + bdLocation.getAddrStr(),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Android 6.0 以上的版本的定位方法
     */
    public void showLocMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);
        } else {
            initLocation();
        }
    }

    //Android 6.0 以上的版本申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder().add("devicenumber","862177040005277").add("diminsion",dimension).add("longtitude",longtitude).build();
                    Request request=new Request.Builder().url("http://119.23.190.83:8080/zhaqsq/request/getBydevice").post(requestBody).build();
                    Response response=client.newCall(request).execute();
                }

                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

/*
    public void GetRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://119.23.190.83:8080/zhaqsq/request/getBydevice?devicenumber=862177040005277").build();
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().string();
                    //
                    parseJSONWithGSON(responseData);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
*/

    public void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
       // List<mapint_info>info=gson.fromJson(jsonData,new TypeToken<List<mapint_info>>(){}.getType());
        List<jsonBean>json=gson.fromJson(jsonData,new TypeToken<List<jsonBean>>(){}.getType());
      //  Log.d(TAG, "I love u");
        for (jsonBean example:json){
            //Log.d(TAG, "parseJSONWithGSON: ");
            Log.d(TAG, example.toString());

        }
    }

    //Gcj02转为百度
    public static Gps gcj02_To_Bd09(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y *pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new Gps(bd_lat, bd_lon);
    }

    //Gps转为Gcj02
    public static Gps gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLat, mgLon);
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public static Gps transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new Gps(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLat, mgLon);
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
}
