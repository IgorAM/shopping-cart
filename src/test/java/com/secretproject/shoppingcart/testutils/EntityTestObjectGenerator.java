package com.secretproject.shoppingcart.testutils;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import com.secretproject.shoppingcart.entities.Product;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityTestObjectGenerator {
    public static CartProduct buildCartProduct(Long id, Product product, int quantity){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(id);
        cartProduct.setProduct(product);
        cartProduct.setPrice(product.getPrice());
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }

    public static Product buildProduct(long id, String name, String sku, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setSku(sku);
        return product;
    }

    public static Cart buildCart(){
        Product product1 = buildProduct1();
        Product product2 = buildProduct2();
        Product product3 = buildProduct3();

        // Create some cart products
        CartProduct cartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1, 2);
        CartProduct cartProduct2 = EntityTestObjectGenerator.buildCartProduct(2L, product2, 1);
        CartProduct cartProduct3 = EntityTestObjectGenerator.buildCartProduct(3L, product3, 3);

        // Create a cart and add cart products
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUuid("cart-uuid");
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct1);
        cartProducts.add(cartProduct2);
        cartProducts.add(cartProduct3);
        cart.setCartProducts(cartProducts);
        return cart;
    }

    public static @NotNull Product buildProduct3() {
        return EntityTestObjectGenerator.buildProduct(3L, "Product 3", "sku3",  new BigDecimal("20.00"));
    }

    public static @NotNull Product buildProduct2() {
        return EntityTestObjectGenerator.buildProduct(2L, "Product 2", "sku2",new BigDecimal("5.00"));
    }

    public static @NotNull Product buildProduct1() {
        return EntityTestObjectGenerator.buildProduct(1L, "Product 1", "sku1",new BigDecimal("10.00"));
    }
}
