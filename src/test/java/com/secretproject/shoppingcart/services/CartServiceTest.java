package com.secretproject.shoppingcart.services;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import com.secretproject.shoppingcart.entities.Product;
import com.secretproject.shoppingcart.promotionalcampaign.CampaignManager;
import com.secretproject.shoppingcart.promotionalcampaign.CampaignManagerFactory;
import com.secretproject.shoppingcart.repositories.CartRepository;
import com.secretproject.shoppingcart.repositories.ProductRepository;
import com.secretproject.shoppingcart.testutils.EntityTestObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    static Optional<Product> product1 = Optional.of(EntityTestObjectGenerator.buildProduct1());
    static Optional<Product> product2 = Optional.of(EntityTestObjectGenerator.buildProduct2());
    static Optional<Product> product3 = Optional.of(EntityTestObjectGenerator.buildProduct3());
    static Optional<Product> product4 = Optional.of(EntityTestObjectGenerator.buildProduct(4L, "Product 4", "sku4",  new BigDecimal("30.00")));
    static Cart cart;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CampaignManagerFactory managerFactory;

    @Mock
    CampaignManager campaignManager;

    @InjectMocks
    CartService cartService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        cart = EntityTestObjectGenerator.buildCart();
    }

    @Test
    void shouldCreateNewCart(){
        Optional<Cart> optionalCart = Optional.of(cart);
        doReturn(optionalCart).when(cartRepository).findByUuid(cart.getUuid());
        doReturn(product1).when(productRepository).findBySku(product1.get().getSku());
        doReturn(product2).when(productRepository).findBySku(product2.get().getSku());
        doReturn(product3).when(productRepository).findBySku(product3.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(cart).when(campaignManager).applyBestOfferPromotionalCampaign(cart);

        cartService.addToCart(cart);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());

        Cart capturedCart = cartCaptor.getValue();

        assertNotNull(capturedCart);
    }

    @Test
    void shouldUpdateExistingCart(){
        CartProduct cartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1.get(), 2);
        Cart requestCart = buildRequestCart(cartProduct1);
        Optional<Cart> optionalCart = Optional.of(buildEmptyRequestCart());

        doReturn(optionalCart).when(cartRepository).findByUuid(requestCart.getUuid());
        doReturn(product1).when(productRepository).findBySku(product1.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(requestCart).when(campaignManager).applyBestOfferPromotionalCampaign(requestCart);

        cartService.addToCart(requestCart);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());

        Cart capturedCart = cartCaptor.getValue();

        assertEquals(requestCart.getUuid(), capturedCart.getUuid());
        assertEquals(requestCart.getCartProducts().size(), capturedCart.getCartProducts().size());
        capturedCart.getCartProducts().forEach(cartProduct -> {
            if(Long.valueOf(1L).equals(cartProduct.getId())){
                assertEquals(1L, cartProduct.getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(2, cartProduct.getQuantity());

                assertEquals(product1.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product1.get().getSku(), cartProduct.getProduct().getSku());
            }else if(Long.valueOf(2L).equals(cartProduct.getId())){
                assertEquals(2L, cartProduct.getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(1, cartProduct.getQuantity());

                assertEquals(product2.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product2.get().getSku(), cartProduct.getProduct().getSku());
            }else if(Long.valueOf(3L).equals(cartProduct.getId())){
                assertEquals(3L, cartProduct.getId());
                assertEquals(product4.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(4, cartProduct.getQuantity());

                assertEquals(product4.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product4.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product4.get().getSku(), cartProduct.getProduct().getSku());
            }

        });
    }

    @Test
    void shouldUpdateExistingCartWithMultipleProducts(){
        validateFirstProductAddedIntoCart();
        validateSecondProductAddedIntoCart();
        validateFirstProductAddedIntoCartSecondTime();
    }

    private void validateFirstProductAddedIntoCart() {
        CartProduct cartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1.get(), 2);
        Cart requestCart1 = buildRequestCart(cartProduct1);
        Optional<Cart> optionalCart1 = Optional.of(buildEmptyRequestCart());

        doReturn(optionalCart1).when(cartRepository).findByUuid(requestCart1.getUuid());
        doReturn(product1).when(productRepository).findBySku(product1.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(requestCart1).when(campaignManager).applyBestOfferPromotionalCampaign(requestCart1);

        cartService.addToCart(requestCart1);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(cartCaptor.capture());

        Cart capturedCart = cartCaptor.getValue();

        assertEquals(requestCart1.getUuid(), capturedCart.getUuid());
        assertEquals(requestCart1.getCartProducts().size(), capturedCart.getCartProducts().size());
        capturedCart.getCartProducts().forEach(cartProduct -> {
            if(Long.valueOf(1L).equals(cartProduct.getId())){
                assertEquals(1L, cartProduct.getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(2, cartProduct.getQuantity());

                assertEquals(product1.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product1.get().getSku(), cartProduct.getProduct().getSku());
            }

        });
    }

    private void validateSecondProductAddedIntoCart() {
        CartProduct cartProduct2 = EntityTestObjectGenerator.buildCartProduct(2L, product2.get(), 2);
        Cart requestCart2 = buildRequestCart(List.of(cartProduct2));

        CartProduct responseCartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1.get(), 2);
        Cart responseCart2 = buildRequestCart(List.of(responseCartProduct1));
        Optional<Cart> optionalCart2 = Optional.of(responseCart2);

        doReturn(optionalCart2).when(cartRepository).findByUuid(requestCart2.getUuid());
        doReturn(product2).when(productRepository).findBySku(product2.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(requestCart2).when(campaignManager).applyBestOfferPromotionalCampaign(requestCart2);

        cartService.addToCart(requestCart2);

        ArgumentCaptor<Cart> cartCaptor2 = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(2)).save(cartCaptor2.capture());

        Cart capturedCart2 = cartCaptor2.getValue();

        assertEquals(requestCart2.getUuid(), capturedCart2.getUuid());
        assertEquals(2, capturedCart2.getCartProducts().size());
        capturedCart2.getCartProducts().forEach(cartProduct -> {
            if(Long.valueOf(1L).equals(cartProduct.getId())){
                assertEquals(1L, cartProduct.getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(2, cartProduct.getQuantity());

                assertEquals(product1.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product1.get().getSku(), cartProduct.getProduct().getSku());
            }else if(Long.valueOf(2L).equals(cartProduct.getId())){
                assertEquals(2L, cartProduct.getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(2, cartProduct.getQuantity());

                assertEquals(product2.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product2.get().getSku(), cartProduct.getProduct().getSku());
            }

        });
    }

    private void validateFirstProductAddedIntoCartSecondTime() {
        CartProduct cartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1.get(), 1);
        Cart requestCart1 = buildRequestCart(List.of(cartProduct1));

        CartProduct responseCartProduct1 = EntityTestObjectGenerator.buildCartProduct(1L, product1.get(), 2);
        CartProduct responseCartProduct2 = EntityTestObjectGenerator.buildCartProduct(2L, product2.get(), 2);
        Cart responseCart = buildRequestCart(List.of(responseCartProduct1, responseCartProduct2));
        Optional<Cart> optionalCart = Optional.of(responseCart);

        doReturn(optionalCart).when(cartRepository).findByUuid(requestCart1.getUuid());
        doReturn(product2).when(productRepository).findBySku(product2.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(requestCart1).when(campaignManager).applyBestOfferPromotionalCampaign(requestCart1);

        cartService.addToCart(requestCart1);

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(3)).save(cartCaptor.capture());

        Cart capturedCart = cartCaptor.getValue();

        assertEquals(requestCart1.getUuid(), capturedCart.getUuid());
        assertEquals(2, capturedCart.getCartProducts().size());
        capturedCart.getCartProducts().forEach(cartProduct -> {
            if(Long.valueOf(1L).equals(cartProduct.getId())){
                assertEquals(1L, cartProduct.getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(3, cartProduct.getQuantity());

                assertEquals(product1.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product1.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product1.get().getSku(), cartProduct.getProduct().getSku());
            }else if(Long.valueOf(2L).equals(cartProduct.getId())){
                assertEquals(2L, cartProduct.getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(2, cartProduct.getQuantity());

                assertEquals(product2.get().getId(), cartProduct.getProduct().getId());
                assertEquals(product2.get().getPrice(), cartProduct.getProduct().getPrice());
                assertEquals(product2.get().getSku(), cartProduct.getProduct().getSku());
            }

        });
    }

    public static Cart buildRequestCart(List<CartProduct> cartProducts){
        Cart requestCart = new Cart();
        requestCart.setUuid(cart.getUuid());
        requestCart.addAllCartProducts(cartProducts);
        return requestCart;
    }

    public static Cart buildRequestCart(CartProduct cartProduct){
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        return buildRequestCart(cartProducts);
    }

    public static Cart buildEmptyRequestCart(){

        Cart requestCart = new Cart();
        requestCart.setUuid(cart.getUuid());
        return requestCart;
    }

    @Test
    void shouldExpectExceptionThrownIfProductNotFound(){

        doReturn(Optional.empty()).when(cartRepository).findByUuid(cart.getUuid());
        doReturn(Optional.empty()).when(productRepository).findBySku(product1.get().getSku());
        doReturn(campaignManager).when(managerFactory).createInstance();
        doReturn(cart).when(campaignManager).applyBestOfferPromotionalCampaign(cart);

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(cart);
        });
    }

}
