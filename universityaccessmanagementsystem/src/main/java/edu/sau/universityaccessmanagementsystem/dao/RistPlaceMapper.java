package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.RiskPlace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 16:33
 * @Version 1.0
 */
@Mapper
public interface RistPlaceMapper {
    void insert(RiskPlace riskPlace);

    void delete();

    List<RiskPlace> getRiskPlace();

    RiskPlace findByPlace(String place);
}
