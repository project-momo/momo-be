package com.example.momobe.tag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select t.id from Tag t where t.engName in :engNames")
    List<Long> findTagIds(List<String> engNames);
}
