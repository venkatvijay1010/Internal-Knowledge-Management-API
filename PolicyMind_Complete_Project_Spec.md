# PolicyMind — Agentic RAG for Insurance Claims Research
## Complete Project Specification with Data Models

---

# PART 1: PROJECT OVERVIEW

## Project Recommendation

**Project Name**: PolicyMind — Agentic RAG for Insurance Claims Research

**Why This Is The Best Choice**:
- Direct domain alignment: Your codebase handles health insurance policies, claims, quotes, and member enrollments — PolicyMind solves the exact problem of researching coverage across policy documents and claims history
- Architecture translation: Your Strategy pattern for validation maps directly to agent tool-routing; your async processing maps to parallel agent execution
- Schema familiarity: Entities like `EnrolmentDetails`, `QuoteMemberInfo`, `GroupActuarialHealthClaims` translate 1:1 to synthetic policy/claims data
- Highest interview signal: Agentic RAG is the #1 skill companies probe for Applied AI roles

**Resume Value**:
- Demonstrates end-to-end AI system design, not just LLM API calls
- Shows hybrid deterministic + LLM decision-making (your validation strategy pattern in AI form)
- Proves you can bridge domain expertise into AI applications — exactly what enterprise AI teams need

**AI Skills Demonstrated**:

| Skill | Signal Level |
|-------|-------------|
| Agentic Tool Routing | HIGH — core interview topic |
| RAG Pipeline Design | HIGH — production RAG is complex |
| Hybrid Search (Vector + BM25) | MEDIUM-HIGH — shows retrieval depth |
| Citation-Grounded Generation | HIGH — prevents hallucination |
| Structured Output Enforcement | MEDIUM — production necessity |
| LLM Evaluation (RAGAS) | HIGH — separates senior from mid |
| Observability & Tracing | MEDIUM — production readiness |

---

# PART 2: COMPLETE DATA MODEL SPECIFICATION

## Source Entity Analysis (From Your Java Codebase)

Your codebase contains 50+ entities across these schemas:
- `digit_health` — Health insurance enrollment and policy management
- `GRPHEALTH` — Group health quotes, members, and transactions

---

## 2.1 ENROLLMENT ENTITIES (digit_health schema)

### EnrolmentDetails
**Table**: `t_group_enrolment_details`
**Schema**: `digit_health`
**Purpose**: Master enrollment record for group policies

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| id | id | BIGINT | PK, AUTO_INCREMENT | Primary key |
| userId | user_id | VARCHAR(255) | SENSITIVE | User identifier (PII) |
| insId | ins_id | INTEGER | | Insurance ID |
| totalNoOfEnrolled | total_no_of_enrolled | INTEGER | | Count of enrolled members |
| paymentMode | payment_mode | VARCHAR(50) | | Payment method |
| quoteNumber | quote_number | VARCHAR(100) | | Associated quote |
| masterPolicyNumber | master_policy_number | VARCHAR(100) | SENSITIVE | Master policy (PII) |
| enrolmentStartDate | enrolment_start_date | DATE | | Enrollment period start |
| enrolmentEndDate | enrolment_end_date | DATE | | Enrollment period end |
| isEnrolLinkModeEmail | is_enrol_link_mode_email | BOOLEAN | | Email enrollment flag |
| isEnrolLinkModeSms | is_enrol_link_mode_sms | BOOLEAN | | SMS enrollment flag |
| emailContent | email_content | TEXT | | Email template content |
| smsContent | sms_content | TEXT | | SMS template content |
| emailTemplateNo | email_template_no | INTEGER | | Email template ID |
| smsTemplateNo | sms_template_no | INTEGER | | SMS template ID |
| isKycRequired | is_kyc_required | BOOLEAN | | KYC requirement flag |
| isDgh | is_dgh | BOOLEAN | | Digit Health flag |
| isEnrolmentCompleted | is_enrolment_completed | BOOLEAN | | Completion status |
| productCode | product_code | VARCHAR(50) | | Product identifier |
| productName | product_name | VARCHAR(255) | | Product display name |
| riskType | risk_type | VARCHAR(50) | | Risk classification |
| status | status | VARCHAR(50) | | Enrollment status |
| createdTimestamp | created_timestamp | TIMESTAMP | AUTO | Creation timestamp |
| modifiedTimestamp | modified_timestamp | TIMESTAMP | AUTO | Last modified |
| dataValidationResponse | data_validation_response | JSON | | Validation results |
| coverageDetailss3Link | coverage_details_s3_link | VARCHAR(500) | | S3 link to coverage PDF |
| emailSubject | email_subject | VARCHAR(255) | | Email subject line |
| enrolmentMemberPremiumChart | enrolment_member_premium_chart | TEXT | | Premium chart data |
| emailCc | email_cc | VARCHAR(500) | | CC recipients |
| emailBcc | email_bcc | VARCHAR(500) | | BCC recipients |

**Relationships**:
- `1:N` → EnrolmentEmployeePaymentMode
- `1:N` → EnrolmentFamilyCompOutsidePolicy
- `1:N` → EnrolmentKycDocumentType
- `1:N` → EnrolmentReminder
- `1:N` → EnrolmentSumInsured

---

### EnrolmentEmpDetails
**Table**: `t_group_enrolment_emp_details`
**Schema**: `digit_health`
**Purpose**: Individual employee/member enrollment records

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| eid | eid | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteId | quote_id | INTEGER | FK | Quote reference |
| referenceIdKey | ref_eid | INTEGER | | Reference ID |
| employeeFirstName | mem_fname | VARCHAR(100) | | First name |
| employeeLastName | mem_lastname | VARCHAR(100) | | Last name |
| empId | emp_id | VARCHAR(50) | | Employee ID |
| relationship | relationship | VARCHAR(50) | | Relation to primary |
| gender | gender | VARCHAR(10) | | M/F/Other |
| employeeDob | date_of_birth | DATE | | Date of birth |
| age | age | INTEGER | | Calculated age |
| ageBand | age_band | VARCHAR(20) | | Age bracket (18-25, etc.) |
| sumInsured | sum_insured | INTEGER | | Coverage amount |
| brPremium | br_premium | FLOAT | | Base rate premium |
| finalPremium | final_premium | FLOAT | | Final premium |
| packageName | package | VARCHAR(100) | | Package tier |
| asAtDate | as_at_date | DATE | | Effective date |
| dateOfjoining | date_of_joining | DATE | | Employment start |
| childPolicyNo | child_policy_no | VARCHAR(100) | SENSITIVE | Individual policy number |
| masterPolicyNo | master_policy_no | VARCHAR(100) | SENSITIVE | Master policy reference |
| status | status | VARCHAR(50) | | Member status |
| memberId | member_id | VARCHAR(50) | | Unique member ID |
| processFlag | process_flag | BOOLEAN | | Processing status |
| dateOfResign | date_of_resign | DATE | | Employment end |
| memberAdd1 | mem_add_1 | VARCHAR(255) | | Address line 1 |
| memberAdd2 | mem_add_2 | VARCHAR(255) | | Address line 2 |
| mob | mob | VARCHAR(15) | SENSITIVE | Mobile number (PII) |
| email | email | VARCHAR(255) | SENSITIVE | Email address (PII) |
| city | city | VARCHAR(100) | | City |
| state | state | VARCHAR(100) | | State |
| pinCode | pincode | VARCHAR(10) | | Postal code |
| district | district | VARCHAR(100) | | District |
| errorMessage | errormessage | TEXT | | Validation errors |
| errorRequest | error_request | TEXT | | Error request payload |
| errorResponse | error_response | TEXT | | Error response |
| processedTime | processed_time | TIMESTAMP | | Processing timestamp |
| endorsementType | endorsement_type | VARCHAR(50) | | Type of endorsement |
| endorsementEffectiveDate | endorsement_effective_date | DATE | | Endorsement start date |
| amendmentCode | amendment_code | VARCHAR(50) | | Amendment identifier |
| userId | user_id | VARCHAR(255) | SENSITIVE | User who processed |
| userRole | user_role | VARCHAR(50) | | User's role |
| isBotPicked | is_bot_picked | BOOLEAN | | Bot processing flag |
| reason | reason | TEXT | | Processing reason |
| productCode | product_code | VARCHAR(50) | | Product code |
| productName | product_name | VARCHAR(255) | | Product name |
| nomineeName | nominee_name | VARCHAR(255) | | Nominee full name |
| nomineeRelation | nominee_relation | VARCHAR(50) | | Nominee relationship |
| nomineeDob | nominee_dob | TIMESTAMP | | Nominee DOB |
| loanAmount | loan_amount | FLOAT | | Associated loan amount |
| loanTenure | loan_tenure | INTEGER | | Loan tenure in months |
| loanDate | loan_date | TIMESTAMP | | Loan date |
| mariatalStatus | mariatal_status | VARCHAR(20) | | Marital status |
| height | height | INTEGER | | Height in cm |
| occupation | OCCUPATION | VARCHAR(100) | | Occupation |
| grade | GRADE | VARCHAR(50) | | Employee grade |

