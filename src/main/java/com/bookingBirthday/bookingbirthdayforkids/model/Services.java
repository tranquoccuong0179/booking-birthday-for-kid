package com.bookingBirthday.bookingbirthdayforkids.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Services extends BaseEntity{
    @NotBlank(message = "Services name cannot be blank")
    private String serviceName;
    @Column(name = "service_img_url",columnDefinition = "TEXT")
    private String serviceImgUrl;
    @Column(name = "service_description",columnDefinition = "TEXT")
    @NotBlank(message = "Description of services name cannot be blank")
    private String serviceDescription;
    @Enumerated(EnumType.STRING)
    private TypeEnum serviceType;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @OneToMany(mappedBy = "services", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PackageService> packageServiceList;

    @OneToMany(mappedBy = "services", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UpgradeService> upgradeServiceList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;
}
