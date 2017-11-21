package fxjzzyo.com.sspkudormselection.Constant;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by fxjzzyo on 2017/11/22.
 */

public class Data implements Serializable {
    Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Data{" +
                "map=" + map +
                '}';
    }
}
