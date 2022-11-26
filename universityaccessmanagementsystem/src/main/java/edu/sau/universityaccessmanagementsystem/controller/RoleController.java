package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.Academy;
import edu.sau.universityaccessmanagementsystem.entity.Role;
import edu.sau.universityaccessmanagementsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 1:50
 * @Version 1.0
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("getAllList")
    private Object getAllList() {
        return roleService.getAllList();
    }

    @PostMapping("edit")
    private Object editRole(@RequestBody Role role) {
        return roleService.editRole(role);
    }
}
