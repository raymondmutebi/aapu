package org.pahappa.systems.security;

public class BalanceResponse {

    private String Status;
    private String Balance;
    private BusinessResponse business;

    /**
     * @return the status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        Status = status;
    }

    /**
     * @return the balance
     */
    public String getBalance() {
        return Balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(String balance) {
        Balance = balance;
    }

    public BusinessResponse getBusiness() {
        return business;
    }

    public void setBusiness(BusinessResponse business) {
        this.business = business;
    }

}
