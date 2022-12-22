package com.example.momobe.common.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RedisStore <T> {
    void saveData(String key, T value, Long expire);
    Optional<T> getData(String key, Class<T> type);
    void deleteData(String key);
    Optional<T> getDataAndDelete(String key, Class<T> type);
}
