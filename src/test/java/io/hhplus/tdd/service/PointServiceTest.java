package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.error.NotEnoughPointError;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PointServiceTest {

    PointHistoryTable pointHistoryTable;
    UserPointTable userPointTable;
    PointService pointService;

    PointServiceTest(){
        this.userPointTable = new UserPointTable();
        this.pointHistoryTable = new PointHistoryTable();
        this.pointService = new PointService(pointHistoryTable, userPointTable);
    }



    @BeforeEach
    void beforeEach(){
        pointHistoryTable = new PointHistoryTable();
        userPointTable = new UserPointTable();

        pointService = new PointService(pointHistoryTable, userPointTable);
    }

    // charge 함수
    // 요구사항 분석
    // - 유저의 돈을 조회
    // - 유저 돈이랑 입력값이랑 더함
    // - 유저 돈 저장 후 반환
    @Test
    void 포인트_충전(){
        //given. 유저 포인트 초기화
        long userId = 1L;
        long pointAmount1 = 5000L;
        long pointAmount2 = 7000L;
        long pointAmount3 = 9000L;


        pointService.charge(userId, pointAmount1);
        pointService.charge(userId, pointAmount2);
        UserPoint updateUserPoint = pointService.charge(userId, pointAmount3);

        // 검증
        assertThat(updateUserPoint.id()).isEqualTo(userId);
        assertThat(updateUserPoint.point()).isEqualTo(pointAmount1 + pointAmount2 + pointAmount3);
    }

    @Test
    void 포인트_사용() {
        UserPoint userPoint = userPointTable.insertOrUpdate(1, 3000);

        UserPoint updateUserPoint = pointService.use(userPoint.id(), 2000);
        assertThat(updateUserPoint.point()).isEqualTo(3000 - 2000);
    }

    @Test
    void 포인트_사용실패(){
        UserPoint userPoint = userPointTable.insertOrUpdate(1, 3000);

        assertThrows(NotEnoughPointError.class, () -> pointService.use(userPoint.id(), 4000)) ;

    }

    @Test
    void 사용자포인트조회() {
        UserPoint userPoint = userPointTable.insertOrUpdate(1, 3000);
        UserPoint selectedUserPoint = pointService.getUserPointById(userPoint.id());

        assertThat(selectedUserPoint).isEqualTo(userPoint);

    }

    @Test
    void 포인트내역조회() {

        pointService.charge(1, 3000);
        pointService.use(1, 1000);
        pointService.charge(1, 2000);

        List<PointHistory> historyTableResult = pointHistoryTable.selectAllByUserId(1);

        List<PointHistory> serviceHistoryResult = pointService.getHistory(1);
        
        assertThat(serviceHistoryResult).isEqualTo(historyTableResult);
        
        
    }
}