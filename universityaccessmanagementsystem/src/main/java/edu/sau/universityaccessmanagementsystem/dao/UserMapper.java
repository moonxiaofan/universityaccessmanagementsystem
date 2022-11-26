package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.User;
import edu.sau.universityaccessmanagementsystem.entity.UserSearchResponseVo;
import edu.sau.universityaccessmanagementsystem.entity.UserSearchVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 20:23
 * @Version 1.0
 */
@Mapper
public interface UserMapper {
    int insertUser(User user);

    User findByUserName(String username);

    User findById(int userId);

    void updatetUser(User user);

    List<UserSearchResponseVo> findApproverByUserSearchVo(UserSearchVo userSearchVo);
}
