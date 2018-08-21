package com.capgemini.jstk.companytrainings.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalCouchTO {

    private Long id;
    private String firstName;
    private String secondName;
    private String companyName;
}
