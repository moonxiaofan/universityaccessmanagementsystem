package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.Apply;
import edu.sau.universityaccessmanagementsystem.entity.Role;
import edu.sau.universityaccessmanagementsystem.entity.User;
import edu.sau.universityaccessmanagementsystem.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/20 0:36
 * @Version 1.0
 */
@RestController
@RequestMapping("approval")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("getApprovalUserInfo")
    public Object getApprovalUserInfo(@RequestBody User searchUser) {
        return approvalService.getApprovalUserInfo(searchUser);
    }

    @PostMapping("updateApprovalInfo")
    public Object updateApprovalInfo(@RequestBody Apply apply) {
        return approvalService.updateApprovalInfo(apply);
    }
}
