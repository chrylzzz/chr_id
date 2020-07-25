package com.chryl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Chr.yl on 2020/7/25.
 *
 * @author Chr.yl
 */
@RestController
@RequestMapping("/id")
public class TestIdController {

    @GetMapping
    public void show() {

    }
}
