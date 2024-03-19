package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.error.NotEnoughPointError;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public class PointService {
    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    public PointService(PointHistoryTable pointHistoryTable, UserPointTable userPointTable) {
        this.pointHistoryTable = pointHistoryTable;
        this.userPointTable = userPointTable;
    }

    public UserPoint charge(long userId, long amount){
        // userPoint 목록 조회 -> Point update -> history 추가
        UserPoint userPoint = userPointTable.selectById(userId);
        UserPoint updateUserPoint = userPointTable.insertOrUpdate(userPoint.id(), userPoint.point() + amount);
        PointHistory pointHistory = pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return updateUserPoint;
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

        UserPoint updateUserPoint = userPointTable.insertOrUpdate(userPoint.id(), pointLeft);
        PointHistory pointHistory = pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());
        return updateUserPoint;
    }

    public List<PointHistory> getHistory(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }



}
