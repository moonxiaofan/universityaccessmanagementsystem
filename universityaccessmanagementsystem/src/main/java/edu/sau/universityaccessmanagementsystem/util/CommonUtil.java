package edu.sau.universityaccessmanagementsystem.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 25);
        System.out.println(getJSONString(0, "ok", map));
    }

    //错误信息返回
    public static HashMap<String, String> errorMessageMap(String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "error");
        map.put("message", message);
        return map;
    }

    //正确信息返回
    public static HashMap<String, String> successMessageMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "success");
        return map;
    }

    //正确信息返回
    public static HashMap<String, String> successMessageMap(String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "success");
        map.put("message", message);
        return map;
    }

    public static String loadJson (String url) {
        StringBuilder json = new StringBuilder();
        try {
            //链接URL
            URL urlObject = new URL(url);
            //创建连接
            URLConnection uc = urlObject.openConnection();
            uc.setRequestProperty("User-Agent", UserAgentUtil.randomUserAgent());
            uc.setDoOutput(true);
            uc.setDoInput(true);
            uc.setRequestProperty("contentType", "UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }


}
