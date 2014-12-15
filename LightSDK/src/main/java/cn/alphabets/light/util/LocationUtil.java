package cn.alphabets.light.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import cn.alphabets.light.log.Logger;

/**
 * 地理位置相关
 * Created by lin on 14/12/14.
 */
public class LocationUtil {

    private LocationManager manager;

    public interface Result {
        public void onFinished(double latitude, double longitude);
    }

    public void getLocation(Context context, final Result success) {

        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String provider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ? LocationManager.NETWORK_PROVIDER
                : LocationManager.GPS_PROVIDER;

        manager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (success != null) {
                    success.onFinished(location.getLatitude(), location.getLongitude());
                }
                manager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Logger.d(provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Logger.d(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Logger.d(provider);
            }
        });
    }

}
