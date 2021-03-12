package dto;

import java.util.List;

public class BatchProjectDTO {
    private List<BatchSchedule> batchScheduleList;
    private List<BatchProvide> batchProvideList;

    public List<BatchSchedule> getBatchScheduleList() {
        return batchScheduleList;
    }

    public void setBatchScheduleList(List<BatchSchedule> batchScheduleList) {
        this.batchScheduleList = batchScheduleList;
    }

    public List<BatchProvide> getBatchProvideList() {
        return batchProvideList;
    }

    public void setBatchProvideList(List<BatchProvide> batchProvideList) {
        this.batchProvideList = batchProvideList;
    }
}
