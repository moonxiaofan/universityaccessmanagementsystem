package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.Setting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 18:09
 * @Version 1.0
 */
@Mapper
public interface SettingMapper {

    int updateRistPlaceUpdateTime(String date);

    Setting getSetting();

    void updateCheckType(Setting setting);
}
