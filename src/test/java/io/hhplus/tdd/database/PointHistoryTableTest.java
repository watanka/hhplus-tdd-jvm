package io.hhplus.tdd.database;

import io.hhplus.tdd.service.PointService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTableTest {


    private PointHistoryTable pointHistoryTable = new PointHistoryTable();
    private UserPointTable userPointTable = new UserPointTable();
    private PointService pointService = new PointService(pointHistoryTable, userPointTable);



    @Test
    @DisplayName("동시 요청 발생시 처리 테스트")
    void insert() throws InterruptedException {
        //given: 동시 요청 발생. 사용자 1
        int concurrentRequests = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(concurrentRequests);

        Long userId = 1L;

        List<Long> chargeList = Arrays.asList(1000L, 2000L, 3000L, 4000L, 5000L, 6000L, 7000L, 8000L, 9000L, 10000L);

        int idx = 0;

        Runnable task = () -> {
            try {
                startSignal.await();
                pointService.use(userId, chargeList.get(idx++));

                System.out.println("Handling request : " + System.currentTimeMillis());


                doneSignal.countDown();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);

        for (int i = 0; i < concurrentRequests; i++) {
            executor.submit(task, i);
        }

        startSignal.countDown();

        doneSignal.await();

        executor.shutdown();

    }

    @Test
    void selectAllByUserId() {
    }
}