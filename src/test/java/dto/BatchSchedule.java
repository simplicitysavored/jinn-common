package dto;

import java.util.Date;

public class BatchSchedule {
    private String jobName;
    private String jobType;
    private String jobDesc;
    private String jobDependence;
    private String jobRunCycle;
    private Date jobRunDate;
    private String jobFlow;
    private String contactPerson;
    private String devStatus;
    private String testStatus;
    private String developer;
    private String dataSource;
    private String remark;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobDependence() {
        return jobDependence;
    }

    public void setJobDependence(String jobDependence) {
        this.jobDependence = jobDependence;
    }

    public String getJobRunCycle() {
        return jobRunCycle;
    }

    public void setJobRunCycle(String jobRunCycle) {
        this.jobRunCycle = jobRunCycle;
    }

    public Date getJobRunDate() {
        return jobRunDate;
    }

    public void setJobRunDate(Date jobRunDate) {
        this.jobRunDate = jobRunDate;
    }

    public String getJobFlow() {
        return jobFlow;
    }

    public void setJobFlow(String jobFlow) {
        this.jobFlow = jobFlow;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
