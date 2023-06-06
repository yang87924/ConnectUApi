package com.connectu.connectuapi.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
    @Select("ALTER TABLE userInfo DROP CONSTRAINT userInfo_ibfk_1")
    void dropConstraint();
}
