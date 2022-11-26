package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.ApplyMapper;
import edu.sau.universityaccessmanagementsystem.dao.ApprovalMapper;
import edu.sau.universityaccessmanagementsystem.dao.RoleMapper;
import edu.sau.universityaccessmanagementsystem.entity.*;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import edu.sau.universityaccessmanagementsystem.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/20 0:38
 * @Version 1.0
 */
@Service
public class ApprovalService {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ApprovalMapper approvalMapper;

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private RoleMapper roleMapper;


    public Object getApprovalUserInfo(User searchUser) {
        HashMap<String, Object> map = new HashMap<>();
        User user = hostHolder.getUser();
        List<ApplyResponseVo> applyList = applyMapper.getApplyUserInfo(user.getId(), searchUser.getName());
        map.put("applyList", applyList);
        return map;
    }

    public Object updateApprovalInfo(Apply apply) {
        try {
            User user = hostHolder.getUser();
            applyMapper.updateApplyStatus(apply);
            ApprovalLog approvalLog = new ApprovalLog();
            approvalLog.setApprovalTime(new Date());
            approvalLog.setApplyId(apply.getId());
            approvalLog.setApprovalUserId(user.getId());
            approvalMapper.insert(approvalLog);
            return CommonUtil.successMessageMap("审批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonUtil.errorMessageMap("审批失败");
        }

    }
}
