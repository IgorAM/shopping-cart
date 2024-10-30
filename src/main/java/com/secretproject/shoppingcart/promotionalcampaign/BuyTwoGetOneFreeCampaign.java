package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.entities.CartProduct;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


public class BuyTwoGetOneFreeCampaign implements CampaignContract{

    private final PromotionalCampaignMediator mediator;
    private BigDecimal price;
    private Boolean eligible;

    public BuyTwoGetOneFreeCampaign(PromotionalCampaignMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public Boolean isEligible() {
        return eligible;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return price;
    }

    @Override
    public void calculateFinalPriceForCampaign(Cart cart) {
        List<CartProduct> cartProducts = cart.getCartProducts();

        if(cartProducts != null && cartProducts.size() < 3 ){
            eligible = false;
        }else{
            eligible = true;
        }

        CartProduct lowestPriceProduct = getLowerPriceCartProduct(cartProducts);

        BigDecimal total = cartProducts.stream()
                .filter(cartProduct -> !cartProduct.equals(lowestPriceProduct))
                .map(cartProduct -> cartProduct.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.price = total;
        mediator.collectPriceCalculation(this);
    }

    @Override
    public void applyPriceCalculationForCampaign(Cart cart){
        List<CartProduct> cartProducts = cart.getCartProducts();

        CartProduct lowerPriceCartProduct = getLowerPriceCartProduct(cartProducts);

        lowerPriceCartProduct.setPrice(new BigDecimal(0));

    }

    private CartProduct getLowerPriceCartProduct(List<CartProduct> cartProducts) {
        if (cartProducts.isEmpty()) {
            throw new IllegalStateException("There should have products in the cart.");
        }

        return cartProducts.stream()
                .min(Comparator.comparing(CartProduct::getPrice)).get();
    }

}
