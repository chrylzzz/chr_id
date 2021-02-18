package chryl.twt;

import com.chryl.ChrIdApplication;
import com.chryl.twitter.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Chr.yl on 2021/2/18.
 *
 * @author Chr.yl
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChrIdApplication.class)
public class TwtTest {
    //=========//=========//=========//=========//=========//=========//=========//=========//=========CountDownLatch测试并发

    //并发量
    private static final int USER_NUM = 5;

    //倒计时器，用于模拟高并发
    private static CountDownLatch countDownLatch = new CountDownLatch(USER_NUM);

    @Test
    public void contextLoads() throws InterruptedException {
        for (int i = 0; i < USER_NUM; i++) {

            new Thread(new UserRequest()).start();//此处不是并发
            //第一次200，第二次199，第三次198......0，直到减到0，会把等待的同时去执行：调用操作
            countDownLatch.countDown();
        }
        Thread.currentThread().sleep(1000);
    }


    static class UserRequest implements Runnable {
        @Override
        public void run() {
            try {
                SnowFlake snowFlake = new SnowFlake(2, 3);
                Set<String> set = new HashSet<>();

                for (int i = 0; i < 100000; i++) {
                    long nextId = snowFlake.nextId();
                    set.add(String.valueOf(nextId));
                }
//                System.out.println("==============" + set.size());
                log.warn("==============" + set.size());
                countDownLatch.await();//当前线程等待，等所有的线程实例化后，同时停止等待后调用接口代码
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

//=========//=========//=========//=========//=========//=========//=========//=========//=========

}
