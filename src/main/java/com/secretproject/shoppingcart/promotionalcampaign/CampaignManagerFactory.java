package com.secretproject.shoppingcart.promotionalcampaign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CampaignManagerFactory {

    @Value("${vipCampaign.discount.percentage}")
    private String discountPercentage;

    public CampaignManager createInstance(){

        PromotionalCampaignMediator mediator = new PromotionalCampaignMediator();
        BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaign = new BuyTwoGetOneFreeCampaign(mediator);
        VipUserCampaign vipUserCampaign = new VipUserCampaign(mediator, Integer.valueOf(discountPercentage));
        return new CampaignManager(mediator, buyTwoGetOneFreeCampaign, vipUserCampaign);

    }
}
