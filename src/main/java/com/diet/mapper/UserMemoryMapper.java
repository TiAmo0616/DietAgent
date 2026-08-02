package com.diet.mapper;

import com.diet.model.UserMemoryRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMemoryMapper {
    List<UserMemoryRow> listByUser(@Param("userId") Long userId);

    int upsert(UserMemoryRow row);
}