---

### EnrolmentSumInsured
**Table**: `t_group_enrolment_sum_insured`
**Schema**: `digit_health`
**Purpose**: Sum insured options for enrollment

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| id | id | BIGINT | PK, AUTO_INCREMENT | Primary key |
| enrolmentId | enrolment_id | BIGINT | FK | Parent enrollment |
| sumInsured | sum_insured | DOUBLE | | Coverage amount |
| minSumInsured | min_sum_insured | DOUBLE | | Minimum allowed SI |
| maxSumInsured | max_sum_insured | DOUBLE | | Maximum allowed SI |
| sumInsuredThreshold | sum_insured_threshold | INTEGER | | SI threshold |
| createdTimestamp | created_timestamp | TIMESTAMP | AUTO | Created |
| modifiedTimestamp | modified_timestamp | TIMESTAMP | AUTO | Modified |

---

### EnrolmentReminder
**Table**: `t_group_enrolment_reminder`
**Schema**: `digit_health`
**Purpose**: Enrollment reminder scheduling

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| id | id | BIGINT | PK, AUTO_INCREMENT | Primary key |
| enrolmentId | enrolment_id | BIGINT | FK | Parent enrollment |
| isReminderSent | is_reminder_sent | BOOLEAN | | Sent flag |
| startDateTime | start_date_time | TIMESTAMP | | Reminder schedule |
| frequency | frequency | VARCHAR(50) | | Daily/Weekly/etc. |
| frequencyCount | frequency_count | INTEGER | | Number of reminders |
| status | status | VARCHAR(50) | | Reminder status |
| createdTimestamp | created_timestamp | TIMESTAMP | AUTO | Created |
| modifiedTimestamp | modified_timestamp | TIMESTAMP | AUTO | Modified |
| isReminderToBeSent | is_reminder_to_be_sent | BOOLEAN | | Active flag |

---

## 2.2 QUOTE ENTITIES (GRPHEALTH schema)

### QuoteDtls
**Table**: `t_quote_details`
**Schema**: `GRPHEALTH`
**Purpose**: Master quote record for group health insurance

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| quoteId | quote_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteNumber | quote_number | VARCHAR(100) | UNIQUE | Quote identifier |
| policyStartDate | policy_start_date | DATE | NOT NULL | Policy inception |
| policyEndDate | policy_end_date | DATE | NOT NULL | Policy expiry |
| absPolicyType | abs_policy_type | VARCHAR(50) | NOT NULL | Policy type code |
| policyDuration | policy_duration | VARCHAR(20) | | Duration in months |
| absPolicyCategory | abs_policy_Category | VARCHAR(50) | | Policy category |
| noOfLives | no_of_lives | INTEGER | | Total members |
| isMandatory | is_mandatory | BOOLEAN | | Mandatory coverage flag |
| premiumFundedBy | premium_funded_by | VARCHAR(50) | | Employer/Employee |
| claimPaymentTo | claim_payment_to | VARCHAR(50) | | Claim beneficiary |
| isTender | is_tender | BOOLEAN | | Tender business flag |
| retirementAge | retirement_age | INTEGER | | Max age for coverage |
| absPaymentFrequency | abs_payment_frequency | VARCHAR(50) | | Monthly/Annual/etc. |
| isIrregularInst | is_irregular_inst | BOOLEAN | | Irregular installment |
| tpaName | tpa_name | VARCHAR(255) | | Third-party administrator |
| imdCode | imd_code | VARCHAR(50) | | Intermediary code |
| imdName | imd_name | VARCHAR(255) | | Intermediary name |
| absCustAccountNo | abs_cust_account_no | VARCHAR(100) | | Customer account |
| companyCode | company_code | VARCHAR(50) | | Company identifier |
| ddTicketNo | dd_ticket_no | VARCHAR(100) | | Deal desk ticket |
| productCode | product_code | VARCHAR(50) | | Product code |
| riskType | risk_type | VARCHAR(50) | | Risk category |
| createdBy | created_by | VARCHAR(100) | NOT NULL | Creator user |
| createdDate | created_date | TIMESTAMP | AUTO | Created timestamp |
| updatedBy | updated_by | VARCHAR(100) | | Last modifier |
| updatedDate | updated_date | TIMESTAMP | AUTO | Last modified |
| quoteExpDate | QUOTE_EXP_DATE | DATE | | Quote expiry date |
| absMasterContractId | ABS_MASTER_CONTRACT_ID | VARCHAR(100) | | Contract ID |
| gstExemption | GST_EXEMPTION | VARCHAR(50) | | GST exemption status |
| claimHistoryOutputS3Link | CLAIM_HISTORY_OUTPUT_S3_LINK | VARCHAR(500) | | S3 claim history |
| groupBusinessType | GROUP_BUSINESS_TYPE | VARCHAR(50) | | Business type |
| memberCustomMapper | MEMBER_CUSTOM_MAPPER | TEXT | LOB | Custom field mapping |
| qqJsonMapper | QQ_JSON_MAPPER | TEXT | LOB | Quick quote JSON |
| qqMemberDataUrl | QQ_MEMBER_DATA_URL | VARCHAR(500) | | Member data URL |
| partnerQqMemberDataUrl | partner_qq_member_data_url | VARCHAR(500) | | Partner data URL |
| claimHistoryStatus | claim_history_status | VARCHAR(50) | | Claim history status |
| mpRequestType | mp_request_type | VARCHAR(50) | | Request type |
| imdAddr | imd_addr | TEXT | | Intermediary address |
| imdMob | imd_mob | VARCHAR(20) | | Intermediary mobile |
| imdEmail | imd_email | VARCHAR(255) | | Intermediary email |
| isStp | is_stp | BOOLEAN | | Straight-through processing |
| memberDemography | MEMBER_DEMOGRAPHY | TEXT | LOB | Demographics JSON |
| proposalQuoteNumber | PROPOSAL_QUOTE_NUMBER | VARCHAR(100) | | Proposal reference |
| dghDcQuestionClusterId | DGH_DC_question_cluster_id | VARCHAR(100) | | Question cluster |
| dealType | deal_type | VARCHAR(50) | | Deal classification |
| policyGroupType | policy_group_type | VARCHAR(50) | | Group type |
| premiumPaymentTerm | premium_payment_term | VARCHAR(50) | | Payment term |

**Relationships**:
- `1:N` → QuoteOption
- `1:N` → QuoteDocumentDtls
- `1:N` → QuoteOtherLob
- `1:N` → QuoteExtDetails
- `1:N` → QuoteClaimSummary
- `1:N` → QuoteMasterPolicy

