package edu.hhu.jetmuffin.maipu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hhu.jetmuffin.map.MapUtil;
import edu.hhu.jetmuffin.model.Point;
import edu.hhu.jetmuffin.model.User;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sp;
    private User user;

    private BaiduMap mBaiduMap;
    private MapView mMapView;


    private Map<String, String> mapData;
    private int[] defaultColors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.DKGRAY, Color.RED, Color.YELLOW};
    private boolean isTraffic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        findViewById(R.id.locate_my_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MapActivity.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
                onDialogLocate();
            }
        });

        findViewById(R.id.locate_longitude_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 修改为定位当前位置
                onLocateLongitude(new Point(31.921571, 118.795228), true);
            }
        });

        findViewById(R.id.city_distance_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDistance();
            }
        });

        findViewById(R.id.erase_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.clear();
            }
        });

        sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        String email = sp.getString("email", "");
        user = new User(sp);

        TextView tvUserNickname = (TextView) findViewById(R.id.userNickname);
        TextView tvUserEmail = (TextView) findViewById(R.id.userEmail);

        tvUserNickname.setText(user.getNickname());
        tvUserEmail.setText(user.getEmail());

        mapInit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        }else if(id == R.id.nav_map_traffic) {
            if(isTraffic) {
                mBaiduMap.setTrafficEnabled(false);
                isTraffic = false;
            }
            else {
                mBaiduMap.setTrafficEnabled(true);
                isTraffic = true;
            }
        }else if(id == R.id.nav_map_satellite){
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            logout();
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始化Map
     */
    void mapInit() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        SharedPreferences sp = getSharedPreferences("data", Activity.MODE_PRIVATE);
        mapData = MapUtil.getData();
        hideZoomView(mMapView);
    }

    public void onDialogLocate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        LayoutInflater inflater = MapActivity.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_locate, null);
        final EditText mEditText = (EditText) mView.findViewById(R.id.position);
        builder.setView(mView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String postion = mEditText.getText().toString();
                        Point point = MapUtil.parse(postion);
                        onLocateLongitude(point, true);
                    }
                })
                .setNegativeButton("Cancel",null);
        builder.show();
    }

    public void onDialogDistance(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        LayoutInflater inflater = MapActivity.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_distance, null);
        final EditText tvFrom = (EditText) mView.findViewById(R.id.from);
        final EditText tvTo = (EditText) mView.findViewById(R.id.to);
        builder.setView(mView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String fromText = tvFrom.getText().toString();
                        String toText = tvTo.getText().toString();
                        Point from = MapUtil.parse(fromText);
                        Point to = MapUtil.parse(toText);

                        calDistance(from, to);
                    }
                })
                .setNegativeButton("Cancel",null);
        builder.show();
    }

    /**
     * 根据经纬度定位
     * @param point
     */
    public void onLocateLongitude(Point point, boolean mark){
        LatLng latLng = new LatLng(point.getX(), point.getY());

        if(mark){
            onMarker(point);
        }
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    /**
     * 地图标注
     * @param point
     */
    public void onMarker(Point point){

        LatLng latLng = new LatLng(point.getX(), point.getY());
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.mark);
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
    }

    public void onLine(Point from, Point to){
        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> colors = new ArrayList<>();

        points.add(new LatLng(from.getX(),from.getY()));
        points.add(new LatLng(to.getX(),to.getY()));

        int key = (int)(Math.random()*8);
        colors.add(Integer.valueOf(defaultColors[key]));

        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .colorsValues(colors).points(points);
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }

    public void calDistance(Point from, Point to){
        onMarker(from);
        onMarker(to);
        onLine(from, to);

        Double distance = DistanceUtil. getDistance(Point.latLng(from), Point.latLng(to))/1000;

        LatLng latLng = Point.middle(from, to);
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(36)
                .fontColor(0xFFFF00FF)
                .text(String.format("%.2f ", distance) + "km")
                .position(latLng);
        mBaiduMap.addOverlay(textOption);
        onLocateLongitude(from, false);
    }

    /**
     * 百度地图定位我的位置
     */
    public void onLocate(){
        //TODO 添加定位方法
    }

    /**
     * 隐藏缩放控件
     *
     * @param mapView
     */
    private void hideZoomView(MapView mapView) {
        // 隐藏缩放控件
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(View.GONE);
    }

    public void logout(){
        SharedPreferences sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email","");
        editor.putString("password","");
    }
}
