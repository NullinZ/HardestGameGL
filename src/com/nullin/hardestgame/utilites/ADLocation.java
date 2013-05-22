package com.nullin.hardestgame.utilites;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.nullin.hardestgame.HGActivity;

public class ADLocation {

	Location mLocation;

	public ADLocation(final HGActivity hg) {
		super();
		mLocation = getLocation(hg);
		if (mLocation != null) {
			new Thread() {
				public void run() {
					try {
						Address ads = getAddressbyGeoPoint(hg, getGeoByLocation(mLocation));
						Configuration.countryCode = ads.getCountryCode();
					} catch (Exception e) {
					}
				};
			}.start();
		}
	}

	public Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

	// 通过Location获取GeoPoint

	public double[] getGeoByLocation(Location location) {
		double[] gp = null;
		try {
			if (location != null) {
				double geoLatitude = location.getLatitude() * 1E6;
				double geoLongitude = location.getLongitude() * 1E6;
				gp = new double[] { (int) geoLatitude, (int) geoLongitude };
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gp;
	}

	// 通过GeoPoint来获取Address
	public Address getAddressbyGeoPoint(Context cntext, double[] gp) {
		Address result = null;
		try {
			if (gp != null) {
				Geocoder gc = new Geocoder(cntext, Locale.CHINA);

				double geoLatitude = (int) gp[0] / 1E6;
				double geoLongitude = (int) gp[1] / 1E6;

				List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);
				if (lstAddress.size() > 0) {
					result = lstAddress.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