---

### QuoteOption
**Table**: `t_quote_option`
**Schema**: `GRPHEALTH`
**Purpose**: Quote option variants (different coverage levels)

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| quoteOptionId | quote_option_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteId | quote_id | INTEGER | FK | Parent quote |
| optionName | option_name | VARCHAR(100) | | Option display name |
| optionDesc | option_desc | TEXT | | Option description |
| isActive | is_active | BOOLEAN | | Active flag |
| isFinalOption | is_final_option | BOOLEAN | | Selected option flag |
| isMandatory | is_mandatory | BOOLEAN | | Mandatory coverage |
| familyComposition | family_composition | VARCHAR(100) | | E, E+S, E+S+C, etc. |
| asAtClaimDate | as_at_claim_date | DATE | | Claim cutoff date |
| noOfLives | no_of_lives | INTEGER | | Member count |
| uwQqMemberDataUrl | UW_QQ_MEMBER_DATA_URL | VARCHAR(500) | | UW data URL |
| annexureS3Link | ANNEXURES3LINK | VARCHAR(500) | | Annexure document |
| claimHistoryStatus | claim_history_status | VARCHAR(50) | | Claim status |
| claimHistoryOutputS3Link | CLAIM_HISTORY_OUTPUT_S3_LINK | VARCHAR(500) | | Claim history link |
| isCoInsApplicable | IS_COINS_APPLICABLE | BOOLEAN | | Co-insurance flag |
| printCoShareGrid | PRINT_COSHARE_GRID | BOOLEAN | | Print co-share |

**Relationships**:
- `1:N` → QuotePackages
- `1:N` → QuoteMemberPremium
- `1:N` → QuoteValidation
- `1:N` → QuoteOptionExt
- `1:N` → QuotePrevPolDtls
- `1:N` → QuotePremiumCoeff
- `1:N` → QuoteInstallment
- `1:N` → QuoteDocumentDtls
- `1:N` → QuoteCoinsuranceDtls
- `1:N` → QuoteHospitalDtls

---

### QuotePackages
**Table**: `t_quote_packages`
**Schema**: `GRPHEALTH`
**Purpose**: Coverage packages within a quote option

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| quotePackageId | quote_package_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteOptionId | quote_option_id | INTEGER | FK | Parent option |
| packageName | package_name | VARCHAR(100) | | Package display name |
| packageDesc | package_desc | TEXT | | Package description |
| isActive | is_active | BOOLEAN | | Active flag |
| familyComposition | family_composition | VARCHAR(100) | | Family structure |
| netPremium | net_premium | DOUBLE | | Net premium amount |
| premiumRate | premium_rate | DOUBLE | | Rate per unit |
| packageType | PACKAGE_TYPE | VARCHAR(50) | | Base/TopUp/Super |
| grossPremium | GROSS_PREMIUM | DOUBLE | | Gross premium |
| taxAmount | TAX_AMOUNT | DOUBLE | | GST amount |
| versionId | VERSION_ID | INTEGER | | Version number |
| createdBy | CREATED_BY | VARCHAR(100) | | Creator |
| createdDate | CREATED_DATE | TIMESTAMP | AUTO | Created |
| taxPercent | tax_percent | DOUBLE | | GST percentage |
| perFamilyRate | PERFAMILYRATE | DOUBLE | | Per family rate |
| perFamilyRateUW | PERFAMILYRATEUW | DOUBLE | | UW per family rate |
| packageCode | PACKAGE_CODE | VARCHAR(50) | | Internal code |
| packageLookupKey | PACKAGE_LOOKUP_KEY | INTEGER | | Lookup reference |
| sumInsured | SUM_INSURED | INTEGER | | Coverage amount |
| selfMaxAge | SELF_MAX_AGE | VARCHAR(10) | | Max age for self |
| childMaxAge | CHILD_MAX_AGE | VARCHAR(10) | | Max age for child |
| spouseMaxAge | SPOUSE_MAX_AGE | VARCHAR(10) | | Max age for spouse |
| parentMaxAge | PARENT_MAX_AGE | VARCHAR(10) | | Max age for parent |
| siblingsMaxAge | SIBLINGS_MAX_AGE | VARCHAR(10) | | Max age for siblings |

**Relationships**:
- `1:N` → QuotePackageCoverages
- `1:N` → QuoteSaCriteria
- `1:N` → QuoteReferral
- `1:N` → QuoteDiseaseCapping

---

### QuotePackageCoverages
**Table**: `t_quote_package_coverages`
**Schema**: `GRPHEALTH`
**Purpose**: Individual coverage benefits within packages

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| quoteCoverageId | quote_coverage_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quotePackageId | quote_package_id | INTEGER | FK | Parent package |
| coverageName | coverage_name | VARCHAR(255) | | Coverage display name |
| isOpted | is_opted | BOOLEAN | | Selected flag |
| coverageCondition | coverage_condition | TEXT | | Coverage terms |
| coverCode | COVER_CODE | VARCHAR(50) | | Internal coverage code |
| sumType | SUM_TYPE | VARCHAR(50) | | SI/Aggregate/Per Claim |
| coverageValue | coverage_value | VARCHAR(255) | | Value/Limit |
| coveragePrintText | coverage_print_text | TEXT | | Print description |
| coverageStartDate | coverage_start_date | DATE | | Coverage start |
| coverageEndDate | coverage_end_date | DATE | | Coverage end |
| netPremium | net_premium | DOUBLE | | Premium for coverage |
| premiumRate | premium_rate | DOUBLE | | Rate |
| sectionNumber | section_number | VARCHAR(20) | | Section reference |
| isMandatory | is_mandatory | BOOLEAN | | Mandatory coverage |
| absCoreNumber | ABS_CORE_NUMBER | INTEGER | | Core system number |
| premiumCoeff | PREMIUM_COEFF | VARCHAR(100) | | Premium coefficient |
| aggregateSi | aggregate_si | DOUBLE | | Aggregate SI |
| netBurnCost | net_burn_cost | DOUBLE | | Net burn cost |
| impliedPercStpPrice | implied_perc_STP_Price | DOUBLE | | STP implied % |
| percOfStandardTable | perc_of_standard_table | DOUBLE | | % of standard |
| rateType | rate_type | VARCHAR(50) | | Rate type |
| classType | CLASS_TYPE | VARCHAR(50) | | Class classification |
| classShortName | CLASS_SHORT_NAME | VARCHAR(50) | | Short name |
| benefitType | BENEFIT_TYPE | VARCHAR(50) | | Benefit classification |
| benefitCriteria | BENEFIT_CRITERIA | TEXT | | Benefit criteria |
| benefitValue | BENEFIT_VALUE | VARCHAR(255) | | Benefit value |
| paramCovgClassName | param_covg_class_name | VARCHAR(100) | | Parameter class |
| sparteEigen | SPARTE_EIGEN | VARCHAR(50) | | Sparte identifier |
| nonMedicalExpensesList | NON_MEDICAL_EXPENSES_LIST | TEXT | | Non-medical expenses |

---

