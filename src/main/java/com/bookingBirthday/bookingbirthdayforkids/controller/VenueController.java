package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/venue")
public class VenueController {
    @Autowired
    VenueService venueService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll() {
        return venueService.getAll();
    }

    @GetMapping("/get-all-venue-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost() {
        return venueService.getAllForHost();
    }

    @GetMapping("/get-venue-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id) {
        return venueService.getById_ForCustomer(id);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping(value = "/customize-venue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> customize(
            @RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                                 @RequestPart String venueName,
                                                 @RequestPart String venueDescription,
                                                 @RequestPart String street,
                                                 @RequestPart String ward,
                                                 @RequestPart String district,
                                                 @RequestPart String city) {
        return venueService.customize(fileImg, venueName, venueDescription, street, ward, district, city);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping(value = "/update-venue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> update(
            @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                              @RequestPart String venueName,
                                              @RequestPart String venueDescription,
                                              @RequestPart String street,
                                              @RequestPart String ward,
                                              @RequestPart String district,
                                              @RequestPart String city) {
        return venueService.update(fileImg, venueName, venueDescription, street, ward, district, city);
    }

    @PutMapping("/set-active-venue")
    public ResponseEntity<ResponseObj> setActiveVenue() {
        return venueService.activeVenue();
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObj> delete() {
        return venueService.delete();
    }

}
