package model;

public class FlashSale {
    private int flashSaleId;
    private int productId;
    private double discountPercent;
    private String startTime;
    private String endTime;
    private String status;

    public FlashSale(int flashSaleId, int productId, double discountPercent, String startTime, String endTime, String status) {
        this.flashSaleId = flashSaleId;
        this.productId = productId;
        this.discountPercent = discountPercent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public FlashSale() {
    }

    public int getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(int flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
