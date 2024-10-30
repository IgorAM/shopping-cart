package com.secretproject.shoppingcart.adapters;

import com.secretproject.shoppingcart.apis.requests.CartRequest;
import com.secretproject.shoppingcart.apis.responses.CartProductDTO;
import com.secretproject.shoppingcart.apis.responses.CartDTO;
import com.secretproject.shoppingcart.controllers.CartController;
import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import com.secretproject.shoppingcart.entities.Product;
import com.secretproject.shoppingcart.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Component
public class CartServiceAdapter {

    Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartServiceAdapter(CartService cartService) {
        this.cartService = cartService;
    }

    public CartDTO loadCart(String uuid){
        if(ObjectUtils.isEmpty(uuid)){
            throw new IllegalArgumentException("uuid must not be empty");
        }

        logger.info("loading cart {}", uuid);
        Optional<Cart> cartEntity = cartService.loadCart(uuid);

        if(cartEntity.isEmpty() ||
                CollectionUtils.isEmpty(cartEntity.get().getCartProducts())){
            logger.info("cart {} doesn't exist", uuid);
            throw new IllegalArgumentException("cart not found");
        }

        logger.info("cart {} loaded: {}", uuid, cartEntity.get());
        CartDTO cartDTO = cartEntity.get()
                .getCartProducts()
                .stream()
                .map(this::toCartProductDto)
                .reduce(new CartDTO(), (cartResponse, cartProductDTO) -> {
                    cartResponse.addCartProduct(cartProductDTO);
                    cartResponse.addTotalAmount(cartProductDTO.getTotalAmount());
                    return cartResponse;
                }, (response1, response2) -> {
                    response1.getCartProducts().addAll(response2.getCartProducts());
                    response1.addTotalAmount(response2.getTotalAmount());
                    return response1;
                });
        cartDTO.setUuid(cartEntity.get().getUuid());

        return cartDTO;
    }

    public String addToCart(CartRequest cartRequest){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(cartRequest.getQuantity());

        Product product = new Product();
        product.setSku(cartRequest.getSku());
        cartProduct.setProduct(product);

        Cart cart = new Cart();
        cart.setUuid(cartRequest.getUuid());
        cart.addAllCartProducts(List.of(cartProduct));

        return cartService.addToCart(cart).getUuid();
    }

    private CartProductDTO toCartProductDto(CartProduct cartProductEntity){
        CartProductDTO cartProductDTO = new CartProductDTO();
        cartProductDTO.setName(cartProductEntity.getProduct().getName());
        cartProductDTO.setPrice(cartProductEntity.getProduct().getPrice());

        cartProductDTO.setQuantity(cartProductEntity.getQuantity());
        cartProductDTO.setTotalAmount(cartProductEntity.calculateTotalAmount());
        cartProductDTO.setSku(cartProductEntity.getProduct().getSku());
        return cartProductDTO;
    }
}
