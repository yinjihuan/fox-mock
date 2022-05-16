package com.cxytiandi.foxmock.example.mybatis;

import org.apache.ibatis.annotations.*;

import java.util.List;

//@CacheNamespace
@Mapper
public interface UserMapper {

    @Select("select * from t_user")
    List<User> find();

    @Select("select * from t_user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select name from t_user where id = #{id}")
    String findNameById(@Param("id") Integer id);

    @Update("update t_user set name = #{name} where id = #{id}")
    int updateNameById(@Param("id") Integer id, @Param("name") String name);

    @Select("select * from t_user where id = #{id}")
    List<User> find2(UserQuery query);

}
