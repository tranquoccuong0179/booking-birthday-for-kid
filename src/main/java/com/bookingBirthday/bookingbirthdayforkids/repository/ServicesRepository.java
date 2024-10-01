package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    boolean existsServiceByServiceName(String serviceName);

    List<Services> findAllByIsActiveIsTrue();


    List<Services> findAllByAccountId(Long accountId);

    List<Services> findAllByServiceTypeAndIsActiveIsTrue(TypeEnum typeEnum);


    List<Services> findAllByServiceType(TypeEnum typeEnum);



}
