package wonheang.org.myapplication;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Settings.Secure.isLocationProviderEnabled;

public class MainActivity extends Activity {
    Double UserDistance; //두사람간 간거리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 위치 정보 확인을 위해 정의한 메소드 호출
        startLocationService();

    }

    private void enableGPSSetting() {
        ContentResolver res = getContentResolver();

        //Gps가 켜져있는지 확인하는 거에요~!
        boolean gpsEnabled = isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Gps 설정")
                    .setPositiveButton("gps야 켜져라", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //gps 설정화면 띄우는 거입니다
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            {

            }
        }
    }


    private void startLocationService() {
        // 위치 관리자 객체 참조  이걸로 조종합니다.
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();

        // 위치 정보를 받을 리스너 생성

        long minTime = 10000;       // 시간이 얼마마다 위치가 바뀌는지에 대한 변수
        float minDistance = 0;    // 거리가 얼마 이동하냐에 위치가 바뀌는지에 대한변수


        // GPS를 이용한 위치 요청

    //이건 뭔지 모르겠어요 퍼미션 체크하는거같은데   이게 없으면 밑에 requestLocationUpdates 부분이안되네요
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        // 네트워크를 이용한 위치 요청
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
        try {
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();


        }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }



    private class GPSListener implements LocationListener{

        //  위치 정보가 확인될 때 자동 호출되는 메소드 위치가 바뀔떄마다

        public void onLocationChanged(Location location) {
            Location UserIdlocation = null;  //mylocation 이름을 서버 아이디로
            Double mylatitude = location.getLatitude();
            Double mylongitude = location.getLongitude();

            UserIdlocation.setLatitude(mylatitude);
            UserIdlocation.setLongitude(mylongitude);



            UserDistance = (double) UserIdlocation.distanceTo(); //이 생성자에 다른 아이디 위치정보 넣으면 서로간의 거리가 구해져요요
            String msg = "Latitude : "+ mylatitude + "\nLongitude:"+ mylongitude;
            Log.i("GPSListener", msg);

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
