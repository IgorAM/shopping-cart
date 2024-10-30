package com.secretproject.shoppingcart.apis.requests;

import java.util.Objects;

public class CartRequest {

    private String uuid;
    private String sku;
    private int quantity;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartRequest that = (CartRequest) o;
        return quantity == that.quantity && Objects.equals(uuid, that.uuid) && Objects.equals(sku, that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sku, quantity);
    }

    @Override
    public String toString() {
        return "CartRequest{" +
                "uuid='" + uuid + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
