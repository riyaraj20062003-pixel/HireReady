package com.hireready.model;

/**
 * Model representing a Certification entry in a Resume.
 */
public class Certification {
    private int certId;
    private int resumeId;
    private String certName;
    private String issuingOrg;
    private int issueYear;

    public Certification() {
    }

    public Certification(String certName, String issuingOrg, int issueYear) {
        this.certName = certName;
        this.issuingOrg = issuingOrg;
        this.issueYear = issueYear;
    }

    public Certification(int certId, int resumeId, String certName, String issuingOrg, int issueYear) {
        this.certId = certId;
        this.resumeId = resumeId;
        this.certName = certName;
        this.issuingOrg = issuingOrg;
        this.issueYear = issueYear;
    }

    public int getCertId() {
        return certId;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getIssuingOrg() {
        return issuingOrg;
    }

    public void setIssuingOrg(String issuingOrg) {
        this.issuingOrg = issuingOrg;
    }

    public int getIssueYear() {
        return issueYear;
    }

    public void setIssueYear(int issueYear) {
        this.issueYear = issueYear;
    }
}
