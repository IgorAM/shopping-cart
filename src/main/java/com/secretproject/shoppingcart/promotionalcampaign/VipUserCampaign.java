package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class VipUserCampaign implements CampaignContract{

    Logger logger = LoggerFactory.getLogger(VipUserCampaign.class);

    private PromotionalCampaignMediator mediator;
    private BigDecimal price;
    private int discountPercentage;
    private Boolean eligible;


    //for testing only
    private VipUserCampaign() {}

    public VipUserCampaign(PromotionalCampaignMediator mediator,
                           int discountPercentage) {
        this.mediator = mediator;
        this.discountPercentage = discountPercentage;
    }


    @Override
    public BigDecimal getTotalPrice() {
        return price;
    }

    @Override
    public Boolean isEligible() {
        return eligible;
    }

    @Override
    public void calculateFinalPriceForCampaign(Cart cart) {

        if (cart != null && isVipUser()) {
            eligible = true;
        } else {
            eligible = false;
        }

        List<CartProduct> cartProducts = cart.getCartProducts();

        BigDecimal total = cartProducts.stream()
                .map(cartProduct -> {
                    BigDecimal totalPrice = cartProduct.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity()));
                    return calculatePercentageDiscount(totalPrice);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Vip user campaign had total price with discount {}", total);
        this.price = total;
        mediator.collectPriceCalculation(this);
    }

    private BigDecimal calculatePercentageDiscount(BigDecimal totalPrice) {
        return totalPrice.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
    }

    //TODO: extract it from Spring Security Context
    private boolean isVipUser() {
        return false;
    }

    @Override
    public void applyPriceCalculationForCampaign(Cart cart){

        if (cart == null || cart.getCartProducts().isEmpty()) {
            throw new IllegalStateException("There should have products in the cart.");
        }

        cart.getCartProducts().forEach(cartProduct ->{
            cartProduct.setPrice(calculatePercentageDiscount(cartProduct.getPrice()));
        });
    }
}
