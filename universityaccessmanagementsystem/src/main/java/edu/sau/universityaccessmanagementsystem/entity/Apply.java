package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 14:23
 * @Version 1.0
 */
@Data
public class Apply {
    private int id;
    private int userId;
    private int academyId;
    private String email;
    private String phone;
    private String toPlace;
    private int type;
    private int approvalUserId;
    private Date startDate;
    private Date endDate;
    private String reason;
    private int status;
}