### MemberDtls
**Table**: `t_member_dtls`
**Schema**: `GRPHEALTH`
**Purpose**: Individual member details within a transaction

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| memberDtlsId | member_dtls_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| empCode | EMP_CODE | VARCHAR(50) | | Employee code |
| firstName | FIRST_NAME | VARCHAR(100) | | First name |
| lastName | LAST_NAME | VARCHAR(100) | | Last name |
| relationship | RELATIONSHIP | VARCHAR(50) | | SELF/SPOUSE/CHILD/PARENT |
| gender | GENDER | VARCHAR(10) | | M/F |
| dateOfBirth | DATE_OF_BIRTH | DATE | | DOB |
| sumInsured | SUM_INSURED | DOUBLE | | Coverage amount |
| packageCode | package_code | VARCHAR(50) | | Package code |
| packages | PACKAGE | VARCHAR(100) | | Package name |
| memberId | MEMBER_ID | VARCHAR(50) | | Unique member ID |
| childPolicyNumber | CHILD_POLICY_NUMBER | VARCHAR(100) | | Individual policy |
| annualPremium | ANNUAL_PREMIUM | DOUBLE | | Annual premium |
| netPremium | NET_PREMIUM | DOUBLE | | Net premium |
| taxAmount | TAX_AMOUNT | DOUBLE | | Tax amount |
| grossPremium | GROSS_PREMIUM | DOUBLE | | Gross premium |
| isDgh | IS_DGH | BOOLEAN | | DGH flag |
| documentType | DOCUMENT_TYPE | VARCHAR(50) | | KYC document type |
| documentNumber | DOCUMENT_NUMBER | VARCHAR(100) | | KYC document number |
| employerName | EMPLOYER_NAME | VARCHAR(255) | | Employer name |
| absPartyId | ABS_PARTY_ID | VARCHAR(100) | | ABS party ID |
| processFlag | PROCESS_FLAG | BOOLEAN | | Processing status |
| statusId | STATUS_ID | VARCHAR(50) | | Status code |
| dateOfJoining | DATE_OF_JOINING | DATE | | Employment start |
| riskInceptionDate | RISK_INCEPTION_DATE | DATE | | Risk start date |
| riskExpiryDate | RISK_EXPIRY_DATE | DATE | | Risk end date |
| endorsementEffectiveDate | ENDORSEMENT_EFFECTIVE_DATE | DATE | | Endorsement date |
| endorsementDate | ENDORSEMENT_DATE | DATE | | Endorsement date |
| endorsementType | ENDORSEMENT_TYPE | VARCHAR(50) | | ADD/DELETE/MODIFY |
| endorsementReason | ENDORSEMENT_REASON | TEXT | | Reason for change |
| createdBy | CREATED_BY | VARCHAR(100) | | Creator |
| createdDate | CREATED_DATE | TIMESTAMP | AUTO | Created |
| updatedBy | UPDATED_BY | VARCHAR(100) | | Modifier |
| updatedDate | UPDATED_DATE | TIMESTAMP | AUTO | Modified |
| ctc | CTC | DOUBLE | | Cost to company |
| designation | DESIGNATION | VARCHAR(100) | | Job title |
| department | DEPARTMENT | VARCHAR(100) | | Department |
| askedSumInsured | ASKED_SUM_INSURED | DOUBLE | | Requested SI |
| taxPercent | tax_percent | DOUBLE | | Tax percentage |
| refundedPremium | REFUNDED_PREMIUM | DOUBLE | | Refund amount |
| errorMessage | ERROR_MESSAGE | TEXT | | Error details |
| age | AGE | INTEGER | | Calculated age |
| occupation | OCCUPATION | VARCHAR(100) | | Occupation |
| grade | GRADE | VARCHAR(50) | | Employee grade |

**Relationships**:
- `N:1` → QuoteTransaction (quote_trans_id)
- `1:N` → MemberAddress
- `1:N` → MemberLoanDtls
- `1:N` → MemberPaymentDtls
- `1:N` → MemberNominee
- `1:N` → MemberCoverageDtls
- `1:N` → MemberEndorsementReq

---

### QuoteClaimSummary
**Table**: `T_QUOTE_CLAIM_SUMMARY`
**Schema**: `GRPHEALTH`
**Purpose**: Historical claim summary by year

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| claimSummaryId | claim_summary_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteId | quote_id | INTEGER | FK | Parent quote |
| year | year | VARCHAR(10) | | Policy year |
| memAtStart | mem_at_start | INTEGER | | Members at inception |
| memAtEnd | mem_at_end | INTEGER | | Members at expiry |
| avgMember | avg_member | DOUBLE | | Average members |
| saAtStart | sa_at_start | BIGINT | | SI at inception |
| saAtEnd | sa_at_end | BIGINT | | SI at expiry |
| avgSa | avg_sa | BIGINT | | Average SI |
| noOfClaims | no_of_claims | INTEGER | | Total claims |
| claimAmount | claim_amount | DOUBLE | | Total claim amount |
| avgClmAmt | avg_clm_amt | BIGINT | | Average claim |
| crudMortCount | crud_mort_count | INTEGER | | Mortality count |
| crudMortAmt | crud_mort_amt | BIGINT | | Mortality amount |
| createdDate | created_date | TIMESTAMP | AUTO | Created |

---

### QuotePrevClaimDtls
**Table**: `t_quote_prev_claim_dtls`
**Schema**: `GRPHEALTH`
**Purpose**: Individual historical claim records

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| prevClaimId | prev_claim_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| claimHistoryUniqueId | claim_history_unique_id | VARCHAR(100) | | Unique claim ref |
| insuranceCompany | insurance_company | VARCHAR(255) | | Previous insurer |
| insuredCorporateName | insured_corporate_name | VARCHAR(255) | | Corporate name |
| imdName | imd_name | VARCHAR(255) | | Intermediary |
| policyNumber | policy_number | VARCHAR(100) | | Policy number |
| policyYear | policy_year | INTEGER | | Policy year |
| riskInceptionDate | risk_inception_date | DATE | | Risk start |
| riskExpiryDate | risk_expiry_date | DATE | | Risk end |
| empId | emp_id | VARCHAR(50) | | Employee ID |
| employeeName | employee_name | VARCHAR(255) | | Employee name |
| dateOfBirth | date_of_birth | DATE | | DOB |
| employeeGrade | employee_grade | VARCHAR(50) | | Grade |
| employeeDesignation | employee_designation | VARCHAR(100) | | Designation |
| memberId | member_id | VARCHAR(50) | | Member ID |
| patientName | patient_name | VARCHAR(255) | | Patient name |
| relation | relation | VARCHAR(50) | | Relationship |
| age | age | INTEGER | | Age at claim |
| gender | gender | VARCHAR(10) | | Gender |
| sumInsured | sum_insured | DOUBLE | | SI at claim |
| ctc | ctc | DOUBLE | | CTC |
| benefitAmount | benefit_amount | DOUBLE | | Benefit amount |
| claimsTypeCashReimb | claims_type_cash_reimb | VARCHAR(50) | | CASHLESS/REIMBURSEMENT |
| claimNo | claim_no | VARCHAR(100) | | Claim number |
| hospitalName | hospital_name | VARCHAR(255) | | Hospital |
| hospitalState | hospital_state | VARCHAR(100) | | Hospital state |
| hospitalCity | hospital_city | VARCHAR(100) | | Hospital city |
| claimStatus | claim_status | VARCHAR(50) | | Claim status |
| alOrPreAuthAmount | al_or_pre_auth_amount | DOUBLE | | Pre-auth amount |
| claimTypeIpdOpd | claim_type_ipd_opd | VARCHAR(20) | | IPD/OPD |
| registrationDate | registration_date | DATE | | Registration date |

---

