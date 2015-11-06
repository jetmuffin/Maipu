package edu.hhu.jetmuffin.model;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * Created by JetMuffin on 15/11/5.
 */
public class Point {

    private double x;
    private double y;

    public Point(){
        super();
    }

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point(String coordinate){
        if(coordinate.contains(",")){
            String splits[] = coordinate.split(",");
            this.x = Double.parseDouble(splits[1]);
            this.y = Double.parseDouble(splits[0]);
        }else if(coordinate.contains("，")) {
            String splits[] = coordinate.split("，");
            this.x = Double.parseDouble(splits[1]);
            this.y = Double.parseDouble(splits[0]);
        }else{
                this.x = this.y = 0;
        }
    }

    public static LatLng latLng(Point point){
       return new LatLng(point.x, point.y);
    }

    public static LatLng middle(LatLng from, LatLng to){
        return new LatLng((from.latitude+to.latitude)/2, (to.longitude+from.longitude)/2);
    }

    public static LatLng middle(Point from, Point to){
        return new LatLng((from.getX()+to.getX())/2, (to.getY()+from.getY())/2);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
