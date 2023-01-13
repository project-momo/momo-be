package com.example.momobe.tag.application;

import com.example.momobe.tag.domain.TagNotFoundException;
import com.example.momobe.tag.domain.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.common.enums.TestConstants.ID2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagCommonServiceTest {
    @InjectMocks
    private TagCommonService tagCommonService;
    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("TagRepository가 올바른 개수의 ID를 반환했을 때 예외가 발생하지 않는다.")
    void findTagIdsByEngNamesOrThrowException() {
        // given
        List<String> tagEngNames = List.of("ONLINE", "OFFLINE");
        given(tagRepository.findTagIds(tagEngNames))
                .willReturn(List.of(ID1, ID2));

        // when / then
        assertDoesNotThrow(() -> tagCommonService.findTagIdsByNamesOrThrowException(tagEngNames));
    }

    @Test
    @DisplayName("TagRepository가 잘못된 개수의 ID를 반환했을 때 TagNotFoundException이 발생한다.")
    void findTagIdsByEngNamesOrThrowExceptionFail() {
        // given
        List<String> tagEngNames = List.of("ONLINE", "OFFLINE");
        given(tagRepository.findTagIds(tagEngNames))
                .willReturn(List.of(ID1));

        // when / then
        assertThrows(TagNotFoundException.class,
                () -> tagCommonService.findTagIdsByNamesOrThrowException(tagEngNames));
    }
}