### QuoteMemberInfo
**Table**: `t_quote_member_info`
**Schema**: `digit_health`
**Purpose**: Quick member info for quote generation

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| id | id | BIGINT | PK, AUTO_INCREMENT | Primary key |
| employeeCode | employee_code | VARCHAR(50) | | Employee code |
| employeeName | employee_name | VARCHAR(255) | | Employee name |
| joiningDate | joining_date | TIMESTAMP | | Join date |
| memberDob | member_dob | TIMESTAMP | | DOB |
| memberGender | member_gender | VARCHAR(10) | | Gender |
| employeeGrade | employee_grade | VARCHAR(50) | | Grade |
| dependentName | dependent_name | VARCHAR(255) | | Dependent name |
| relationship | relationship | VARCHAR(50) | | Relationship |
| dependentDob | dependent_dob | TIMESTAMP | | Dependent DOB |
| dependentGender | dependent_gender | VARCHAR(10) | | Dependent gender |
| sumInsured | sum_insured | DECIMAL(15,2) | | Sum insured |
| createDate | create_date | TIMESTAMP | AUTO | Created |
| updateDate | update_date | TIMESTAMP | AUTO | Updated |
| createdBy | created_by | VARCHAR(100) | | Creator |
| updatedBy | updated_by | VARCHAR(100) | | Updater |
| quoteNumber | quote_number | VARCHAR(100) | FK | Quote reference |
| empPackage | emp_package | VARCHAR(100) | | Package |
| status | status | VARCHAR(50) | | Status |
| memberId | member_id | VARCHAR(50) | | Member ID |
| remark | remark | TEXT | | Remarks |
| panNumber | pan_number | VARCHAR(20) | | PAN |
| gstNumber | gst_number | VARCHAR(20) | | GST |
| isNewlyAdded | is_newly_added | BOOLEAN | | New flag |
| claimNumber | claim_number | VARCHAR(100) | | Linked claim |
| isPolicyLinked | is_policylinked | BOOLEAN | | Policy linked |
| claimCreatedDate | claim_created_date | TIMESTAMP | | Claim date |

---

### LegalEntityDtls
**Table**: `t_legal_entity_details`
**Schema**: `GRPHEALTH`
**Purpose**: Corporate entity master

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| legalEntityId | legal_entity_id | INTEGER | PK, AUTO_INCREMENT | Primary key |
| insuredName | insured_name | VARCHAR(255) | | Company name |
| industryType | industry_type | VARCHAR(100) | | Industry classification |
| mobile | mobile | VARCHAR(20) | | Contact mobile |
| email | email | VARCHAR(255) | | Contact email |
| address | address | TEXT | | Full address |
| city | city | VARCHAR(100) | | City |
| district | DISTRICT | VARCHAR(100) | | District |
| state | state | VARCHAR(100) | | State |
| pincode | pincode | INTEGER | | Postal code |
| gstNumber | gst_number | VARCHAR(20) | | GST registration |
| panNumber | pan_number | VARCHAR(20) | | PAN number |
| gstUrl | gst_URL | VARCHAR(500) | | GST certificate URL |
| gstOcrDtls | gst_OCR_dtls | TEXT | | OCR extracted data |
| absLocationAddendum | abs_location_addendum | TEXT | | Location addendum |
| createdBy | created_by | VARCHAR(100) | | Creator |
| createdDate | created_date | TIMESTAMP | AUTO | Created |
| updatedBy | updated_by | VARCHAR(100) | | Updater |
| updatedDate | updated_date | TIMESTAMP | AUTO | Updated |
| absCompanyId | ABS_COMPANY_ID | VARCHAR(100) | | ABS company ID |
| parentEntityId | parent_entity_id | INTEGER | FK (self) | Parent company |
| gstRegistrationDate | GST_REGISTRATION_DATE | DATE | | GST registration date |

---

### QuoteHospitalDtls
**Table**: `T_QUOTE_HOSPITALSDTLS`
**Schema**: `GRPHEALTH`
**Purpose**: Hospital network and restrictions

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| hospitalId | HOSPITAL_ID | INTEGER | PK, AUTO_INCREMENT | Primary key |
| quoteOptionId | QUOTE_OPTION_ID | INTEGER | FK | Parent option |
| rohiniCode | ROHINI_ID | VARCHAR(50) | | ROHINI code |
| hospitalName | HOSP_NAME | VARCHAR(255) | | Hospital name |
| hospitalAddress | HOSP_ADDRESS | TEXT | | Address |
| city | HOSP_CITY | VARCHAR(100) | | City |
| state | HOSP_STATE | VARCHAR(100) | | State |
| pincode | HOSP_PINCODE | INTEGER | | Pincode |
| restrictionType | REST_TYPE | VARCHAR(50) | | INCLUDE/EXCLUDE |
| copay | COPAY | INTEGER | | Copay percentage |
| updatedDate | UPDATED_DATE | TIMESTAMP | AUTO | Updated |

---

## 2.3 HEALTH PAYEE/CLAIMS ENTITIES (digit_health schema)

### HealthPayeeEntity
**Table**: `t_health_payee`
**Schema**: `digit_health`
**Purpose**: Claim payment beneficiary details

| Field Name | Column Name | Data Type | Constraints | Notes |
|---|---|---|---|---|
| requestId | request_id | BIGINT | PK, AUTO_INCREMENT | Primary key |
| policyNumber | policy_number | VARCHAR(100) | | Policy reference |
| claimNumber | claim_number | VARCHAR(100) | | Claim reference |
| payeeName | payee_name | VARCHAR(255) | | Beneficiary name |
| ifscCode | ifsc_code | VARCHAR(20) | | Bank IFSC |
| bankName | bank_name | VARCHAR(255) | | Bank name |
| accountNumber | account_number | VARCHAR(50) | | Account number |
| createDate | create_date | DATE | | Created |
| updateDate | update_date | DATE | | Updated |
| createdBy | created_by | VARCHAR(100) | | Creator |
| updatedBy | updated_by | VARCHAR(100) | | Updater |
| remark | remark | TEXT | | Remarks |
| liabilityAmount | liability_amount | INTEGER | | Claim liability |
| payeeType | payee_type | VARCHAR(50) | | HOSPITAL/INSURED/NOMINEE |
| dob | dob | DATE | | DOB |
| mobileNumber | mobile_number | VARCHAR(20) | | Mobile |
| email | email | VARCHAR(255) | | Email |
| gender | gender | VARCHAR(10) | | Gender |
| address | address | TEXT | | Address |
| pincode | pincode | VARCHAR(10) | | Pincode |
| city | city | VARCHAR(100) | | City |
| state | state | VARCHAR(100) | | State |
| imd | imd | VARCHAR(100) | | Intermediary |
| panNumber | pan_number | VARCHAR(20) | | PAN |
| aadharNumber | aadhar_number | VARCHAR(20) | | Aadhaar |
| invoiceDate | invoice_date | DATE | | Invoice date |
| nomineeRole | nominee_role | VARCHAR(50) | | Nominee role |
| createdUsername | created_username | VARCHAR(100) | | Creator username |
| updatedUsername | updated_username | VARCHAR(100) | | Updater username |
| tdsAmount | tds_amount | DECIMAL(15,2) | | TDS amount |
| netPaidAmount | net_paid_amount | DECIMAL(15,2) | | Net payment |
| utrNumber | utr_number | VARCHAR(100) | | UTR reference |
| recieptNumber | reciept_number | VARCHAR(100) | | Receipt number |
| status | status | VARCHAR(50) | | Payment status |

---

# PART 3: SYNTHETIC DATA SCHEMA FOR POLICYMIND

Based on your source entities, here's the synthetic schema for the PolicyMind project:

## 3.1 Policy Documents Table (for RAG)

```sql
CREATE TABLE policies (
    policy_id SERIAL PRIMARY KEY,
    policy_number VARCHAR(100) UNIQUE NOT NULL,
    policy_type VARCHAR(50) NOT NULL,  -- GROUP_HEALTH, INDIVIDUAL_HEALTH
    product_code VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    insured_name VARCHAR(255) NOT NULL,  -- Company name
    industry_type VARCHAR(100),
    policy_start_date DATE NOT NULL,
    policy_end_date DATE NOT NULL,
    total_lives INTEGER,
    total_sum_insured DECIMAL(15,2),
    premium_amount DECIMAL(15,2),
    tpa_name VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    document_s3_link VARCHAR(500),  -- Link to policy PDF
    embedding VECTOR(1536)  -- OpenAI embedding dimension
);
```

