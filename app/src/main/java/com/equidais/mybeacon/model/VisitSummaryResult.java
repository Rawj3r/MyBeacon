package com.equidais.mybeacon.model;

/**
 * Created by daydreamer on 8/12/2015.
 */
public class VisitSummaryResult {
    public Long VisitCount;
    public Long AvgVisitDuration;
    public String LastVisitDate;

    public VisitSummaryResult(){
        VisitCount = null;
        AvgVisitDuration = null;
        LastVisitDate = "";
    }
}
