package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.ApprovalLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/20 0:36
 * @Version 1.0
 */
@Mapper
public interface ApprovalMapper {
    void insert(ApprovalLog approvalLog);
}
