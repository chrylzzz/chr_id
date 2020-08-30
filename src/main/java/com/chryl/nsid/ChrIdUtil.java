package com.chryl.nsid;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Chr.yl on 2020/8/30.
 *
 * @author Chr.yl
 */
public class ChrIdUtil {
    private static byte[] lock = new byte[0];

    //位数: 默认8位
    private final static long w = 100000000;

    /**
     * 根据时间戳和随机数生成唯一序列
     */

    public static String nextId() {
        String prefixId = getLocalDateTime();
        long r = 0;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * w);
        }
        String suffixId = String.valueOf(r).substring(1);
        return prefixId.concat(suffixId);

    }

    public static String getLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String format = dateTimeFormatter.format(localDateTime);
        System.out.println(format);
        return format;
    }

    public static void main(String[] args) {
        System.out.println(nextId());
    }
}
