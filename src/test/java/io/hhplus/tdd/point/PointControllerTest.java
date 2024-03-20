package io.hhplus.tdd.point;

import io.hhplus.tdd.service.PointService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.awt.*;
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

        //when: cnntroller 유지 1의 포인트 조회
        UserPoint result = pointController.point(1);

        //then: 유저 1의 포인트 리턴
        assertEquals(userId, result.id());
        assertEquals(amount, result.point());
    }

    @Test
    @DisplayName("포인트 사용기록 조회 테스트")
    void history() {
    }

    @Test
    @DisplayName("포인트 충전 테스트")
    void charge() {
    }

    @Test
    @DisplayName("포인트 사용 테스트")
    void use() {
    }
}