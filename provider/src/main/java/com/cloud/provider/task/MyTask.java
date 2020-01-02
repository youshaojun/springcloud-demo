package com.cloud.provider.task;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.cron.CronUtil;
import org.redisson.executor.CronExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyTask {

    @Bean
    public void test() { // 测试自定义配置定时任务
        // 思路
        // 后台配置定任务
        // 配置任务表达式, 执行方法名
        // 查询出所有配置任务, 通过工具类创建定时任务
        Test test = new Test();
        String cron = "30 * * * * ?";
        if (CronExpression.isValidExpression(cron)) { // 验证表达式
            CronUtil.schedule("1", cron, () -> ReflectUtil.invoke(test, "aa"));// 创建任务
            CronUtil.setMatchSecond(true); // 支持秒级任务
            CronUtil.start();
        }
        // 删除任务
        // CronUtil.remove("1");
        // 修改任务
        // CronPattern cronPattern = new CronPattern(cron);
        // CronUtil.updatePattern("1", cronPattern);
    }


}
