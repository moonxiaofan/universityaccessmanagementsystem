package edu.sau.universityaccessmanagementsystem.entity;

import lombok.Data;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 17:04
 * @Version 1.0
 */
@Data
public class Academy {
    private Integer id;
    private String academyId;
    private String academyName;
    private String academyInfo;
    private Integer status;
}
