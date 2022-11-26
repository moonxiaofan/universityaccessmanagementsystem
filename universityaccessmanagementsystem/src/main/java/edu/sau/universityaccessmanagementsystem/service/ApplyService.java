package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.ApplyMapper;
import edu.sau.universityaccessmanagementsystem.dao.RoleMapper;
import edu.sau.universityaccessmanagementsystem.dao.SettingMapper;
import edu.sau.universityaccessmanagementsystem.entity.*;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import edu.sau.universityaccessmanagementsystem.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 14:28
 * @Version 1.0
 */
@Service
public class ApplyService {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SettingMapper settingMapper;

    @Autowired
    private RistPlaceService ristPlaceService;

    @Autowired
    private ApplyMapper applyMapper;

    public Object addApplyInfo(Apply apply) {
        User user = hostHolder.getUser();
        apply.setUserId(user.getId());
        if(!roleMapper.findById(user.getRoleId()).getRoleName().equals("学生")) {
            Setting setting = settingMapper.getSetting();
            if(setting.getCheckType() == 0) {
                RiskPlace riskPlace = ristPlaceService.findByPlace(apply.getToPlace());
                if(riskPlace == null) {
                    apply.setStatus(2);
                    applyMapper.addApplyInfo(apply);
                    return CommonUtil.successMessageMap("自动审批通过");
                }
            }
        }
        applyMapper.addApplyInfo(apply);
        return CommonUtil.successMessageMap("等待审批通过");
    }

    public Object getOwnerApplyInfo() {
        HashMap<String, Object> map = new HashMap<>();
        User user = hostHolder.getUser();
        List<ApplyResponseVo> applyList = applyMapper.getOwnerApplyInfo(user.getId());
        map.put("applyList", applyList);
        return map;
    }

    public Object getRoundApplyInfoNum(Apply apply) {
        HashMap<String, Object> map = new HashMap<>();
        User user = hostHolder.getUser();
        List<ApplyResponseVo> applyList = new ArrayList<>();
        if(apply != null) {
            applyList = applyMapper.getRoundApplyInfoNum(user.getAcademyId(), apply.getStartDate(), apply.getEndDate());
        } else {
            applyList = applyMapper.getAllRoundApplyInfoNum(user.getAcademyId());
        }
        map.put("applyList", applyList);
        return map;
    }
}
