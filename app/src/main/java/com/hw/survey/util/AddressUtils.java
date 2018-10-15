package com.hw.survey.util;

/**
 * Created by haowang on 2018/5/8.
 */

public class AddressUtils {
    public static double[] getAddressFromString(String address){
        double[] result = new double[2];
        result[0] = 0;
        result[1] = 0;

        if(address != null && address.startsWith("x=") && address.indexOf("  y=") > 1){
            //"x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat")
            int i = address.indexOf("  y=");
            String lng = address.substring(2,i);
            String Iat = address.substring(i + 4);
            result[0] = Double.valueOf(lng);
            result[1] = Double.valueOf(Iat);
        }

        return result;
    }
}
