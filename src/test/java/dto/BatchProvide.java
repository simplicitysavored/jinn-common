package dto;

import java.util.Date;

public class BatchProvide {
    private Date naturalDay;
    private Integer businessDay;
    private String env;
    private String type;
    private String progressRate;
    private String remark;

    public Date getNaturalDay() {
        return naturalDay;
    }

    public void setNaturalDay(Date naturalDay) {
        this.naturalDay = naturalDay;
    }

    public Integer getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(Integer businessDay) {
        this.businessDay = businessDay;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(String progressRate) {
        this.progressRate = progressRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
