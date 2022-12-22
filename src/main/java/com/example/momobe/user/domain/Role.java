package com.example.momobe.user.domain;

import com.example.momobe.user.domain.enums.RoleName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    private String roleNames;

    public Role(List<RoleName> roleNames) {
        this.roleNames = roleNames
                .stream()
                .map(Enum::toString)
                .collect(Collectors.joining(","));
    }

    public List<String> getRoles() {
        return Arrays
                .stream(roleNames.split(","))
                .collect(Collectors.toList());
    }
}
