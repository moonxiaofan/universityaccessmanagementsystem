package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.Apply;
import edu.sau.universityaccessmanagementsystem.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 14:29
 * @Version 1.0
 */
@RestController
@RequestMapping("apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @PostMapping("addApplyInfo")
    public Object addApplyInfo(@RequestBody Apply apply) {
        return applyService.addApplyInfo(apply);
    }

    @GetMapping("getOwnerApplyInfo")
    public Object getOwnerApplyInfo() {
        return applyService.getOwnerApplyInfo();
    }

    @PostMapping("getRoundApplyInfoNum")
    public Object getRoundApplyInfoNum(@RequestBody(required = false) Apply apply) {
        return applyService.getRoundApplyInfoNum(apply);
    }

}
