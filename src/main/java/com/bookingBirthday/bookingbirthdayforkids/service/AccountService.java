package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.LoginRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
    public ResponseEntity<ResponseObj> getAll(int page, int size);

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<?> create(AccountRequest accountRequest);

    public ResponseEntity<ResponseObj> authenticate(LoginRequest request);
    public ResponseEntity<?> loginWithGmail(String accessToken) throws FirebaseAuthException;
    public ResponseEntity<ResponseObj> information();

    ResponseEntity<ResponseObj> authenticateAdmin(LoginRequest loginRequest);

    ResponseEntity<ResponseObj> authenticateHost(LoginRequest loginRequest);
    ResponseEntity<ResponseObj> updateImg(MultipartFile imgFile);
    ResponseEntity<ResponseObj> updateAccount(Long id, AccountRequest accountRequest);
}
