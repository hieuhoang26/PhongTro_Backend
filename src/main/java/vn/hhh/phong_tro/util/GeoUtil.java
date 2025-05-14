package vn.hhh.phong_tro.util;


import ch.hsr.geohash.GeoHash;

import java.util.HashSet;
import java.util.Set;

public class GeoUtil {
    //    public static String generateGeoHash(double latitude, double longitude) {
//        return GeoHash.encodeHash(latitude, longitude, 9);
//    }
    public static Set<String> getGeoHashSearchAreas(double latitude, double longitude, int precision) {
        GeoHash center = GeoHash.withCharacterPrecision(latitude, longitude, precision);
        Set<String> hashes = new HashSet<>();
        hashes.add(center.toBase32());
        for (GeoHash neighbor : center.getAdjacent()) {
            hashes.add(neighbor.toBase32());
        }
        return hashes;
    }

}
