package com.cloud.consul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;

@RestController
public class TestController {

    @RequestMapping("/testConsul")
    public String test() {
        return "hello world";
    }


    public static void main(String[] args) {

        String[] s = { "2", "1", "-", "3", "*" }; // 逆波兰式
        String operator = "+-*/";
        Stack<Integer> stack = new Stack<>(); // 栈
        Integer a, b;
        for (int i = 0; i < s.length; i++) {
            if (!operator.contains(s[i]))
                stack.push(Integer.valueOf(s[i])); // 2,1 入栈; 3 入栈
            else {
                // 栈先进后出
                a = stack.pop(); // 1 出栈 a = 1 ; 3 出栈 a = 3
                b = stack.pop(); // 2 出栈 b = 2 ; -1 出栈 b = -1
                switch (operator.indexOf(s[i])) {
                    case 0:
                        stack.push(a + b);
                        break;
                    case 1:
                        stack.push(a - b);// 1 - 2 = -1 入栈
                        break;
                    case 2:
                        stack.push(a * b);// 3 * (-1) = -3 入栈
                        break;
                    case 3:
                        stack.push(a / b);
                        break;
                }
            }
        }
        System.out.println("result = " + stack.pop());// -3 出栈
    }
}
