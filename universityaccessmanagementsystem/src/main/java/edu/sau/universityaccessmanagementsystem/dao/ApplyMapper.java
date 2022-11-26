package edu.sau.universityaccessmanagementsystem.dao;

import edu.sau.universityaccessmanagementsystem.entity.Apply;
import edu.sau.universityaccessmanagementsystem.entity.ApplyResponseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author sxf
 * @Date 2022/11/19 14:27
 * @Version 1.0
 */
@Mapper
public interface ApplyMapper {
    void addApplyInfo(Apply apply);

    List<ApplyResponseVo> getOwnerApplyInfo(Integer id);

    List<ApplyResponseVo> getApplyUserInfo(int id, String name);

    void updateApplyStatus(Apply apply);

    List<ApplyResponseVo> getRoundApplyInfoNum(Integer academyId, Date startDate, Date endDate);

    List<ApplyResponseVo> getAllRoundApplyInfoNum(Integer academyId);
}
