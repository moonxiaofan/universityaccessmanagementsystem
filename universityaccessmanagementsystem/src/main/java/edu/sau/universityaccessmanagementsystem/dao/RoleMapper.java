package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/16 20:57
 * @Version 1.0
 */
@Mapper
public interface RoleMapper {
    Role findById(int id);

    List<Role> getAllList();

    void insertRole(Role role);

    void updateRole(Role role);
}
