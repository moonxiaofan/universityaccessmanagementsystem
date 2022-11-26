package edu.sau.universityaccessmanagementsystem.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.sau.universityaccessmanagementsystem.dao.RistPlaceMapper;
import edu.sau.universityaccessmanagementsystem.dao.SettingMapper;
import edu.sau.universityaccessmanagementsystem.entity.RiskPlace;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 16:33
 * @Version 1.0
 */
@Service
public class RistPlaceService {

    @Autowired
    private RistPlaceMapper ristPlaceMapper;

    @Autowired
    private SettingMapper settingMapper;

    public RiskPlace findByPlace(String place) {
        return ristPlaceMapper.findByPlace(place);
    }

    public Object updateRistPlace() {
        try {
            JSONObject data = JSONObject.parseObject(CommonUtil.loadJson("https://api.risk-region.ml/latest.json"));
            JSONArray highlist = ((JSONObject) data.get("data")).getJSONArray("highlist");
            JSONArray lowlist = ((JSONObject) data.get("data")).getJSONArray("lowlist");
            settingMapper.updateRistPlaceUpdateTime(((JSONObject) data.get("data")).getString("end_update_time"));
            RiskPlace riskPlace = new RiskPlace();
            ristPlaceMapper.delete();
            for(int i = 0; i < highlist.size(); i ++ ) {
                JSONObject highPlace = (JSONObject) highlist.get(i);
                riskPlace.setPlace(highPlace.getString("area_name"));
                riskPlace.setType(2);
                ristPlaceMapper.insert(riskPlace);
            }
            for(int i = 0; i < lowlist.size(); i ++ ) {
                JSONObject lowPlace = (JSONObject) lowlist.get(i);
                riskPlace.setPlace(lowPlace.getString("area_name"));
                riskPlace.setType(1);
                ristPlaceMapper.insert(riskPlace);
            }
            return CommonUtil.successMessageMap();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorMessageMap("获取风险地区失败");
        }
    }

    public Object getRistPlace() {
        HashMap<String, Object> map = new HashMap<>();
        List<RiskPlace> riskPlaces = ristPlaceMapper.getRiskPlace();
        map.put("riskPlaces", riskPlaces);
        return map;
    }
}
