package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.DatasourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DatasourceFactoryTest {

    private static DatasourceFactory factory;
    private static DatasourceInfo    dsi;

    @BeforeAll
    static void init() {
        factory = new DefaultDatasourceFactory();
        dsi = new DatasourceInfo();
        dsi = new DatasourceInfo();
        dsi.setUsername("root");
        dsi.setPassword("a123456");
        dsi.setUrl("jdbc:mysql://localhost:3306/octopus_db");
        dsi.setDatasourceType(DatasourceTypeHelper.PostgreSQL);
    }

    @DisplayName("创建咨询来源：成功创建")
    @Test
    public void getConnectionTest() throws Exception {
        Connection conn = factory.getConnection(dsi);
        log.info("获取连接测试： {}", conn != null);
        assertEquals("连接为空", conn);
    }

    @Test
    public void refreshDatasourceTest() throws Exception {
        factory.addDatasource(dsi);
        factory.addDatasource(dsi);
        factory.refreshDatasource(dsi);
        getConnectionTest();
        log.info("刷新数据源测试");
    }

    /**
     * 多线程获取数据源连接测试
     *
     * @throws Exception e
     */
    @Test
    public void multiThreadTest() throws Exception {
        int maxSize = 10;
        final CountDownLatch latch = new CountDownLatch(maxSize);
        Thread[] threads = new Thread[maxSize];
        for (int i = 0; i < maxSize; i++) {
            threads[i] = new Thread(() -> {
                try {
                    latch.await();
                    System.out.println(Thread.currentThread().getName() + "启动时间：" + System.currentTimeMillis());
                    Connection conn = factory.getConnection(dsi);
                    System.out.println(Thread.currentThread().getName() + "获取连接信息： " + (conn != null ? "成功" : "失败"));
                } catch (Exception e) {
                    log.error("获取连接失败", e);
                }
            });
            threads[i].setName("线程-" + (i + 1));
            threads[i].start();
            latch.countDown();
        }

        // 等待线程线束
        for (int i = 0; i < maxSize; i++) {
            threads[i].join();
            System.out.println(threads[i].getName() + " 结束。");
        }

        int size = factory.getDatasourceSize();
        System.out.println("数据源数量：" + size);
        assertEquals(size, 1);
    }

    @Test
    public void removeAndGetConnectionTest() throws Exception {
        int maxSize = 10;
        final CountDownLatch latch = new CountDownLatch(maxSize);
        Thread freshThread = new Thread(() -> {
            try {
                latch.await();
                System.out.println(Thread.currentThread().getName() + "启动时间：" + System.currentTimeMillis());
                factory.refreshDatasource(dsi);
                System.out.println(Thread.currentThread().getName() + "执行完成：" + System.currentTimeMillis());
            } catch (Exception e) {
                log.error("刷新失败");
            }
        });
        freshThread.setName("刷新数据源线程");
        freshThread.start();
        latch.countDown();

        Thread[] threads = new Thread[maxSize - 1];
        for (int i = 0; i < maxSize - 1; i++) {

            threads[i] = new Thread(() -> {
                try {
                    latch.await();
                    System.out.println(Thread.currentThread().getName() + "启动时间：" + System.currentTimeMillis());
                    factory.getConnection(dsi);
                    System.out.println(Thread.currentThread().getName() + "执行完成：" + System.currentTimeMillis());
                } catch (Exception e) {
                    log.error("获取Connection失败");
                }
            });
            threads[i].setName("获取Connection_" + (i + 1));
            threads[i].start();
            latch.countDown();
        }
        for (int i = 0; i < maxSize - 1; i++) {
            threads[i].join();
        }
        freshThread.join();
    }
}
