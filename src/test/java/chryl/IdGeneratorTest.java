package chryl;

import cn.hutool.core.lang.Snowflake;
import com.chryl.ChrIdApplication;
import com.chryl.hutool_id.IdUtil;
import com.chryl.snowFlake.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Chr.yl on 2020/7/25.
 *
 * @author Chr.yl
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChrIdApplication.class)
public class IdGeneratorTest {

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void testNextId() {
        for (int i = 0; i < 20; i++) {
            long nextid = idGenerator.snowflakeId();
            log.info("批次号: {} ,长度: {}", nextid, String.valueOf(nextid).length());//1362347306820698122
        }
    }

    @Test
    public void testBatchId() {
        for (int i = 0; i < 100; i++) {
            String batchId = idGenerator.batchId(1001, 100);
            log.info("批次号: {} ,长度: {}", batchId, batchId.length());//202007250833098521001100160
        }
    }

    @Test
    public void testSimpleUUID() {
        for (int i = 0; i < 100; i++) {
            String simpleUUID = idGenerator.simpleUUID();
            log.info("simpleUUID: {} ,长度: {}", simpleUUID, simpleUUID.length());//7782a887f241408889a2a920af8e8183
        }
    }

    @Test
    public void testRandomUUID() {
        for (int i = 0; i < 100; i++) {
            String randomUUID = idGenerator.randomUUID();
            log.info("randomUUID: {} ,长度: {}", randomUUID, randomUUID.length());//80b1e039-a0e8-4138-951f-24abc1a8ad9f
        }
    }

    @Test
    public void testObjectID() {
        for (int i = 0; i < 100; i++) {
            String objectId = idGenerator.objectId();
            log.info("objectId: {} ,长度: {}", objectId, objectId.length());//5f1b7e527a6cf0fa27b8b9c2
        }
    }

    @Test
    public void testSnowflakeId() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                log.info("分布式 ID: {} ,长度: {}", idGenerator.snowflakeId());//1286822474406821892
            });
        }
        executorService.shutdown();
    }

    @Autowired
    IdUtil idUtil;

    @Test
    public void show() {
        for (int i = 0; i < 99; i++) {
            System.out.println(idUtil.nextShortId());
        }
    }
}