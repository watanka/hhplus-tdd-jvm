package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.error.NotEnoughPointError;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    @Autowired
    public PointService(PointHistoryTable pointHistoryTable, UserPointTable userPointTable) {
        this.pointHistoryTable = pointHistoryTable;
        this.userPointTable = userPointTable;
    }

    public PointHistory recordHistory(long userId, long amount, TransactionType type){
        return pointHistoryTable.insert(userId, amount, type, System.currentTimeMillis());
    }

    public UserPoint charge(long userId, long amount){
        // userPoint 목록 조회 -> Point update -> history 추가
        UserPoint userPoint = userPointTable.selectById(userId);
        // 히스토리 테이블에 기록
        recordHistory(userId, amount, TransactionType.CHARGE);
        return userPointTable.insertOrUpdate(userPoint.id(), userPoint.point() + amount);
    }


    public UserPoint getUserPointById(long id){
        return userPointTable.selectById(id);
    }

    public UserPoint use(long userId, long amount) throws NotEnoughPointError {
        // userPoint 목록 조회 -> Point update -> history 추가
        UserPoint userPoint = userPointTable.selectById(userId);
        long pointLeft = userPoint.point() - amount;
        if (pointLeft < 0){
            throw new NotEnoughPointError("포인트가 부족합니다.");

        }
        // 히스토리 테이블에 기록
        pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());
        return userPointTable.insertOrUpdate(userPoint.id(), pointLeft);
    }

    public List<PointHistory> getHistory(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }



}
