package com.luoying.luochat.common;

import com.luoying.luochat.common.common.utils.JsonUtils;
import com.luoying.luochat.common.common.utils.JwtUtils;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private WxMpService wxMpService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedissonClient redissonClient;


    @Test
    public void testUserDao() {
        User user = userDao.getById(1);
        User insert = new User();
        insert.setName("LUO");
        insert.setOpenId("123");
        boolean save = userDao.save(insert);
        System.out.println(save);
    }

    @Test
    public void testWxMP() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }

    @Test
    public void testJWT() throws InterruptedException {
        System.out.println(jwtUtils.createToken(1L));
        Thread.sleep(1000);
        System.out.println(jwtUtils.createToken(1L));
    }

    @Test
    public void testRedis() throws InterruptedException {
        Long aLong = JsonUtils.toObj("1", Long.class);
        System.out.println(aLong);
    }



    @Test
    public void testRedisson() {
        RLock lock = redissonClient.getLock("key");
        lock.lock();
        System.out.println();
        lock.unlock();
    }
}
