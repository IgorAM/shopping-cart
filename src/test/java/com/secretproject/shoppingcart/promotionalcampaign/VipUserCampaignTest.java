package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.testutils.EntityTestObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VipUserCampaignTest {

    public static final int PERCENTAGE = 15;
    @Mock
    private PromotionalCampaignMediator mediator;

    @InjectMocks
    private VipUserCampaign vipUserCampaign;

    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vipUserCampaign = new VipUserCampaign(mediator, PERCENTAGE);
        cart = EntityTestObjectGenerator.buildCart();
    }

    @Test
    void shouldApplyPromotionForLowestPriceProductInCart() {
        vipUserCampaign.calculateFinalPriceForCampaign(cart);

        assertThat(vipUserCampaign.getTotalPrice()).isEqualByComparingTo(new BigDecimal("72.25"));

        vipUserCampaign.applyPriceCalculationForCampaign(cart);

        cart.getCartProducts().forEach(cartProduct -> {
            BigDecimal expectedPrice = cartProduct.getProduct().getPrice().multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(PERCENTAGE).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            assertThat(cartProduct.getPrice()).isEqualByComparingTo(expectedPrice);
        });

    }

}
