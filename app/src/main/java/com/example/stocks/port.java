package com.example.stocks;

public class port {
    private String ticker;
    private String share;
    private String market;
    private String total;

    // Constructor
    public port(String ticker, String share, String market, String total) {
        this.ticker = ticker;
        this.share = share;
        this.market = market;
        this.total=total;
    }
    // Getter and Setter
    public String getTicker1() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getTotal1() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
