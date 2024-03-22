package io.hhplus.tdd.database;

import io.hhplus.tdd.domain.point.UserPoint;

public interface UserPointRepository {
    UserPoint selectById(Long id);
    UserPoint insertOrUpdate(long id, long amount);
}
