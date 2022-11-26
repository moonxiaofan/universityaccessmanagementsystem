package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 3:02
 * @Version 1.0
 */
@Data
public class UserSearchResponseVo {
    private int id;
    private String username;
    private String name;
    private String beizhu;
    private String email;
    private String phone;
    private String academyName;
    private int academyId;
    private String status;
}
