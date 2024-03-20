package io.hhplus.tdd.domain;

import io.hhplus.tdd.database.UserPointDB;
import io.hhplus.tdd.domain.point.UserPoint;

public class UserPointRepository {

    private final UserPointDB userPointDB;
    public UserPointRepository(UserPointDB userPointDB){
        this.userPointDB = userPointDB;
    }
    public UserPoint selectById(long id){
        return userPointDB.selectById(id);
    }
    public UserPoint insertOrUpdate(long id, long amount){
        return userPointDB.insertOrUpdate(id, amount);
    }
}
