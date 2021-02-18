package com.chryl.twitter;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * java代码雪花算法源码
 * Created by Chr.yl on 2021/2/18.
 *
 * @author Chr.yl
 */
public class SnowFlake {
    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }
/*
    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(2, 3);
        Set<String> set = new HashSet<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            long nextId = snowFlake.nextId();
            set.add(String.valueOf(nextId));
//            System.out.println(nextId);
        }

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(set.size());
    }

    */

//=========//=========//=========//=========//=========//=========//=========//=========//=========

    //并发量
    private static final int USER_NUM = 5000;

    //倒计时器，用于模拟高并发
    private static CountDownLatch countDownLatch = new CountDownLatch(USER_NUM);

    @Test
    public static void contextLoads() throws InterruptedException {
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
                countDownLatch.await();//当前线程等待，等所有的线程实例化后，同时停止等待后调用接口代码
            } catch (Exception e) {
                e.printStackTrace();
            }

            SnowFlake snowFlake = new SnowFlake(2, 3);
            Set<String> set = new HashSet<>();

            for (int i = 0; i < 1000000; i++) {
                long nextId = snowFlake.nextId();
                set.add(String.valueOf(nextId));
            }
            System.out.println(set.size());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        contextLoads();
    }

}
