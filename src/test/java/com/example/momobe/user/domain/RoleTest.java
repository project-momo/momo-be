package com.example.momobe.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.momobe.user.domain.enums.RoleName.*;
import static org.assertj.core.api.Assertions.*;

class RoleTest {
    @Test
    @DisplayName("ROLE_USER를 가진 Role 조회 테스트")
    public void getRoles1() {
        //given
        Role role = new Role(List.of(ROLE_USER));

        //when
        List<String> roles = role.getRoles();

        //then
        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles.get(0)).isEqualTo(ROLE_USER.toString());
    }

    @Test
    @DisplayName("ROLE_USER, ROLE_MANAGER를 가진 Role 조회 테스트")
    public void getRoles2() {
        //given
        Role role = new Role(List.of(ROLE_USER, ROLE_MANAGER));

        //when
        List<String> roles = role.getRoles();

        //then
        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles).contains(ROLE_MANAGER.toString(), ROLE_USER.toString());
    }

    @Test
    @DisplayName("ROLE_USER, ROLE_MANAGER, ROLE_ADMIN을 가진 Role 조회 테스트")
    public void getRoles() {
        //given
        Role role = new Role(List.of(ROLE_USER, ROLE_MANAGER, ROLE_ADMIN));

        //when
        List<String> roles = role.getRoles();

        //then
        assertThat(roles.size()).isEqualTo(3);
        assertThat(roles).contains(ROLE_MANAGER.toString(), ROLE_USER.toString(), ROLE_ADMIN.toString());
    }
}