package com.capgemini.jstk.companytrainings.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalCouchTO {

    private Long id;
    private Long version;
    private String firstName;
    private String lastName;
    private String companyName;
}
