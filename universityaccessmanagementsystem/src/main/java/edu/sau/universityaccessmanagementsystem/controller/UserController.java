package edu.sau.universityaccessmanagementsystem.controller;

import edu.sau.universityaccessmanagementsystem.entity.*;
import edu.sau.universityaccessmanagementsystem.service.RoleService;
import edu.sau.universityaccessmanagementsystem.service.UserService;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import edu.sau.universityaccessmanagementsystem.util.Constant;
import edu.sau.universityaccessmanagementsystem.util.CookieUtil;
import edu.sau.universityaccessmanagementsystem.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 20:23
 * @Version 1.0
 */
@RestController
@RequestMapping("user")
public class UserController implements Constant {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("insert")
    private Object insertUser(@RequestBody User userRegisterVo) {
        return userService.insertUser(userRegisterVo);
    }

    /**
     * @Description: 编辑审批人员
     * @param userSearchResponseVo
     * @return: java.lang.Object
     * @Author: sxf
     * @Date: 2022/11/19 3:59
     */
    @PostMapping("editApproverUser")
    private Object editApproverUser(@RequestBody UserSearchResponseVo userSearchResponseVo) {
        return userService.editApproverUser(userSearchResponseVo);
    }

    /**
     * @Description: 编辑教师
     * @param userSearchResponseVo
     * @return: java.lang.Object
     * @Author: sxf
     * @Date: 2022/11/19 13:53
     */
    @PostMapping("editTeacherUser")
    private Object editTeacherUser(@RequestBody UserSearchResponseVo userSearchResponseVo) {
        return userService.editTeacherUser(userSearchResponseVo);
    }

    /**
     * @Description: 编辑学生
     * @param userSearchResponseVo
     * @return: java.lang.Object
     * @Author: sxf
     * @Date: 2022/11/19 13:59
     */
    @PostMapping("editStudentUser")
    private Object editStudentUser(@RequestBody UserSearchResponseVo userSearchResponseVo) {
        return userService.editStudentUser(userSearchResponseVo);
    }

    @PostMapping("login")
    private Object login(@RequestBody UserLoginVo userLoginVo, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String kaptchaOwner = null;
        kaptchaOwner = CookieUtil.getValue(httpServletRequest, "kaptchaOwner");
        if(kaptchaOwner == null) {
            return CommonUtil.errorMessageMap("验证码不能为空");
        }
        HashMap<String, String> map = userService.login(userLoginVo, kaptchaOwner, httpServletRequest);
        if(map.get("result").equals("success")) {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                cookie.setMaxAge(EXPIRED_SECONDS);
                response.addCookie(cookie);
                return map;
            } else {
                return CommonUtil.errorMessageMap("登录失败");
            }
        } else {
            return map;
        }
    }

    @PostMapping(value = "/getRoleByTicket")
    public Object getRoleByTicket(@RequestBody String ticket, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        LoginTicket loginTicket = userService.findLoginTicket(ticket);
        //检查凭证是否有效
        if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
            User user = userService.findById(loginTicket.getUserId());
            map.put("result", "success");
            map.put("role", roleService.findById(user.getRoleId()).getRoleName());
            map.put("user", user);
            //更新用户登录信息
            user.setIp(request.getRemoteAddr());
            user.setLastLogin(new Date());
            userService.updatetUser(user);
            return map;
        }
        return CommonUtil.errorMessageMap("无效凭证");
    }

    @PostMapping("logout")
    public void logout(@RequestBody String ticket) {
        userService.logout(ticket);
    }

    @SuppressWarnings("all")
    @PostMapping("resetPassword")
    public Object resetPassword(@RequestBody ResetPasswordVo resetPasswordVo) {
        User user = hostHolder.getUser();
        String checkPassword = CommonUtil.md5(resetPasswordVo.getOldPass() + user.getSalt());
        if(checkPassword.equals(user.getPassword())) {
            user.setPassword(CommonUtil.md5(resetPasswordVo.getNewPass() + user.getSalt()));
            userService.updateUser(user);
            return CommonUtil.successMessageMap();
        } else {
            return CommonUtil.errorMessageMap("原密码错误");
        }
    }

    @PostMapping("updateUser")
    public Object updateUser(@RequestBody User user) {
        User oldUser = hostHolder.getUser();
        if(StringUtils.isNotBlank(user.getName())) {
            oldUser.setName(user.getName());
        }
        if(StringUtils.isNotBlank(user.getEmail())) {
            oldUser.setEmail(user.getEmail());
        }
        if(StringUtils.isNotBlank(user.getPhone())) {
            oldUser.setPhone(user.getPhone());
        }
        if(StringUtils.isNotBlank(user.getBeizhu())) {
            oldUser.setBeizhu(user.getBeizhu());
        }
        userService.updateUser(oldUser);
        return CommonUtil.successMessageMap();
    }


    /**
     * @Description:
     * @param null
     * @return: 获取审批人员详细信息
     * @Author: sxf
     * @Date: 2022/11/19 2:37
     */
    @PostMapping("getAllApproverList")
    public Object getAllApproverList(@RequestBody(required = false) UserSearchVo userSearchVo) {
        return userService.getAllApproverList(userSearchVo);
    }

    /**
     * @Description: 获取教师人员信息
     * @param null
     * @return:
     * @Author: sxf
     * @Date: 2022/11/19 13:51
     */
    @PostMapping("getAllTeacherList")
    public Object getAllTeacherList(@RequestBody(required = false) UserSearchVo userSearchVo) {
        return userService.getAllTeacherList(userSearchVo);
    }

    /**
     * @Description: 获取学生人员信息
     * @param userSearchVo
     * @return: java.lang.Object
     * @Author: sxf
     * @Date: 2022/11/19 14:00
     */
    @PostMapping("getAllStudentList")
    public Object getAllStudentList(@RequestBody(required = false) UserSearchVo userSearchVo) {
        return userService.getAllStudentList(userSearchVo);
    }

}
