package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class NoteElementResponse {
    private final Integer id;

    @With
    private final String title;

    @With
    private final String name;

    @With
    private final String surname;
}
