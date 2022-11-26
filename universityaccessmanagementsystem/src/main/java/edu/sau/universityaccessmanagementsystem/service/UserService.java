package edu.sau.universityaccessmanagementsystem.service;

import edu.sau.universityaccessmanagementsystem.dao.RoleMapper;
import edu.sau.universityaccessmanagementsystem.dao.UserMapper;
import edu.sau.universityaccessmanagementsystem.entity.*;
import edu.sau.universityaccessmanagementsystem.util.CommonUtil;
import edu.sau.universityaccessmanagementsystem.util.Constant;
import edu.sau.universityaccessmanagementsystem.util.HostHolder;
import edu.sau.universityaccessmanagementsystem.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 20:23
 * @Version 1.0
 */
@Service
public class UserService implements Constant{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HostHolder hostHolder;

    public void updateUser(User user) {
        userMapper.updatetUser(user);
    }

    public Object insertUser(User userRegisterVo) {
        User user = new User();
        HashMap<String, String> map = new HashMap<>();
        if(userRegisterVo == null) {
            return CommonUtil.errorMessageMap("参数不能为空");
        }
        if(StringUtils.isBlank(userRegisterVo.getUsername())) {
            return CommonUtil.errorMessageMap("用户名不能为空");
        }
        if(StringUtils.isBlank(userRegisterVo.getPassword())) {
            return CommonUtil.errorMessageMap("密码不能为空");
        }
        if(userRegisterVo.getRoleId() == null) {
            return CommonUtil.errorMessageMap("应设置角色");
        }
        if(userRegisterVo.getAcademyId() == null) {
            return CommonUtil.errorMessageMap("应设置所属学院");
        }
        User oldUser = userMapper.findByUserName(userRegisterVo.getUsername());
        if(oldUser != null) {
            return CommonUtil.errorMessageMap("用户名已存在");
        }
        user.setUsername(userRegisterVo.getUsername());
        String salt = CommonUtil.generateUUID().substring(0, 5);
        user.setSalt(salt);
        user.setPassword(CommonUtil.md5(userRegisterVo.getPassword() + salt));
        user.setName(userRegisterVo.getName());
        user.setStatus("0");
        userMapper.insertUser(user);
        map.put("result", "success");
        return map;
    }

