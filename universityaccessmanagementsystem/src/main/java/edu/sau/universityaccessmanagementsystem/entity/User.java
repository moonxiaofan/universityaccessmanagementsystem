package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 20:27
 * @Version 1.0
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String name;
    private Date lastLogin;
    private String ip;
    private String status;
    private String beizhu;
    private String email;
    private String phone;
    private Integer academyId;
    private Integer roleId;
}
