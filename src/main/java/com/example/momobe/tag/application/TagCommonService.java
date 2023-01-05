package com.example.momobe.tag.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.tag.domain.Tag;
import com.example.momobe.tag.domain.TagNotFoundException;
import com.example.momobe.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagCommonService {
    private final TagRepository tagRepository;

    public Tag findTagOrThrowException(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new TagNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }
}
