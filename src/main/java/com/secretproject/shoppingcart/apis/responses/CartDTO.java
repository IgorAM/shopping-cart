package com.secretproject.shoppingcart.apis.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDTO implements ApiResponse {

    private String uuid;
    private List<CartProductDTO> cartProducts;
    private BigDecimal totalAmount;

    public CartDTO() {
    }

    public void addCartProduct(CartProductDTO cartProductDTO){
        if(CollectionUtils.isEmpty(cartProducts)){
            cartProducts = new ArrayList<>();
        }

        cartProducts.add(cartProductDTO);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CartProductDTO> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProductDTO> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public BigDecimal getTotalAmount() {
        if(totalAmount == null){
            return new BigDecimal(0);
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDTO that = (CartDTO) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(cartProducts, that.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, cartProducts);
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "uuid='" + uuid + '\'' +
                ", cartProducts=" + cartProducts +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public void addTotalAmount(BigDecimal totalProductAmount) {
        setTotalAmount(getTotalAmount().add(totalProductAmount) );
    }
}
