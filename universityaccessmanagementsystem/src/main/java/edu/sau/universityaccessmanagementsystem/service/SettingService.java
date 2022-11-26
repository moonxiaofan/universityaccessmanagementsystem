package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.SettingMapper;
import edu.sau.universityaccessmanagementsystem.entity.Setting;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 19:49
 * @Version 1.0
 */
@Service
public class SettingService {

    @Autowired
    private SettingMapper settingMapper;


    public Object getSetting() {
        HashMap<String, Object> map = new HashMap<>();
        Setting setting = settingMapper.getSetting();
        map.put("setting", setting);
        return map;
    }

    public Object updateCheckType(Setting setting) {
        settingMapper.updateCheckType(setting);
        return CommonUtil.successMessageMap();
    }
}
