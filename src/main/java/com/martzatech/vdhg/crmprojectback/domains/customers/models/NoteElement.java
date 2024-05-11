package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class NoteElement {
    private final Integer id;

    @With
    private final String title;

    @With
    private final String name;

    @With
    private final String surname;
}
