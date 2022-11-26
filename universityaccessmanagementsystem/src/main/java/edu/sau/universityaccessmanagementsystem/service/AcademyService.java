package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.AcademyMapper;
import edu.sau.universityaccessmanagementsystem.entity.Academy;
import edu.sau.universityaccessmanagementsystem.entity.User;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 17:20
 * @Version 1.0
 */
@Service
public class AcademyService {

    @Autowired
    private AcademyMapper academyMapper;

    public Object insertAcademy(Academy academy) {
        HashMap<String, String> map = new HashMap<>();
        if(academy == null) {
            return CommonUtil.errorMessageMap("参数不能为空");
        }
        if(StringUtils.isBlank(academy.getAcademyId())) {
            return CommonUtil.errorMessageMap("学院编号不能为空");
        }
        if(StringUtils.isBlank(academy.getAcademyName())) {
            return CommonUtil.errorMessageMap("学院名称不能为空");
        }
        map.put("result", "success");
        academyMapper.insertAcademy(academy);
        return map;
    }

    public Object getAllList() {
        HashMap<String, Object> map = new HashMap<>();
        List<Academy> academyList = academyMapper.getAllList();
        map.put("academyList", academyList);
        return map;
    }

    public Object editAcademy(Academy academy) {
        Academy oldAcademy = academyMapper.findById(academy.getId());
        if(oldAcademy == null) {
            academy.setStatus(0);
            academyMapper.insertAcademy(academy);
            return CommonUtil.successMessageMap("新增学院信息成功");
        } else {
            academyMapper.updateAcademy(academy);
            return CommonUtil.successMessageMap("修改学院信息成功");
        }
    }
}
