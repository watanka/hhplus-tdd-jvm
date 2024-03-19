package io.hhplus.tdd.database;

import io.hhplus.tdd.point.UserPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPointTableTest {

    UserPointTable userPointTable;

    @BeforeEach
    void beforeEach(){
        // 테이블 초기화
        userPointTable = new UserPointTable();
    }


    @Test
    void 유저_포인트_조회() {
        //given
        UserPoint userPoint = userPointTable.insertOrUpdate(1, 30);
        //when
        UserPoint selectedUserPoint = userPointTable.selectById(userPoint.id());
        //then
        assertThat(selectedUserPoint).isEqualTo(userPoint);
    }


    @Test
    void 유지_포인트_업데이트() {
        //given
        int originalPoint = 30;
        int addPoint = 50;
        UserPoint userPoint = userPointTable.insertOrUpdate(1, originalPoint);
        UserPoint updatedPoint = userPointTable.insertOrUpdate(userPoint.id(), userPoint.point() + addPoint);

        assertThat(updatedPoint.point()).isEqualTo(originalPoint + addPoint);

    }
}