## 3.2 Policy Chunks Table (for RAG retrieval)

```sql
CREATE TABLE policy_chunks (
    chunk_id SERIAL PRIMARY KEY,
    policy_id INTEGER REFERENCES policies(policy_id),
    section_type VARCHAR(100) NOT NULL,  -- COVERAGE, EXCLUSION, LIMIT, DEDUCTIBLE, WAITING_PERIOD, COPAY
    section_name VARCHAR(255),
    content TEXT NOT NULL,
    page_number INTEGER,
    start_char INTEGER,
    end_char INTEGER,
    embedding VECTOR(1536),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Section types for insurance policies:
-- COVERAGE: What is covered
-- EXCLUSION: What is not covered
-- LIMIT: Coverage limits and sub-limits
-- DEDUCTIBLE: Deductible amounts
-- WAITING_PERIOD: Waiting periods for conditions
-- COPAY: Co-payment requirements
-- NETWORK: Hospital network details
-- PRE_AUTH: Pre-authorization requirements
-- CLAIM_PROCESS: Claim submission process
```

## 3.3 Coverage Details Table

```sql
CREATE TABLE coverages (
    coverage_id SERIAL PRIMARY KEY,
    policy_id INTEGER REFERENCES policies(policy_id),
    coverage_code VARCHAR(50) NOT NULL,
    coverage_name VARCHAR(255) NOT NULL,
    coverage_type VARCHAR(50),  -- BASE, ADDON, RIDER
    sum_type VARCHAR(50),  -- AGGREGATE, PER_CLAIM, PER_PERSON
    coverage_value DECIMAL(15,2),
    coverage_limit DECIMAL(15,2),
    sub_limit_percentage DECIMAL(5,2),
    deductible_amount DECIMAL(15,2),
    copay_percentage DECIMAL(5,2),
    waiting_period_days INTEGER,
    is_mandatory BOOLEAN DEFAULT true,
    benefit_criteria TEXT,
    exclusion_notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3.4 Members Table

```sql
CREATE TABLE members (
    member_id SERIAL PRIMARY KEY,
    policy_id INTEGER REFERENCES policies(policy_id),
    employee_code VARCHAR(50),
    member_name VARCHAR(255) NOT NULL,
    relationship VARCHAR(50) NOT NULL,  -- SELF, SPOUSE, CHILD, PARENT
    gender VARCHAR(10),
    date_of_birth DATE,
    age INTEGER,
    sum_insured DECIMAL(15,2),
    premium_amount DECIMAL(15,2),
    package_name VARCHAR(100),
    enrollment_date DATE,
    risk_inception_date DATE,
    risk_expiry_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3.5 Claims Table

```sql
CREATE TABLE claims (
    claim_id SERIAL PRIMARY KEY,
    claim_number VARCHAR(100) UNIQUE NOT NULL,
    policy_id INTEGER REFERENCES policies(policy_id),
    member_id INTEGER REFERENCES members(member_id),
    claim_type VARCHAR(50) NOT NULL,  -- CASHLESS, REIMBURSEMENT
    claim_category VARCHAR(50),  -- IPD, OPD, DAYCARE
    diagnosis_code VARCHAR(20),  -- ICD-10 code
    diagnosis_description VARCHAR(500),
    treatment_type VARCHAR(255),
    hospital_name VARCHAR(255),
    hospital_city VARCHAR(100),
    hospital_state VARCHAR(100),
    hospital_rohini_code VARCHAR(50),
    admission_date DATE,
    discharge_date DATE,
    claim_amount DECIMAL(15,2),
    approved_amount DECIMAL(15,2),
    deductible_applied DECIMAL(15,2),
    copay_applied DECIMAL(15,2),
    net_payable DECIMAL(15,2),
    claim_status VARCHAR(50),  -- REGISTERED, UNDER_PROCESS, APPROVED, SETTLED, REJECTED
    registration_date DATE,
    settlement_date DATE,
    rejection_reason TEXT,
    adjuster_notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3.6 ICD Codes Reference Table

```sql
CREATE TABLE icd_codes (
    icd_code VARCHAR(20) PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    category VARCHAR(255),
    is_chronic BOOLEAN DEFAULT false,
    is_pre_existing_condition BOOLEAN DEFAULT false,
    typical_hospitalization_days INTEGER,
    typical_treatment_cost DECIMAL(15,2)
);
```

## 3.7 Hospital Network Table

```sql
CREATE TABLE hospitals (
    hospital_id SERIAL PRIMARY KEY,
    rohini_code VARCHAR(50) UNIQUE,
    hospital_name VARCHAR(255) NOT NULL,
    hospital_type VARCHAR(50),  -- NETWORK, NON_NETWORK
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    tier VARCHAR(20),  -- TIER_1, TIER_2, TIER_3
    specializations TEXT[],
    is_active BOOLEAN DEFAULT true
);
```

## 3.8 Evaluation Dataset Table

```sql
CREATE TABLE eval_questions (
    question_id SERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    ground_truth_answer TEXT NOT NULL,
    query_type VARCHAR(50) NOT NULL,  -- DOCUMENT_QA, CLAIMS_SQL, HYBRID
    difficulty VARCHAR(20),  -- EASY, MEDIUM, HARD
    relevant_policy_ids INTEGER[],
    relevant_chunk_ids INTEGER[],
    expected_sql TEXT,  -- For SQL questions
    metadata JSONB,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

# PART 4: SAMPLE DATA GENERATION PROMPTS

## 4.1 Policy Document Generation Prompt

Use this prompt with Claude/GPT-4 to generate realistic policy documents:

```
Generate a synthetic health insurance policy document for a GROUP HEALTH INSURANCE policy with the following structure:

Policy Number: GHI-2024-{random 6 digits}
Insured Company: {realistic Indian company name}
Industry: {IT/Manufacturing/Banking/Pharma/Retail}
Policy Period: 1 year from today
Number of Lives: {50-500}
Sum Insured Options: 3L, 5L, 10L, 15L, 25L

Include these sections with realistic insurance language:

1. DEFINITIONS
   - Define key terms: Sum Insured, Deductible, Co-payment, Pre-existing Disease, Waiting Period, Network Hospital, etc.

2. COVERAGE DETAILS
   - In-Patient Hospitalization (Room rent limits, ICU charges)
   - Pre and Post Hospitalization (30/60 days)
   - Day Care Procedures (list 10-15 procedures)
   - Ambulance Charges
   - Domiciliary Hospitalization
   - Organ Donor Expenses
   - Maternity Coverage (if applicable)
   - New Born Baby Cover

3. EXCLUSIONS
   - General exclusions (cosmetic surgery, adventure sports, etc.)
   - Specific disease exclusions with waiting periods
   - Pre-existing disease clause

4. WAITING PERIODS
   - Initial waiting period: 30 days
   - Specific disease waiting period: 24 months
   - Pre-existing disease waiting period: 36 months

5. CLAIM PROCESS
   - Cashless claim procedure
   - Reimbursement claim procedure
   - Required documents
   - Timelines

6. SUB-LIMITS
   - Room rent: 1% of SI or actuals
   - ICU: 2% of SI
   - Ambulance: Rs. 2000 per hospitalization
   - Specific procedure limits

7. CO-PAYMENT
   - Age-based co-payment (if any)
   - Non-network hospital co-payment

8. DEDUCTIBLES
   - Per claim deductible
   - Aggregate deductible

Output as structured JSON with each section clearly marked with page numbers.
```

## 4.2 Claims Data Generation Prompt

```
Generate 100 synthetic health insurance claims with realistic distribution:

Claim Distribution:
- 60% CASHLESS, 40% REIMBURSEMENT
- 80% IPD, 15% DAYCARE, 5% OPD
- 70% APPROVED, 15% SETTLED, 10% UNDER_PROCESS, 5% REJECTED

For each claim include:
- claim_number: CLM-{YYYY}-{6 random digits}
- policy_id: Reference to existing policy
- member_id: Reference to existing member
- diagnosis_code: Valid ICD-10 code
- diagnosis_description: Matching description
- hospital_name: Realistic Indian hospital name
- admission_date, discharge_date: Realistic duration based on diagnosis
- claim_amount: Realistic amount for the diagnosis (range 10K to 15L)
- approved_amount: Less than or equal to claim_amount
- deductible_applied: Based on policy terms
- copay_applied: Based on policy terms (if age > 60)

Common diagnoses to include:
- Dengue fever (A90)
- Viral fever with complications (B34.9)
- Appendectomy (K35.80)
- Kidney stones (N20.0)
- Fractures (S72.001A)
- Cardiac conditions (I25.10)
- Diabetes complications (E11.9)
- Maternity (O80)

Output as JSON array with all fields populated.
```

## 4.3 Evaluation Questions Generation Prompt

```
Generate 50 evaluation questions for a health insurance RAG system:

Question Types:
1. DOCUMENT_QA (20 questions) - Answerable from policy documents
   Examples:
   - "What is the waiting period for knee replacement surgery?"
   - "Is dental treatment covered under this policy?"
   - "What is the sub-limit for room rent?"

2. CLAIMS_SQL (20 questions) - Answerable from claims database
   Examples:
   - "What was the total claim amount in Q1 2024?"
   - "How many maternity claims were filed last year?"
   - "What is the average claim amount for cardiac conditions?"

3. HYBRID (10 questions) - Requires both documents and data
   Examples:
   - "Are the claims for patient John Doe within his coverage limits?"
   - "What claims were rejected and what exclusion clause applied?"

For each question provide:
- question_id
- question
- ground_truth_answer
- query_type
- difficulty (EASY/MEDIUM/HARD)
- relevant_sources (policy sections or table references)
- expected_sql (for SQL questions)

Output as JSON array.
```

---

# PART 5: SYSTEM ARCHITECTURE

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PolicyMind System                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────┐    ┌──────────────────────────────────────────────┐  │
│  │  FastAPI │───▶│            LangGraph Orchestrator            │  │
│  │   /ask   │    │  ┌────────────────────────────────────────┐  │  │
│  └──────────┘    │  │           Query Classifier              │  │  │
│                  │  │  (document_qa / claims_sql / hybrid)    │  │  │
│                  │  └───────────────┬────────────────────────┘  │  │
│                  │                  │                            │  │
│                  │    ┌─────────────┼─────────────┐              │  │
│                  │    ▼             ▼             ▼              │  │
│                  │ ┌──────┐   ┌──────────┐   ┌─────────┐         │  │
│                  │ │ RAG  │   │   SQL    │   │ Hybrid  │         │  │
│                  │ │Agent │   │  Agent   │   │  Agent  │         │  │
│                  │ └──┬───┘   └────┬─────┘   └────┬────┘         │  │
│                  │    │            │              │              │  │
│                  └────┼────────────┼──────────────┼──────────────┘  │
│                       │            │              │                 │
│         ┌─────────────┼────────────┼──────────────┼───────────┐     │
│         │             ▼            ▼              ▼           │     │
│         │  ┌──────────────┐  ┌───────────┐  ┌────────────┐    │     │
│         │  │   pgvector   │  │ PostgreSQL│  │   Both +   │    │     │
│         │  │  (policies)  │  │  (claims) │  │  Citation  │    │     │
│         │  └──────────────┘  └───────────┘  └────────────┘    │     │
│         │              Data Layer                             │     │
│         └─────────────────────────────────────────────────────┘     │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Supporting Services                      │    │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐   │    │
│  │  │  Evaluation  │  │ Observability│  │   Deterministic  │   │    │
│  │  │   (RAGAS)    │  │  (LangSmith) │  │   Math Engine    │   │    │
│  │  └──────────────┘  └──────────────┘  └──────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
```

---

# PART 6: TECHNOLOGY STACK

## Current Skills → AI Stack Translation

| Java/Spring Boot | Python/AI Equivalent | Notes |
|-----------------|---------------------|-------|
| `@RestController` | FastAPI `@router` | Async by default |
| `@Service` | Service classes | Same pattern |
| `@Repository` + JPA | SQLAlchemy + Repository | ORM identical |
| Strategy Pattern | LangGraph Tool Routing | Validation → Agent tools |
| `@Async` | `asyncio` | Different syntax |
| Redis caching | Redis + semantic cache | Add embeddings |
| Kafka events | LangGraph state machine | Event → state |
| `@ControllerAdvice` | FastAPI exception handlers | Same concept |
| Mockito | pytest + mock | Same TDD |

## Required Dependencies

```
# pyproject.toml or requirements.txt

# Core
python = "^3.11"
fastapi = "^0.109.0"
uvicorn = "^0.27.0"
pydantic = "^2.6.0"

# Database
sqlalchemy = "^2.0.25"
asyncpg = "^0.29.0"
pgvector = "^0.2.4"

# AI/LLM
langchain = "^0.1.0"
langgraph = "^0.0.20"
langsmith = "^0.0.87"
openai = "^1.10.0"

# Embeddings & Search
sentence-transformers = "^2.3.1"
rank-bm25 = "^0.2.2"
cohere = "^4.45"  # for reranking

# Evaluation
ragas = "^0.1.0"
deepeval = "^0.20.0"

# Document Processing
pypdf2 = "^3.0.1"
unstructured = "^0.12.0"

# Utilities
python-dotenv = "^1.0.0"
httpx = "^0.26.0"
tenacity = "^8.2.3"  # retry logic
```

---

# PART 7: WEEK-BY-WEEK PLAN

## Week 1: Foundation + RAG Pipeline

**Goals**:
- Set up Python/FastAPI project structure
- Ingest synthetic policy documents
- Implement basic RAG retrieval

**Deliverables**:
- [ ] FastAPI skeleton with health endpoint
- [ ] PostgreSQL + pgvector schema deployed
- [ ] 20 synthetic policy documents generated and chunked
- [ ] Basic vector search returning relevant chunks
- [ ] Simple `/ask` endpoint answering document questions

**Demo Milestone**: Query "What is the deductible for surgery?" and get a relevant chunk with page citation.

---

## Week 2: Agentic Routing + SQL Agent

**Goals**:
- Implement LangGraph orchestrator
- Add query classification
- Build SQL agent for claims queries

**Deliverables**:
- [ ] LangGraph state machine with 3 paths (rag/sql/hybrid)
- [ ] Query classifier distinguishing document vs. data questions
- [ ] SQL agent with self-correction (retry on error)
- [ ] Synthetic claims table with 5,000 records
- [ ] Deterministic math engine for coverage calculations

**Demo Milestone**: "What was the total claim amount for policy P-1234 in 2024?" returns correct SQL-derived answer.

---

## Week 3: Hybrid Search + Citations + Observability

**Goals**:
- Implement hybrid vector + BM25 search
- Add citation grounding to all responses
- Integrate observability (LangSmith or Phoenix)

**Deliverables**:
- [ ] RRF fusion combining vector + keyword results
- [ ] Reranker integration (Cohere or cross-encoder)
- [ ] Every response includes [Source: Policy X, Section Y, Page Z]
- [ ] LangSmith traces for every query
- [ ] Latency metrics collection

**Demo Milestone**: "Is knee replacement surgery covered?" returns answer citing specific policy section and page.

---

## Week 4: Evaluation + Polish + Documentation

**Goals**:
- Implement RAGAS evaluation suite
- Create evaluation dashboard
- Write documentation and README

**Deliverables**:
- [ ] 50-question evaluation dataset with ground truth
- [ ] RAGAS metrics: faithfulness, relevance, precision, recall
- [ ] Comparison report: hybrid vs. vector-only ablation
- [ ] README with architecture diagram, setup instructions
- [ ] Docker Compose for one-command deployment
- [ ] 3-minute demo video

**Demo Milestone**: Run full evaluation, show metrics dashboard, explain trade-offs.

---

# PART 8: FOLDER STRUCTURE

```
policymind/
├── domain/                     # Core business logic (no dependencies)
│   ├── entities/
│   │   ├── policy.py           # Policy, PolicyChunk domain objects
│   │   ├── claim.py            # Claim domain object
│   │   └── query.py            # Query, QueryResult value objects
│   ├── services/
│   │   ├── coverage_calculator.py  # Deterministic math (never LLM)
│   │   └── citation_builder.py     # Build source citations
│   └── interfaces/             # Abstract repos/ports
│       ├── policy_repository.py
│       └── claim_repository.py
│
├── application/                # Use cases / orchestration
│   ├── query_handler.py        # Main entry point for /ask
│   ├── agents/
│   │   ├── query_classifier.py # Route to rag/sql/hybrid
│   │   ├── rag_agent.py        # Document retrieval + generation
│   │   ├── sql_agent.py        # NL-to-SQL with retry
│   │   └── hybrid_agent.py     # Orchestrate both
│   └── graph/
│       └── orchestrator.py     # LangGraph state machine
│
├── infrastructure/             # External dependencies
│   ├── database/
│   │   ├── postgres.py         # SQLAlchemy engine
│   │   └── pgvector.py         # Vector operations
│   ├── llm/
│   │   ├── openai_client.py    # OpenAI wrapper
│   │   └── embeddings.py       # Embedding generation
│   ├── search/
│   │   ├── hybrid_search.py    # RRF fusion implementation
│   │   └── reranker.py         # Cohere/cross-encoder
│   └── observability/
│       ├── langsmith.py        # Tracing setup
│       └── metrics.py          # Prometheus metrics
│
├── api/                        # FastAPI layer
│   ├── main.py                 # App entry point
│   ├── routes/
│   │   ├── ask.py              # POST /ask endpoint
│   │   ├── health.py           # GET /health
│   │   └── eval.py             # POST /eval/run
│   └── schemas/
│       ├── request.py          # Pydantic request models
│       └── response.py         # Pydantic response models
│
├── evaluation/                 # RAGAS + custom metrics
│   ├── ragas_runner.py         # Run RAGAS evaluation
│   ├── metrics/
│   │   ├── faithfulness.py
│   │   ├── relevance.py
│   │   └── retrieval.py
│   ├── datasets/
│   │   └── eval_questions.json # Ground truth Q&A
│   └── reports/
│       └── generate_report.py
│
├── data/                       # Data generation + seeding
│   ├── generators/
│   │   ├── policy_generator.py # Synthetic policy docs
│   │   └── claims_generator.py # Synthetic claims data
│   ├── seed/
│   │   └── icd_codes.csv       # Public ICD-10 codes
│   └── documents/              # Generated policy PDFs
│
├── tests/
│   ├── unit/
│   │   ├── test_coverage_calculator.py
│   │   └── test_citation_builder.py
│   ├── integration/
│   │   ├── test_rag_agent.py
│   │   └── test_sql_agent.py
│   └── e2e/
│       └── test_full_query.py
│
├── docker-compose.yml
├── Dockerfile
├── pyproject.toml
├── README.md
└── .env.example
```

---

# PART 9: EVALUATION FRAMEWORK

## Target Metrics

| Metric | Target | Stretch Goal |
|--------|--------|--------------|
| Precision@5 | ≥ 0.70 | ≥ 0.80 |
| Recall@5 | ≥ 0.65 | ≥ 0.75 |
| MRR | ≥ 0.60 | ≥ 0.70 |
| Faithfulness | ≥ 0.85 | ≥ 0.92 |
| Answer Relevance | ≥ 0.80 | ≥ 0.88 |
| Hallucination Rate | ≤ 10% | ≤ 5% |
| Latency p50 | ≤ 3s | ≤ 2s |
| Latency p95 | ≤ 8s | ≤ 5s |
| Cost per Query | ≤ $0.05 | ≤ $0.03 |

---

# PART 10: RESUME POSITIONING

## Resume Bullet Points

**1. Technical Achievement**:
> Built an agentic RAG system for insurance claims research using LangGraph, combining vector retrieval (pgvector), SQL query generation, and deterministic coverage calculations, achieving 85% faithfulness and 70% retrieval precision on a 50-question evaluation benchmark.

**2. Architecture/Design**:
> Designed a hybrid search pipeline (vector + BM25) with Reciprocal Rank Fusion and reranking, reducing hallucination rate to <10% through citation-grounded generation and deterministic math for coverage calculations.

**3. Evaluation/Production Readiness**:
> Implemented end-to-end LLM evaluation using RAGAS (faithfulness, relevance, context precision), LangSmith observability, and latency tracking, establishing a reproducible evaluation workflow for RAG system iteration.

## LinkedIn Project Description

> **PolicyMind — Agentic RAG for Insurance Research**
>
> An AI-powered research assistant that answers insurance claims questions by intelligently routing between document retrieval (policy PDFs) and structured data queries (claims database). Built with Python, FastAPI, LangGraph, and PostgreSQL + pgvector.
>
> Key features:
> • Agentic tool routing: Classifies queries and orchestrates RAG, SQL, or hybrid workflows
> • Citation-grounded answers: Every claim links to source documents and page numbers
> • Self-correcting SQL: Executes queries, catches errors, and retries with context
> • Deterministic math: Coverage calculations never trust LLM arithmetic
> • Full evaluation suite: RAGAS metrics, LangSmith tracing, latency monitoring
>
> Technical highlights: Hybrid vector + BM25 search, RRF fusion, structured output enforcement, 85% faithfulness score.

---

# PART 11: ENTITY RELATIONSHIP SUMMARY

## Key Relationships from Your Codebase

```
EnrolmentDetails (1) ─────┬───────► (N) EnrolmentReminder
                         ├───────► (N) EnrolmentSumInsured
                         ├───────► (N) EnrolmentEmployeePaymentMode
                         ├───────► (N) EnrolmentKycDocumentType
                         └───────► (N) EnrolmentFamilyCompOutsidePolicy

QuoteDtls (1) ────────────┬───────► (N) QuoteOption
                         ├───────► (N) QuoteClaimSummary
                         └───────► (N) QuoteMasterPolicy

QuoteOption (1) ──────────┬───────► (N) QuotePackages
                         ├───────► (N) QuoteMemberPremium
                         ├───────► (N) QuotePrevPolDtls
                         ├───────► (N) QuotePrevClaimDtls
                         └───────► (N) QuoteHospitalDtls

QuotePackages (1) ────────┴───────► (N) QuotePackageCoverages

MemberDtls (1) ───────────┬───────► (N) MemberAddress
                         ├───────► (N) MemberPaymentDtls
                         ├───────► (N) MemberCoverageDtls
                         └───────► (N) MemberNominee

QuoteMasterPolicy (1) ────┬───────► (N) QuoteTransaction
                         └───────► (N) QuoteCommunicationDtls
```

## Synthetic Schema Relationships

```
policies (1) ─────────────┬───────► (N) policy_chunks
                         ├───────► (N) coverages
                         ├───────► (N) members
                         └───────► (N) claims

members (1) ──────────────┴───────► (N) claims

hospitals (N) ◄───────────────────► (N) claims (via hospital_name)

icd_codes (1) ◄───────────────────► (N) claims (via diagnosis_code)
```

---

*Generated: 2026-07-28*
*Based on analysis of GO Digit Insurance microservices codebase*
*Total entities analyzed: 50+*
*Total fields documented: 500+*
