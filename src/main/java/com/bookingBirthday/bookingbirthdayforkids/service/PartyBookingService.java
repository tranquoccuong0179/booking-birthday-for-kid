package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObjMeta;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.SlotInRoom;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.model.UpgradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PartyBookingService {
    public ResponseEntity<ResponseObj> getAllByUser();

    public ResponseEntity<ResponseObjMeta> getAll_ForHost(int page, int size);

    public ResponseEntity<ResponseObjMeta> getAllCompleted(int page, int size);

    public ResponseEntity<ResponseObj> getById_ForHost(Long partyBookingId);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long partyBookingId);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByDate(LocalDate date, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByStatus(StatusEnum statusEnum, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByTypeAndDate(StatusEnum statusEnum, LocalDate date, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByDateAndCreatedAndStatus(LocalDate date, LocalDate createdAt ,StatusEnum statusEnum, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByDateAndCreated(LocalDate date, LocalDate createdAt, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByStatusAndCreated(StatusEnum statusEnum, LocalDate createdAt, int page, int size);

    ResponseEntity<ResponseObjMeta> getAll_ForHostByCreated(LocalDate createdAt, int page, int size);

    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> updateUpgradeService(Long partyBookingId, List<UpgradeServiceRequest> dataUpgrade);

    public ResponseEntity<ResponseObj> updateOrganizationTime(Long partyBookingId, LocalDate date, Long slotInRoomId);

    public ResponseEntity<ResponseObj> updatePackage(Long partyBookingId, Long packageDecoId, Long packageFoodId);

    public ResponseEntity<ResponseObj> updateBasicInfo(Long partyBookingId, String kidName, String reservationAgent, LocalDate kidDOB, String email, String phoneNumber, int participantAmount);

    ResponseEntity<ResponseObj> cancelBooking_ForHost(Long partyBookingId);

    ResponseEntity<ResponseObj> cancelBooking_ForCustomer(Long partyBookingId);

    public ResponseEntity<ResponseObj> deleteBooking_ForHost(Long partyBookingId);

    ResponseEntity<ResponseObj> completeBooking_ForHost(Long partyBookingId);

    public List<PartyBooking> findConfirmedBookings();

    public void updateCronJob(Long partyBookingId, PartyBooking partyBooking);

    public List<PartyBooking> findPendingBookings();
}
