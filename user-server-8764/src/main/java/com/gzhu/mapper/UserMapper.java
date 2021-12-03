package com.gzhu.mapper;

import com.gzhu.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User findById(Long id);
}
