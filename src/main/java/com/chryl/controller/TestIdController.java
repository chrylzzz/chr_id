package com.chryl.controller;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.chryl.snowFlake.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chr.yl on 2020/7/25.
 *
 * @author Chr.yl
 */
@Slf4j
@RestController
@RequestMapping("id")
public class TestIdController {

    @Autowired
    private IdGenerator idGenerator;
    ConcurrentHashSet<String> cset = new ConcurrentHashSet<>();

    @GetMapping("test")
    public void testNextId() {

        Set<String> set = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            long nextid = idGenerator.snowflakeId();
//            set.add(String.valueOf(nextid));
            cset.add(String.valueOf(nextid));

//            log.info("批次号: {} ,长度: {}", nextid, String.valueOf(nextid).length());//1362347306820698122
//            log.info("批次号: {} ,长度: {}", nextid, String.valueOf(nextid).length());//1362567611371618308
        }
//        System.out.println(set.size());
        System.out.println(cset.size());
    }

}
