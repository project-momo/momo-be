package com.example.momobe.tag.application;

import com.example.momobe.tag.domain.TagNotFoundException;
import com.example.momobe.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.common.exception.enums.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagCommonService {
    private final TagRepository tagRepository;

    public List<Long> findTagIdsByEngNamesOrThrowException(List<String> tagEngNames) {
        List<Long> tagIds = tagRepository.findTagIds(tagEngNames);
        if (tagIds.size() != tagEngNames.size()) throw new TagNotFoundException(DATA_NOT_FOUND);
        return tagIds;
    }
}
