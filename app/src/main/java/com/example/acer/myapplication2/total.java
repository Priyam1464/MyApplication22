package com.example.acer.myapplication2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by acer on 8/26/2016.
 */

public class total {
    String uri;
    int key;

    public total(String uri, int key) {
        this.uri = uri;
        this.key = key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uri", uri);
        result.put("key", key);
        return result;
    }

}
