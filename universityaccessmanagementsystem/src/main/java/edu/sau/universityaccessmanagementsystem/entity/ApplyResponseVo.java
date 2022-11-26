package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 23:45
 * @Version 1.0
 */
@Data
public class ApplyResponseVo {
    private int id;
    private int userId;
    private String name;
    private int academyId;
    private String academyName;
    private String email;
    private String phone;
    private String toPlace;
    private int type;
    private int approvalUserId;
    private String approvalUserName;
    private Date startDate;
    private Date endDate;
    private String reason;
    private int status;
}
