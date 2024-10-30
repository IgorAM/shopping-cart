package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;

import java.math.BigDecimal;

public interface CampaignContract {

    Boolean isEligible();
    BigDecimal getTotalPrice();
    void calculateFinalPriceForCampaign(Cart cart);

    void applyPriceCalculationForCampaign(Cart cart);
}
