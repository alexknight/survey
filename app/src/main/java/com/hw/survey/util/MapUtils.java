package com.hw.survey.util;

/**
 * Created by haowang on 2018/5/14.
 */

public class MapUtils {
    public static final double PI = 3.14159265358979324;
    public static final double X_PI = 52.35987755982988;// 3.14159265358979324
    // *3000.0 / 180.0;

//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        double[] a = wgs84ToBd09(104.27718602443197, 30.980198853826675);
//        System.out.println("百度经度:" + a[0] + "  百度纬度:" + a[1]);
//
//        double[] b = bd09ToWgs84(a[0], a[1]);
//        System.out.println("wgs84经度:" + b[0] + "  wgs84纬度:" + b[1]);
//
//        b = wgs84ToBd09(b[0], b[1]);
//        System.out.println("百度经度:" + b[0] + "  百度纬度:" + b[1]);
//        System.out.println(System.currentTimeMillis() - start);
//
//    }

    public static double[] wgs84ToBd09(double lng, double lat) {
        double[] tmp = wgs84ToGcj02(lng, lat);
        return gcj02ToBd09(tmp[0], tmp[1]);
    }

    //百度09坐标系转为wgs84坐标系
    public static double[] bd09ToWgs84(double lng, double lat) {
        double[] tmp = bd09ToGcj02(lng, lat);
        return gcj02ToWgs84(tmp[0], tmp[1]);
    }

    // GCJ02坐标转到百度 BD09坐标
    public static double[] gcj02ToBd09(double lng, double lat) {
        double x = lng;
        double y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        return new double[]{z * Math.cos(theta) + 0.0065, z * Math.sin(theta) + 0.006};
    }

    public static double[] bd09ToGcj02(double lng, double lat) {
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        return new double[]{z * Math.cos(theta), z * Math.sin(theta)};
    }

    // wgs84坐标转到GCJ02坐标
    public static double[] wgs84ToGcj02(double lng, double lat) {
        double[] tmp = delta(lng, lat);
        return new double[]{lng + tmp[0], lat + tmp[1]};
    }

    public static double[] gcj02ToWgs84(double lng, double lat) {
        double[] tmp = delta(lng, lat);
        return new double[]{lng - tmp[0], lat - tmp[1]};

    }

    public static double[] gcj02ToWgs84Ex(double gcjLng, double gcjLat) {
        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta, dLng = initDelta;
        double mLat = gcjLat - dLat, mLng = gcjLng - dLng;
        double pLat = gcjLat + dLat, pLng = gcjLng + dLng;
        double wgsLat = 0, wgsLng = 0;
        int i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLng = (mLng + pLng) / 2;
            double[] tmp = delta(wgsLat, wgsLng);
            dLng = wgsLng + tmp[0] - gcjLng;
            dLat = wgsLat + tmp[1] - gcjLat;
            if (Math.abs(dLat) < threshold && Math.abs(dLng) < threshold) {
                break;
            }
            if (dLat > 0) {
                pLat = wgsLat;
            } else {
                mLat = wgsLat;
            }
            if (dLng > 0) {
                pLng = wgsLng;
            } else {
                mLng = wgsLng;
            }
            if (++i > 10000) {
                break;
            }
        }

        return new double[]{wgsLng, wgsLat};
    }


    private static double[] delta(double lng, double lat) {
        double a = 6378245.0;
        double ee = 0.00669342162296594323;
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLon(lng - 105.0, lat - 35.0);

        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        return new double[]{dLng, dLat};
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
}
