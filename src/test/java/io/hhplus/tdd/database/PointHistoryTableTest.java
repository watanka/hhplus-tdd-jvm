package io.hhplus.tdd.database;

import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.service.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PointHistoryTableTest {


    private PointHistoryTable pointHistoryTable = new PointHistoryTable();
    private UserPointTable userPointTable = new UserPointTable();
    private PointService pointService = new PointService(pointHistoryTable, userPointTable);




    @Test
    void 유저_포인트_동시성_체크() {}

    @Test
    void selectAllByUserId() {}
}

