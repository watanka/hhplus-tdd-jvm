package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryDB;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.domain.PointHistoryRepository;
import io.hhplus.tdd.domain.UserPointRepository;
import io.hhplus.tdd.domain.error.NotEnoughPointError;
import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.UserPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PointServiceTest {


    // 질문: Interface로 DB(?데이터 처리하는부분)을 감쌌기 때문에 mock을 따로 안둬도 된다?
    PointHistoryRepository pointHistoryRepository;
    UserPointRepository userPointRepository;
    PointService pointService;

    PointHistoryDB pointHistoryDB = new PointHistoryTable();
    UserPointDB userPointDB = new UserPointTable();



    PointServiceTest(){
        this.pointHistoryRepository = new PointHistoryRepository(pointHistoryDB);
        this.userPointRepository = new UserPointRepository(userPointDB);
        this.pointService = new PointService(pointHistoryRepository, userPointRepository);
    }



    @BeforeEach
    void beforeEach(){
        pointHistoryRepository = new PointHistoryRepository(pointHistoryDB);
        userPointRepository = new UserPointRepository(userPointDB);

        pointService = new PointService(pointHistoryRepository, userPointRepository);
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
        UserPoint userPoint = userPointRepository.insertOrUpdate(1, 3000);

        UserPoint updateUserPoint = pointService.use(userPoint.id(), 2000);
        assertThat(updateUserPoint.point()).isEqualTo(3000 - 2000);
    }

    @Test
    void 포인트_사용실패(){
        UserPoint userPoint = userPointRepository.insertOrUpdate(1, 3000);

        assertThrows(IllegalStateException.class, () -> pointService.use(userPoint.id(), 4000)) ;

    }

    @Test
    void 사용자포인트조회() {
        UserPoint userPoint = userPointRepository.insertOrUpdate(1, 3000);
        UserPoint selectedUserPoint = pointService.getUserPointById(userPoint.id());

        assertThat(selectedUserPoint).isEqualTo(userPoint);

    }

    @Test
    void 포인트내역조회() {

        pointService.charge(1, 3000L);
        pointService.use(1, 1000L);
        pointService.charge(1, 2000L);

        List<PointHistory> historyTableResult = pointHistoryRepository.selectAllByUserId(1L);

        List<PointHistory> serviceHistoryResult = pointService.getHistory(1L);
        
        assertThat(serviceHistoryResult).isEqualTo(historyTableResult);
        
        
    }
}