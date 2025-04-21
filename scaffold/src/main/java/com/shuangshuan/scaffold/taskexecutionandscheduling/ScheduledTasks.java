package com.shuangshuan.scaffold.taskexecutionandscheduling;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.service.AddressBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    @Autowired
    private AddressBookService addressBookService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }


    @Scheduled(cron = "0 55 13 * * ?")
    public void syncInsertDatas() {
        addressBookService.testSyncInsertDatas();
        log.info("批量插入10万条数据成功！");// 会不会我直接写方法，他会直接自己放入线程池的意思？不是的，是mein方法继续执行的

    }

    @Scheduled(cron = "0 02 19 * * ?")
    public Future asyncInsertDatas() {

        return CompletableFuture.supplyAsync(() -> "success")
                .thenApplyAsync(value -> {
                    log.info("异步线程的线程名称：" + Thread.currentThread().getName());
                    return addressBookService.testAsyncInsertDatas();
                });
    }
}
