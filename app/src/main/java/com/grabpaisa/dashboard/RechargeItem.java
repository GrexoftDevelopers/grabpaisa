package com.grabpaisa.dashboard;

/**
 * Created by tahir on 5/8/2016.
 */
public class RechargeItem {

    private String sNo;

    private String amount;

    private String operatorName;

    private String status;

    private String requestDate;

    private String approvedDate;

    public RechargeItem(String sNo, String amount, String operatorName, String status, String requestDate, String approvedDate) {
        this.amount = amount;
        this.operatorName = operatorName;
        this.status = status;
        this.requestDate = requestDate;
        this.approvedDate = approvedDate;
        this.sNo = sNo;
    }

    public String getAmount() {
        return amount;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public String getsNo() {
        return sNo;
    }
}
