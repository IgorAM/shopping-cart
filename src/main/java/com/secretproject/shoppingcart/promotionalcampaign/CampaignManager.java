package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;

import java.util.List;
import java.util.Optional;


public class CampaignManager {

    PromotionalCampaignMediator mediator;
    private BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaignFactory;
    private VipUserCampaign vipUserCampaignFactory;

    public CampaignManager(PromotionalCampaignMediator mediator,
                           BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaignFactory,
                           VipUserCampaign vipUserCampaignFactory) {
        this.mediator = mediator;
        this.buyTwoGetOneFreeCampaignFactory = buyTwoGetOneFreeCampaignFactory;
        this.vipUserCampaignFactory = vipUserCampaignFactory;
    }

    public Cart applyBestOfferPromotionalCampaign(Cart cart){

        List<CampaignContract> campaigns = List.of( buyTwoGetOneFreeCampaignFactory,
                                                    vipUserCampaignFactory);
        campaigns.forEach(campaign -> campaign.calculateFinalPriceForCampaign(cart));

        Optional<CampaignContract> bestOffer = mediator.getBestOffer();
        bestOffer.ifPresent(offer -> offer.applyPriceCalculationForCampaign(cart));
        return cart;
    }
}
