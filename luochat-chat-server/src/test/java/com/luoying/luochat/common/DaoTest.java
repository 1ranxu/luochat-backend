package com.luoying.luochat.common;

import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/2 12:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoTest {
    @Resource
    private UserDao userDao;

    @Test
    public void test() {
        User user = userDao.getById(1);
        User insert = new User();
        insert.setName("LUO");
        insert.setOpenId("123");
        boolean save = userDao.save(insert);
        System.out.println(save);
    }
}
