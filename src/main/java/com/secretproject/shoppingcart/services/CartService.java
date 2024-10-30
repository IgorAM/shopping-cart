package com.secretproject.shoppingcart.services;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import com.secretproject.shoppingcart.entities.Product;
import com.secretproject.shoppingcart.promotionalcampaign.CampaignManager;
import com.secretproject.shoppingcart.promotionalcampaign.CampaignManagerFactory;
import com.secretproject.shoppingcart.repositories.CartRepository;
import com.secretproject.shoppingcart.repositories.ProductRepository;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CampaignManagerFactory managerFactory;


    public CartService(CartRepository cartRepository, ProductRepository productRepository, CampaignManagerFactory managerFactory) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.managerFactory = managerFactory;
    }

    public Optional<Cart> loadCart(String uuid){
        if(ObjectUtils.isEmpty(uuid)){
            throw new IllegalArgumentException("uuid must not be empty");
        }
        return cartRepository.findByUuid(uuid);
    }

    public Cart addToCart(Cart cartRequest){

        logger.info("fetching cart by uuid {} for payload {}", cartRequest.getUuid(), cartRequest);

        Cart cart = cartRepository.findByUuid(cartRequest.getUuid())
                        .orElse(new Cart(UUID.randomUUID().toString()));

        logger.debug("cart fetched by uuid {}: {}", cart.getUuid(), cart);
        List<CartProduct> requestCartProducts = getCartProductListWithEntityProduct(cartRequest);

        logger.debug("cart products fetched for uuid {}: {}", cart.getUuid(), requestCartProducts);

        updateCart(requestCartProducts, cart);

        resetCartProductPrice(cart);

        CampaignManager campaignManager = managerFactory.createInstance();
        campaignManager.applyBestOfferPromotionalCampaign(cart);
        logger.debug("cart products updated after applying campaign for uuid {}: {}", cart.getUuid(), cart);

        cartRepository.save(cart);
        logger.debug("cart products saved for uuid {}: {}", cart.getUuid(), cart);

        return cart;

    }

    private void resetCartProductPrice(Cart cart) {
        cart.getCartProducts().forEach(cartProduct -> {
            cartProduct.setPrice(cartProduct.getProduct().getPrice());
        });
    }

    private void updateCart(List<CartProduct> requestCartProducts, Cart cart) {
        updateItemsInCart(requestCartProducts, cart);
        logger.info("cart updated for uuid {}: {}", cart.getUuid(), cart);

        //removeItemsExcludedFromCart(requestCartProducts, cart);
        //logger.debug("cart updated after removing items for uuid {}: {}", cart.getUuid(), cart);

        addNewItemsToCart(requestCartProducts, cart);
        logger.info("cart updated after adding items for uuid {}: {}", cart.getUuid(), cart);
    }

    private void updateItemsInCart(List<CartProduct> requestCartProducts, Cart cart) {
        for(CartProduct requestCartProduct: requestCartProducts){
            for(CartProduct destinationCartProduct : cart.getCartProducts()){
                logger.info("trying to update product {} in cart: {}", requestCartProduct, destinationCartProduct);
                if(requestCartProduct.getProduct().getSku().equals(destinationCartProduct.getProduct().getSku())){
                    destinationCartProduct.setPrice(requestCartProduct.getProduct().getPrice());
                    destinationCartProduct.setQuantity(destinationCartProduct.getQuantity() + requestCartProduct.getQuantity());
                    logger.info("updated product {} in cart: {}", requestCartProduct.getProduct().getSku(), destinationCartProduct);
                }
            }
        }
    }

    private void addNewItemsToCart(List<CartProduct> requestCartProducts, Cart cart) {
        List<CartProduct> toAddCartProducts = ListUtils.subtract(requestCartProducts, cart.getCartProducts());
        logger.info("adding products {} in cart: {}", toAddCartProducts, cart);
        cart.addAllCartProducts(toAddCartProducts);
    }

    private void removeItemsExcludedFromCart(List<CartProduct> requestCartProducts, Cart cart) {
        List<CartProduct> toRemoveCartProducts = ListUtils.subtract(cart.getCartProducts(), requestCartProducts);
        cart.getCartProducts().removeAll(toRemoveCartProducts);
    }

    private List<CartProduct> getCartProductListWithEntityProduct(Cart cartRequest) {

        logger.info("cart product list before grouping by product sku {}", cartRequest.getCartProducts());

        return cartRequest.getCartProducts().stream()
                .map(cartProduct -> {
                    String sku = cartProduct.getProduct().getSku();
                    Optional<Product> product = productRepository.findBySku(sku);
                    if(product.isEmpty()){
                        logger.debug("product not found for sku {}: {}", sku);
                        throw new IllegalArgumentException("Product not found for sku " + sku);
                    }else{
                        logger.debug("product fetched by sku {}: {}", sku, product.get());
                        cartProduct.setProduct(product.get());
                        cartProduct.setPrice(product.get().getPrice());
                    }

                    return cartProduct;
                }).collect(Collectors.toList());
    }
}
