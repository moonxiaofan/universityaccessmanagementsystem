package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.Setting;
import edu.sau.universityaccessmanagementsystem.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 19:49
 * @Version 1.0
 */
@RestController
@RequestMapping("setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("getSetting")
    private Object getSetting() {
        return settingService.getSetting();
    }

    @PostMapping("updateCheckType")
    private Object updateCheckType(@RequestBody Setting setting) {
        return settingService.updateCheckType(setting);
    }

}
