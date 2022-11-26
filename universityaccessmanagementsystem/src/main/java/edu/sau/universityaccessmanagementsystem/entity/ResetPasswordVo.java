package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/18 21:08
 * @Version 1.0
 */
@Data
public class ResetPasswordVo {
    private String oldPass;
    private String newPass;
}
