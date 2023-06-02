package com.planet.assesment.repository;

import com.planet.assesment.repository.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    //We are directly using the ID present in csv so not using below generation strategy
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String phoneNumber;

    private Double parcelWeight;
}
