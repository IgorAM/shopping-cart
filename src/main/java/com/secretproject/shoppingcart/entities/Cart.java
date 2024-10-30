package com.secretproject.shoppingcart.entities;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="cart_id")
    private List<CartProduct> cartProducts;

    public Cart() {
    }

    public Cart(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CartProduct> getCartProducts() {
        if(cartProducts == null){
            return new ArrayList<>();
        }

        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public void addAllCartProducts(List<CartProduct> newCartProducts){
        if(cartProducts == null){
            cartProducts = new ArrayList<>();
        }
        cartProducts.addAll(newCartProducts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(uuid, cart.uuid) && Objects.equals(cartProducts, cart.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, cartProducts);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", cartProducts=" + cartProducts +
                '}';
    }
}
