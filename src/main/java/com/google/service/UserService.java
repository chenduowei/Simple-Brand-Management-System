package com.google.service;

import com.google.mapper.UserMapper;
import com.google.pojo.User;
import com.google.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class UserService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.select(username, password);
        sqlSession.close();
        return user;
    }

    public boolean register(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // 判断用户是否存在
        User u = mapper.selectByUsername(user.getUsername());
        if (u == null) {
            mapper.add(user);
            sqlSession.commit();
        }
        sqlSession.close();
        return u == null;
    }
}
