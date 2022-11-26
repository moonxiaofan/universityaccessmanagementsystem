package edu.sau.universityaccessmanagementsystem.util;

import edu.sau.universityaccessmanagementsystem.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/18 20:19
 * @Version 1.0
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
