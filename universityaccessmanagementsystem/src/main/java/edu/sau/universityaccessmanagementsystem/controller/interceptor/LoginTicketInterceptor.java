package edu.sau.universityaccessmanagementsystem.controller.interceptor;

import edu.sau.universityaccessmanagementsystem.entity.LoginTicket;
import edu.sau.universityaccessmanagementsystem.entity.User;
import edu.sau.universityaccessmanagementsystem.service.UserService;
import edu.sau.universityaccessmanagementsystem.util.CookieUtil;
import edu.sau.universityaccessmanagementsystem.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/18 20:18
 * @Version 1.0
 */
@Component
public class LoginTicketInterceptor  implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
