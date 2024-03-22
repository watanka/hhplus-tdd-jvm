package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryRepository;
import io.hhplus.tdd.database.UserPointRepository;
import io.hhplus.tdd.domain.error.NotEnoughPointError;
import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.domain.point.UserPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;

    @Autowired
    public PointService(PointHistoryRepository pointHistoryRepository, UserPointRepository userPointRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
        this.userPointRepository = userPointRepository;
    }

    public PointHistory recordHistory(long userId, long amount, TransactionType type){
        return pointHistoryRepository.insert(userId, amount, type, System.currentTimeMillis());
    }

    public UserPoint charge(long userId, long amount){
        // userPoint 목록 조회 -> Point update -> history 추가
        UserPoint userPoint = userPointRepository.selectById(userId);
        // 히스토리 테이블에 기록
        recordHistory(userId, amount, TransactionType.CHARGE);
        return userPointRepository.insertOrUpdate(userPoint.id(), userPoint.point() + amount);
    }


    public UserPoint getUserPointById(long id){
        return userPointRepository.selectById(id);
    }

    public UserPoint use(long userId, long amount) throws NotEnoughPointError {
        // userPoint 목록 조회 -> Point update -> history 추가
        UserPoint userPoint = userPointRepository.selectById(userId);
        long pointLeft = userPoint.point() - amount;
        if (pointLeft < 0){
            throw new NotEnoughPointError("포인트가 부족합니다.");

        }
        // 히스토리 테이블에 기록
        pointHistoryRepository.add(userId, amount, TransactionType.USE, System.currentTimeMillis());
        return userPointRepository.insertOrUpdate(userPoint.id(), pointLeft);
    }

    public List<PointHistory> getHistory(long id) {
        return pointHistoryRepository.selectAllByUserId(id);
    }



}
