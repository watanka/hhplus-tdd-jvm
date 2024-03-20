package io.hhplus.tdd.domain.point;

import io.hhplus.tdd.domain.error.NotEnoughPointError;
import io.hhplus.tdd.service.PointService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class PointControllerTest {

    @Autowired
    private PointController pointController;

    @Autowired
    @MockBean
    private PointService pointService;



    @Test
    @DisplayName("포인트 조회 테스트")
    void point() {
        //given: 3000 포인트를 가진 유저 등록
        long userId = 1L;
        long amount = 3000L;


        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        // 이부분이 맞는 코드인지 모르겠다. 이렇게 조건을 셋팅하는 게 맞는지?
        when(pointService.getUserPointById(userId)).thenReturn(userPoint);

        //when: controller 유지 1의 포인트 조회
        UserPoint result = pointController.point(1);

        //then: 유저 1의 포인트 리턴
        assertThat(userId).isEqualTo(result.id());
        assertThat(amount).isEqualTo(result.point());
    }

    @Test
    @DisplayName("포인트 사용기록 조회 테스트")
    void history() {
        long userId = 1L;
        long amountCharge = 5000;
        long amountUsed = 3000;

        //given: 포인트 충전/사용 기록 발생
        PointHistory chargeHistory = pointService.recordHistory(userId, amountCharge, TransactionType.CHARGE);
        PointHistory useHistory = pointService.recordHistory(userId, amountUsed, TransactionType.USE);

        List<PointHistory> historyList = new ArrayList<>();
        historyList.add(chargeHistory);
        historyList.add(useHistory);


        when(pointService.getHistory(userId)).thenReturn(historyList);

        //when: history api 요청
        List<PointHistory> pointHistory = pointController.history(userId);
        //then
        assertThat(pointHistory.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("포인트 충전 테스트")
    void charge() {
        //given: 기존 포인트 3000에 2000포인트 추가
        long userId = 1L;
        long originalAmount = 3000;
        long chargeAmount = 2000;

        UserPoint userPoint = new UserPoint(userId, originalAmount + chargeAmount, System.currentTimeMillis());

        when(pointService.charge(userId, chargeAmount)).thenReturn(userPoint);

        //when: controller에 포인트 2000원 추가 요청
        Long point = pointController.charge(userId, chargeAmount).point();

        //then: 5000포인트 리턴
        assertThat(point)
                .isEqualTo(originalAmount + chargeAmount);
    }

    @Test
    @DisplayName("포인트 사용 테스트")
    void use() {
        //given: 기존 포인트 3000에 2000포인트 사용
        long userId = 1L;
        long originalAmount = 3000L;
        long useAmount = 2000L;

        UserPoint userPoint = new UserPoint(userId, originalAmount - useAmount, System.currentTimeMillis());

        when(pointService.use(userId, useAmount)).thenReturn(userPoint);

        //when: controller에 포인트 2000원 사용 요청
        Long point = pointController.charge(userId, useAmount).point();

        //then: 1000포인트 리턴
        assertThat(point)
                .isEqualTo(originalAmount - useAmount);
    }

    @Test
    @DisplayName("포인트 사용 실패 테스트")
    void use_fail() {
        //given: 기존 포인트 3000에 4000포인트 사용
        long userId = 1L;
        long originalAmount = 3000L;
        long useAmount = 4000L;

        UserPoint userPoint = new UserPoint(userId, originalAmount - useAmount, System.currentTimeMillis());

        //when: controller에 초과 포인트 사용 요청
        when(pointService.use(userId, useAmount)).thenThrow(NotEnoughPointError.class);



        //then: NotEnoughPointError 발생
        assertThatThrownBy(() ->
                pointController.use(userId, useAmount)).isInstanceOf(NotEnoughPointError.class);


    }
}