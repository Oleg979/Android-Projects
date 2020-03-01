package com.olso0919.localizationapplication;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ConversionService {
    private static Map<Units, Double> unitsToMeter = new HashMap<>();
    static {
        unitsToMeter.put(Units.KM, 1000.0);
        unitsToMeter.put(Units.M, 1.0);
        unitsToMeter.put(Units.CM, 1.0/100.0);
        unitsToMeter.put(Units.FT, 0.3048);
        unitsToMeter.put(Units.ML, 1609.34);
        unitsToMeter.put(Units.INCH, 0.0254);
    }
    static double convert(double val, Units from, Units to) {
        Log.i("Conversion Service", "Converting from " + from + " to " + to);
        if(from == to) return val;
        return val * unitsToMeter.get(from) * 1.0 / unitsToMeter.get(to);
    }
    static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value *= factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
