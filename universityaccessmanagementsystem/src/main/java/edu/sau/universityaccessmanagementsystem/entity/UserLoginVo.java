package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 21:07
 * @Version 1.0
 */
@Data
public class UserLoginVo {
    private String username;
    private String password;
    private String code;
}
