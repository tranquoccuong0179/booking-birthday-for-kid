package com.bookingBirthday.bookingbirthdayforkids.model;

import lombok.Getter;

@Getter
public enum StatusEnum {
    PENDING(1),
    CONFIRMED(2),
    CANCELLED(3),
    COMPLETED(4);
    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

}
