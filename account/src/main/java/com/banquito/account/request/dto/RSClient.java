package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSClient implements Serializable{

    private String id;
    private String identificationType;
    private String identification;
    private String lastname;
    private String firstname;
    private String fullname;
    private String status;
    private String email;
    private Date birthDate;
    private String gender;
    private String career;
    private String companyName;
    private String companyType;
    private Date createDateCompany;
    private String appLegalRepresent;
    private String articlesAssociatedDoc;
    private String basicServicesDocument;
    private String fingerPrint;
    private String incomeTaxDocument;
    private Date lastStatusDate;
    private String maritalStatus;
    private String monthlyAvgIncome;
    private String nationality;
    private String signature;
    private String taxPaymentPlace;
    private String tinDocument;
    private String workStatus;
    private Date creationDate;

    private RSUser RSUser;
    private List<RSClientRelationship> relationship;
    private List<RSClientReference> reference;
    private List<RSClientPhone> phone;
    private List<RSClientAddress> address;
    private List<RSClientSegment> segment;

}
