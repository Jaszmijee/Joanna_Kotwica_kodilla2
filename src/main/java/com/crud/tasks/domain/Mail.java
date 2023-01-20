package com.crud.tasks.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Mail {

    @NonNull
    private final String mailTo;
    private final String subject;
    private final String message;
    private final String toCc;

}