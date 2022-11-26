package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.RoleMapper;
import edu.sau.universityaccessmanagementsystem.entity.Academy;
import edu.sau.universityaccessmanagementsystem.entity.Role;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/16 21:50
 * @Version 1.0
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Role findById(int id) {
        return roleMapper.findById(id);
    }

    public Object getAllList() {
        HashMap<String, Object> map = new HashMap<>();
        List<Role> roleList = roleMapper.getAllList();
        map.put("roleList", roleList);
        return map;
    }


    public Object editRole(Role role) {
        Role oldRole = roleMapper.findById(role.getId());
        if(oldRole == null) {
            role.setStatus(0);
            roleMapper.insertRole(role);
            return CommonUtil.successMessageMap("新增角色信息成功");
        } else {
            roleMapper.updateRole(role);
            return CommonUtil.successMessageMap("修改角色信息成功");
        }
    }
}