    public HashMap<String, String> login(UserLoginVo userLoginVo, String kaptchaOwner, HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        if(userLoginVo == null) {
            return CommonUtil.errorMessageMap("参数不能为空");
        }
        if(StringUtils.isBlank(userLoginVo.getUsername())) {
            return CommonUtil.errorMessageMap("用户名不能为空");
        }
        if(StringUtils.isBlank(userLoginVo.getPassword())) {
            return CommonUtil.errorMessageMap("密码不能为空");
        }
        if(StringUtils.isBlank(userLoginVo.getCode())) {
            return CommonUtil.errorMessageMap("验证码不能为空");
        }
        //验证验证码
        String kaptcha = null;
        if(StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(userLoginVo.getCode()) || !kaptcha.equalsIgnoreCase(userLoginVo.getCode())) {
            return CommonUtil.errorMessageMap("验证码不正确");
        }
        //验证账号密码
        User user = userMapper.findByUserName(userLoginVo.getUsername());
        if(user == null) {
            return CommonUtil.errorMessageMap("账号不存在");
        }
        String password = CommonUtil.md5(userLoginVo.getPassword() + user.getSalt());
        if(!password.equals(user.getPassword())) {
            return CommonUtil.errorMessageMap("密码错误");
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommonUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + (Long.parseLong(String.valueOf(EXPIRED_SECONDS)) * 1000))); //设置过期时间
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());
        map.put("result", "success");
        //存入角色
        Role role = roleMapper.findById(user.getRoleId());
        map.put("role", role.getRoleName());
        //更新用户登录信息
        user.setIp(request.getRemoteAddr());
        user.setLastLogin(new Date());
        userMapper.updatetUser(user);
        return map;
    }

    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    public User findById(int userId) {
        return userMapper.findById(userId);
    }

    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public void updatetUser(User user) {
        userMapper.updatetUser(user);
    }

    public Object getAllApproverList(UserSearchVo userSearchVo) {
        HashMap<String, Object> map = new HashMap<>();
        if(userSearchVo == null) userSearchVo = new UserSearchVo();
        User user = hostHolder.getUser();
        if(!roleMapper.findById(user.getRoleId()).getRoleName().equals("管理员")) {
            userSearchVo.setAcademyId(user.getAcademyId());
        }
        userSearchVo.setRoleId(2);
        List<UserSearchResponseVo> approverList = userMapper.findApproverByUserSearchVo(userSearchVo);
        map.put("approverList", approverList);
        return map;
    }

    public Object editApproverUser(UserSearchResponseVo userSearchResponseVo) {
        if(userSearchResponseVo == null) return CommonUtil.errorMessageMap("参数不能为空");
        if(StringUtils.isBlank(userSearchResponseVo.getUsername())) return CommonUtil.errorMessageMap("用户名不能为空");
        if(userSearchResponseVo.getAcademyId() == 0) return CommonUtil.errorMessageMap("所属学院不能为空");
        User user = userMapper.findById(userSearchResponseVo.getId());
        if(user == null) {
            User newUser = new User();
            newUser.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                newUser.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                newUser.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                newUser.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                newUser.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                newUser.setPhone(userSearchResponseVo.getPhone());
            }
            newUser.setStatus("0");
            newUser.setSalt(CommonUtil.generateUUID().substring(0, 5));
            newUser.setPassword(CommonUtil.md5(newUser.getUsername() + newUser.getSalt()));
            newUser.setRoleId(2);
            userMapper.insertUser(newUser);
            return CommonUtil.successMessageMap("新增审批人员成功");
        } else {
            user.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                if(!user.getUsername().equals(userSearchResponseVo.getUsername())) {
                    user.setPassword(CommonUtil.md5(userSearchResponseVo.getUsername() + user.getSalt()));
                }
                user.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                user.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                user.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                user.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                user.setPhone(userSearchResponseVo.getPhone());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getStatus())) {
                user.setStatus(userSearchResponseVo.getStatus());
            }
            userMapper.updatetUser(user);
            return CommonUtil.successMessageMap("修改审批人员成功");
        }
    }

    public Object getAllTeacherList(UserSearchVo userSearchVo) {
        HashMap<String, Object> map = new HashMap<>();
        if(userSearchVo == null) userSearchVo = new UserSearchVo();
        User user = hostHolder.getUser();
        if(!roleMapper.findById(user.getRoleId()).getRoleName().equals("管理员")) {
            userSearchVo.setAcademyId(user.getAcademyId());
        }
        userSearchVo.setRoleId(3);
        List<UserSearchResponseVo> approverList = userMapper.findApproverByUserSearchVo(userSearchVo);
        map.put("approverList", approverList);
        return map;
    }

    public Object editTeacherUser(UserSearchResponseVo userSearchResponseVo) {
        if(userSearchResponseVo == null) return CommonUtil.errorMessageMap("参数不能为空");
        if(StringUtils.isBlank(userSearchResponseVo.getUsername())) return CommonUtil.errorMessageMap("用户名不能为空");
        if(userSearchResponseVo.getAcademyId() == 0) return CommonUtil.errorMessageMap("所属学院不能为空");
        User user = userMapper.findById(userSearchResponseVo.getId());
        if(user == null) {
            User newUser = new User();
            newUser.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                newUser.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                newUser.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                newUser.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                newUser.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                newUser.setPhone(userSearchResponseVo.getPhone());
            }
            newUser.setStatus("0");
            newUser.setSalt(CommonUtil.generateUUID().substring(0, 5));
            newUser.setPassword(CommonUtil.md5(newUser.getUsername() + newUser.getSalt()));
            newUser.setRoleId(3);
            userMapper.insertUser(newUser);
            return CommonUtil.successMessageMap("新增教师人员成功");
        } else {
            user.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                if(!user.getUsername().equals(userSearchResponseVo.getUsername())) {
                    user.setPassword(CommonUtil.md5(userSearchResponseVo.getUsername() + user.getSalt()));
                }
                user.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                user.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                user.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                user.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                user.setPhone(userSearchResponseVo.getPhone());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getStatus())) {
                user.setStatus(userSearchResponseVo.getStatus());
            }
            userMapper.updatetUser(user);
            return CommonUtil.successMessageMap("修改教师人员成功");
        }
    }

    public Object editStudentUser(UserSearchResponseVo userSearchResponseVo) {
        if(userSearchResponseVo == null) return CommonUtil.errorMessageMap("参数不能为空");
        if(StringUtils.isBlank(userSearchResponseVo.getUsername())) return CommonUtil.errorMessageMap("用户名不能为空");
        if(userSearchResponseVo.getAcademyId() == 0) return CommonUtil.errorMessageMap("所属学院不能为空");
        User user = userMapper.findById(userSearchResponseVo.getId());
        if(user == null) {
            User newUser = new User();
            newUser.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                newUser.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                newUser.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                newUser.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                newUser.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                newUser.setPhone(userSearchResponseVo.getPhone());
            }
            newUser.setStatus("0");
            newUser.setSalt(CommonUtil.generateUUID().substring(0, 5));
            newUser.setPassword(CommonUtil.md5(newUser.getUsername() + newUser.getSalt()));
            newUser.setRoleId(4);
            userMapper.insertUser(newUser);
            return CommonUtil.successMessageMap("新增教师人员成功");
        } else {
            user.setAcademyId(userSearchResponseVo.getAcademyId());
            if(StringUtils.isNotBlank(userSearchResponseVo.getUsername())) {
                if(!user.getUsername().equals(userSearchResponseVo.getUsername())) {
                    user.setPassword(CommonUtil.md5(userSearchResponseVo.getUsername() + user.getSalt()));
                }
                user.setUsername(userSearchResponseVo.getUsername());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getBeizhu())) {
                user.setBeizhu(userSearchResponseVo.getBeizhu());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getName())) {
                user.setName(userSearchResponseVo.getName());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getEmail())) {
                user.setEmail(userSearchResponseVo.getEmail());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getPhone())) {
                user.setPhone(userSearchResponseVo.getPhone());
            }
            if(StringUtils.isNotBlank(userSearchResponseVo.getStatus())) {
                user.setStatus(userSearchResponseVo.getStatus());
            }
            userMapper.updatetUser(user);
            return CommonUtil.successMessageMap("修改教师人员成功");
        }
    }

    public Object getAllStudentList(UserSearchVo userSearchVo) {
        HashMap<String, Object> map = new HashMap<>();
        if(userSearchVo == null) userSearchVo = new UserSearchVo();
        User user = hostHolder.getUser();
        if(!roleMapper.findById(user.getRoleId()).getRoleName().equals("管理员")) {
            userSearchVo.setAcademyId(user.getAcademyId());
        }
        userSearchVo.setRoleId(4);
        List<UserSearchResponseVo> approverList = userMapper.findApproverByUserSearchVo(userSearchVo);
        map.put("approverList", approverList);
        return map;
    }


}
