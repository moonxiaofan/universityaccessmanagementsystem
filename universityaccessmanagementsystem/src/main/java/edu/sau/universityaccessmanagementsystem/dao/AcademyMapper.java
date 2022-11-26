package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.Academy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/15 17:03
 * @Version 1.0
 */
@Mapper
public interface AcademyMapper {
    int insertAcademy(Academy academy);

    List<Academy> getAllList();

    Academy findById(Integer id);

    void updateAcademy(Academy academy);
}
