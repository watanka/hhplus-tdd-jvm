package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.PointHistoryDB;
import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;

import java.util.List;

public class PointHistoryRepository { // Repository는 더이상 DB의 세부사항에 대해 몰라도 된다.

    private final PointHistoryDB pointHistoryDB;

    public PointHistoryRepository(PointHistoryDB pointHistoryDB){
        this.pointHistoryDB = pointHistoryDB;
    }


    public PointHistory add(long userId, long amount, TransactionType type, long millis){
        return pointHistoryDB.insert(userId, amount, type, millis);
    }
    public List<PointHistory> getAllbyId(Long userId){
        return pointHistoryDB.selectAllByUserId(userId);
    }
}
