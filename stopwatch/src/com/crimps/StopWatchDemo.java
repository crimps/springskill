package com.crimps;

import org.springframework.util.StopWatch;

public class StopWatchDemo {



    public static void main(String[] agrs){
        ActionTime actionTime = new ActionTime();
        StopWatch stopWatch = new StopWatch("执行计时示例");

        stopWatch.start("数据库用时");
        actionTime.actionDb();
        stopWatch.stop();

        stopWatch.start("网络连接用时");
        actionTime.actionHttp();
        stopWatch.stop();

        stopWatch.start("长用时任务");
        actionTime.actionLong();
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

        System.out.println(stopWatch.shortSummary());

        System.out.println(stopWatch.getTaskInfo());
    }
}
