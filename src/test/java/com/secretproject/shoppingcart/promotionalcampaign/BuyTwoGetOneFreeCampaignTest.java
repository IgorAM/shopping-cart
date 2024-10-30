package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import com.secretproject.shoppingcart.testutils.EntityTestObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

public class BuyTwoGetOneFreeCampaignTest {

    @Mock
    private PromotionalCampaignMediator mediator;

    @InjectMocks
    private BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaign;

    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyTwoGetOneFreeCampaign = new BuyTwoGetOneFreeCampaign(mediator);
        cart = EntityTestObjectGenerator.buildCart();
    }

    @Test
    void shouldApplyPromotionForLowestPriceProductInCart() {

        buyTwoGetOneFreeCampaign.calculateFinalPriceForCampaign(cart);
        assertThat(buyTwoGetOneFreeCampaign.getTotalPrice()).isEqualByComparingTo(new BigDecimal("80.00"));

        buyTwoGetOneFreeCampaign.applyPriceCalculationForCampaign(cart);

        CartProduct lowestPriceCartProduct = cart.getCartProducts().stream()
                .min(Comparator.comparing(CartProduct::getPrice)).get();
        assertThat(lowestPriceCartProduct.getPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldNotAllowPromotionForEmptyProductsInCart() {
        Cart emptyCart = new Cart();

        assertThrows(IllegalStateException.class, () -> {
            buyTwoGetOneFreeCampaign.calculateFinalPriceForCampaign(emptyCart);
        });
    }

}
