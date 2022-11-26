package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/20 13:12
 * @Version 1.0
 */
@Data
public class ApprovalLog {
    private int id;
    private int applyId;
    private int approvalUserId;
    private Date approvalTime;
}
