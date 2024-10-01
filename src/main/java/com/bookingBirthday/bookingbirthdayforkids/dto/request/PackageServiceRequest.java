package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageServiceRequest {
    private long serviceId;
    private int count;
}