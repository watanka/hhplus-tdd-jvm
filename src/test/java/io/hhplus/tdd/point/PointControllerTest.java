package io.hhplus.tdd.point;

import io.hhplus.tdd.service.PointService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PointControllerTest {

    @Autowired
    private PointController pointController;

    @MockBean
    private PointService pointService;

    @Test
    void 포인트_조회() {
        //given: 3000 포인트를 가진 유저 등록
        long userId = 1L;
        long amount = 3000L;


        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        // 이부분이 맞는 코드인지 모르겠다. 이렇게 조건을 셋팅하는 게 맞는지?
        when(pointService.getUserPointById(userId)).thenReturn(userPoint);

        //when: 유지 1의 포인트 조회
        UserPoint result = pointController.point(1);

        //then: 유저 1의 포인트 리턴
        assertEquals(userId, result.id());
        assertEquals(amount, result.point());
    }

    @Test
    void history() {
    }

    @Test
    void charge() {
    }

    @Test
    void use() {
    }
}