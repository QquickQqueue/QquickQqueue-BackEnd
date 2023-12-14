package com.example.qquickqqueue.domain.enumPackage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Rating {
    ALL("전체관람가"),
    PG12("12세관람가"),
    P15("15세관람가"),
    P18("청소년관람불가");

    private final String key;
}
