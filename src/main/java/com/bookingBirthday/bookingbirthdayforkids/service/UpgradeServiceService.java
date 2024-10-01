package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface UpgradeServiceService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> delete(Long id);
}
