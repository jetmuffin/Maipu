package edu.hhu.jetmuffin.map;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hhu.jetmuffin.model.Point;

/**
 * Created by JetMuffin on 15/11/5.
 */
public class MapUtil {
    public static Map<String, String> data;

    public static Map<String, String> getData(){
            data = new HashMap<String, String>();
        data.put("北京","116.41667,39.91667");
        data.put("Beijing","116.41667,39.91667");
        data.put("上海","121.48023,31.23630");
        data.put("shanghai","121.43333,34.50000");
        data.put("天津","117.20000,39.13333");
        data.put("Tianjin","117.20000,39.13333");
        data.put("香港","114.10000,22.20000");
        data.put("Xianggang","114.10000,22.20000");
        data.put("广州","113.23333,23.16667");
        data.put("Guangzhou","113.23333,23.16667");
        data.put("珠海","113.51667,22.30000");
        data.put("Zhuhai","113.51667,22.30000");
        data.put("深圳","114.06667,22.61667");
        data.put("Shenzhen","114.06667,22.61667");
        data.put("杭州","120.20000,30.26667");
        data.put("Hangzhou","120.20000,30.26667");
        data.put("重庆","106.45000,29.56667");
        data.put("Chongqin","106.45000,29.56667");
        data.put("青岛","120.33333,36.06667");
        data.put("Qingdao","120.33333,36.06667");
        data.put("厦门","118.10000,24.46667");
        data.put("Xiamen","118.10000,24.46667");
        data.put("福州","119.30000,26.08333");
        data.put("Fuzhou","119.30000,26.08333");
        data.put("兰州","103.73333,36.03333");
        data.put("Lanzhou","103.73333,36.03333");
        data.put("贵阳","106.71667,26.56667");
        data.put("Guiyang","106.71667,26.56667");
        data.put("长沙","113.00000,28.21667");
        data.put("Changsha","113.00000,28.21667");
        data.put("南京","118.78333,32.05000");
        data.put("Nanjing","118.78333,32.05000");
        data.put("南昌","115.90000,28.68333");
        data.put("Nanchang","115.90000,28.68333");
        data.put("沈阳","123.38333,41.80000");
        data.put("Shenyang","123.38333,41.80000");
        data.put("太原", "112.53333,37.86667");
        data.put("Taiyuan", "112.53333,37.86667");
        data.put("成都", "104.06667,30.66667");
        data.put("Chengdu", "104.06667,30.66667");
        data.put("拉萨", "91.00000,29.60000");
        data.put("Lasa", "91.00000,29.60000");
        data.put("乌鲁木齐", "87.68333,43.76667");
        data.put("Wulumiqi", "87.68333,43.76667");
        data.put("昆明", "102.73333,25.05000");
        data.put("Kunming", "102.73333,25.05000");
        data.put("西安", "108.95000,34.26667");
        data.put("Xian", "108.95000,34.26667");
        data.put("西宁", "101.75000,36.56667");
        data.put("Xining", "101.75000,36.56667");
        data.put("银川", "106.26667,38.46667");
        data.put("Yinchuan", "106.26667,38.46667");
        data.put("呼和浩特", "122.08333,46.06667");
        data.put("Huhehaote", "122.08333,46.06667");
        data.put("哈尔滨", "126.63333,45.75000");
        data.put("Haerbin", "126.63333,45.75000");
        data.put("长春", "125.35000,43.88333");
        data.put("Changchun", "125.35000,43.88333");
        data.put("武汉", "114.31667,30.51667");
        data.put("Wuhan", "114.31667,30.51667");
        data.put("郑州", "113.65000,34.76667");
        data.put("Zhengzhou", "113.65000,34.76667");
        data.put("石家庄", "114.48333,38.03333");
        data.put("Shijiazhuang", "114.48333,38.03333");
        data.put("三亚", "109.50000,18.20000");
        data.put("Sanya", "109.50000,18.20000");
        data.put("海口", "110.35000,20.01667");
        data.put("Haikou", "110.35000,20.01667");
        data.put("澳门", "113.50000,22.20000");
        data.put("Aomen", "113.50000,22.20000");

        return data;
    }

    public static Point parse(String postion){
        Map<String, String> data = getData();
        Point point;
        if (hasDigit(postion)) {
            point = new Point(postion);
        } else {
            String coordinate = data.get(postion);
            if(coordinate != null)
                point = new Point(coordinate);
            else
                point = new Point(0,0);
        }
        return point;
    }

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
    }
}
