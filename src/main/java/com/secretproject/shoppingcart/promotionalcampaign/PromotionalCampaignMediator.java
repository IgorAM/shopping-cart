package com.secretproject.shoppingcart.promotionalcampaign;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class PromotionalCampaignMediator {

    private Set<CampaignContract> campaigns;

    public PromotionalCampaignMediator() {
        this.campaigns = new TreeSet<>(Comparator.comparing(CampaignContract::getTotalPrice).reversed());
    }

    public void collectPriceCalculation(CampaignContract campaign){
        campaigns.add(campaign);
    }

    public Optional<CampaignContract> getBestOffer(){
        return campaigns.stream().filter(CampaignContract::isEligible).findFirst();
    }
}
