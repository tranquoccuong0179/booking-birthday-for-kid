package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl implements com.bookingBirthday.bookingbirthdayforkids.service.PackageService {
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    PackageServiceRepository packageServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired PartyBookingRepository partyBookingRepository;
    //fix
    @Override
    public ResponseEntity<ResponseObj> getAllForCustomer(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        List<Package> packageList = venue.get().getPackageList();
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages found for this venue", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    //fix done


    @Override
    public ResponseEntity<ResponseObj> getAllForHost() {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();
        List<Package> packageList = packageRepository.findAllByVenueId(venueId);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHostIsTrue() {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();
        List<Package> packageList = packageRepository.findAllByVenueIdAndIsActiveIsTrue(venueId);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHostIsFalse() {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();
        List<Package> packageList = packageRepository.findAllByVenueIdAndIsActiveIsFalse(venueId);

        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }


    @Override
    public ResponseEntity<ResponseObj> getByIdForCustomer(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        List<Venue> venue = venueRepository.findAllByIsActiveIsTrue();
        if (venue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }

        Optional<Package> apackage = packageRepository.findById(id);
        if (apackage.isPresent() && apackage.get().isActive()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", apackage));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist or is inactive", null));
        }
    }

//fix

    @Override
    public ResponseEntity<ResponseObj> getByIdForHost(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();

        Optional<Package> packageOptional = packageRepository.findById(id);
        if (!packageOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package not found", null));
        }

        Package packageItem = packageOptional.get();
        if (!packageItem.getVenue().getId().equals(venueId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "You do not have permission to access this package", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageItem));

    }

    //fix

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        if (account.get().getVenue() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Venue not found for this account", null));
        }
        try {
            for (PackageServiceRequest packageServiceRequest : packageServiceRequestList) {
                Optional<Services> serviceOptional = servicesRepository.findById(packageServiceRequest.getServiceId());
                if (!serviceOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service not found for ID: " + packageServiceRequest.getServiceId(), null));
                }
            }
            if (imgFile != null) {
                switch (typeEnum) {
                    case FOOD:
                    case DECORATION:
                        String img = firebaseService.uploadImage(imgFile);
                        Package pack = new Package();
                        float packPricing = 0;
                        pack.setPackageName(packageName);
                        pack.setPackageImgUrl(img);
                        pack.setPackageDescription(packageDescription);
                        pack.setActive(true);
                        pack.setCreateAt(LocalDateTime.now());
                        pack.setUpdateAt(LocalDateTime.now());
                        pack.setPackageType(typeEnum);
                        pack.setVenue(account.get().getVenue());
                        float newPricing = 0;
                        pack = packageRepository.save(pack);
                        for (PackageServiceRequest packageServiceRequest : packageServiceRequestList) {
                            PackageService packageService = new PackageService();
                            packageService.setCount(packageServiceRequest.getCount());
                            Optional<Services> serviceOptional = servicesRepository.findById(packageServiceRequest.getServiceId());
                            Services service = serviceOptional.get();
                            packageService.setPricing(packageService.getCount() * service.getPricing());
                            packageService.setActive(true);
                            packageService.setCreateAt(LocalDateTime.now());
                            packageService.setUpdateAt(LocalDateTime.now());
                            packPricing += packageService.getPricing();
                            packageService.setApackage(pack);
                            packageService.setServices(service);
                            packageServiceRepository.save(packageService);
                        }
                        newPricing = packPricing * percent;
                        pack.setPricing(packPricing - newPricing);
                        packageRepository.save(pack);
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Successful", pack));
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid package type", null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Error occurred: " + e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid image file", null));
    }


    //fix
    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String packageName, String packageDescription) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);

        Optional<Package> aPackage = packageRepository.findById(id);
        if (!aPackage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        Venue packageVenue = aPackage.get().getVenue();
        if (!packageVenue.getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You do not have permission to update this package", null));
        }

        try {
            aPackage.get().setPackageName(packageName == null ? aPackage.get().getPackageName() : packageName);
            aPackage.get().setPackageDescription(packageDescription == null ? aPackage.get().getPackageDescription() : packageDescription);
            aPackage.get().setPackageImgUrl(imgFile == null ? aPackage.get().getPackageImgUrl() : firebaseService.uploadImage(imgFile));
            aPackage.get().setUpdateAt(LocalDateTime.now());
            aPackage.get().setActive(true);
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    //fix
    @Override
    public ResponseEntity<ResponseObj> updatePercentPackage(Long id, float percent) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        Optional<Package> aPackage = packageRepository.findById(id);
        if (!aPackage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        Venue packageVenue = aPackage.get().getVenue();
        if (!packageVenue.getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You do not have permission to update this package", null));
        }

        try {
            float packPricing = 0;
            List<PackageService> packageServiceList = aPackage.get().getPackageServiceList();
            for (PackageService packageService : packageServiceList) {
                packPricing += packageService.getPricing();
            }
            float newPricing = packPricing * (1 - percent);
            aPackage.get().setPricing(newPricing);
            aPackage.get().setUpdateAt(LocalDateTime.now());
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    //fix
    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        Optional<Package> pack = packageRepository.findById(id);
        if (!pack.get().isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package is already inactive", null));
        }
        Venue packageVenue = pack.get().getVenue();
        if (!packageVenue.getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You do not have permission to delete this package", null));
        }
        if (pack.isPresent()) {
            pack.get().setActive(false);
            pack.get().setDeleteAt(LocalDateTime.now());
            pack.get().getPackageServiceList().forEach(packageService -> {
                packageService.setDeleteAt(LocalDateTime.now());
                packageService.setActive(false);
                packageServiceRepository.save(packageService);
            });
            packageRepository.save(pack.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> enablePackageForHost(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Package> pack = packageRepository.findById(id);
        if (pack.isPresent()) {
            if (pack.get().isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package is already active", null));
            } else if (!pack.get().getVenue().getAccount().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "You are not permission", null));
            }
            pack.get().setActive(true);
            pack.get().setDeleteAt(null);
            pack.get().getPackageServiceList().forEach(packageService -> {
                packageService.setDeleteAt(null);
                packageService.setActive(true);
                packageServiceRepository.save(packageService);
            });
            packageRepository.save(pack.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Enable successful", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForCustomerByType(Long venueId, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        List<Package> packageList = packageRepository.findAllByVenueIdAndPackageTypeAndIsActiveIsTrue(venue.get().getId(), typeEnum);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages found for this venue", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHostByType(TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();
        List<Package> packageList = packageRepository.findAllByVenueIdAndPackageType(venueId, typeEnum);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHostIsTrueByType(TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();

        List<Package> packageList = packageRepository.findAllByVenueIdAndIsActiveIsTrueAndPackageType(venueId, typeEnum);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHostIsFalseByType(TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You don't have any venue", null));
        }
        Long venueId = venue.getId();

        List<Package> packageList = packageRepository.findAllByVenueIdAndIsActiveIsFalseAndPackageType(venueId, typeEnum);

        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "No Package Found", new ArrayList<>()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }
    @Override
    public ResponseEntity<ResponseObj> getAllPackageByPartyBookingId(Long partyBookingId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(partyBookingId);
        if (partyBookingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
        }
        Venue venue =partyBookingOptional.get().getSlotInRoom().getRoom().getVenue();
        Long venueId = venue.getId();
        PartyBooking partyBooking = partyBookingOptional.get();
        List<PackageInBooking> selectedPackages = partyBooking.getPackageInBookings();
        List<Package> allPackages = packageRepository.findAllByVenueIdAndIsActiveIsTrue(venueId);
        List<Package> availablePackages = new ArrayList<>();
        for (Package aPackage : allPackages) {
            boolean isSelected = false;
            for (PackageInBooking selectedPackage : selectedPackages) {
                if (selectedPackage.getAPackage().equals(aPackage)) {
                    isSelected = true;
                    break;
                }
            }
            if (!isSelected) {
                availablePackages.add(aPackage);
            }
        }

        if (availablePackages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages available for this party booking", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", availablePackages));
    }
    @Override
    public ResponseEntity<ResponseObj> getAllPackageByPartyBookingIdAndType(Long partyBookingId, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(partyBookingId);
        if (partyBookingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
        }
        Venue venue =partyBookingOptional.get().getSlotInRoom().getRoom().getVenue();
        Long venueId = venue.getId();
        PartyBooking partyBooking = partyBookingOptional.get();
        List<PackageInBooking> selectedPackages = partyBooking.getPackageInBookings();
        List<Package> allPackages = packageRepository.findAllByVenueIdAndPackageTypeAndIsActiveIsTrue(venueId,typeEnum);
        List<Package> availablePackages = new ArrayList<>();
        for (Package aPackage : allPackages) {
            boolean isSelected = false;
            for (PackageInBooking selectedPackage : selectedPackages) {
                if (selectedPackage.getAPackage().equals(aPackage)) {
                    isSelected = true;
                    break;
                }
            }
            if (!isSelected) {
                availablePackages.add(aPackage);
            }
        }

        if (availablePackages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages available for this party booking", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", availablePackages));
    }
}

