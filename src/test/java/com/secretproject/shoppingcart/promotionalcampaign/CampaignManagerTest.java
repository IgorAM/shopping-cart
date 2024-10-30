package com.secretproject.shoppingcart.promotionalcampaign;

import com.secretproject.shoppingcart.entities.Cart;
import com.secretproject.shoppingcart.testutils.EntityTestObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CampaignManagerTest {

    @Mock
    private PromotionalCampaignMediator mediator;

    @Mock
    private BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaign;

    @Mock
    private VipUserCampaign vipUserCampaign;

    @InjectMocks
    private CampaignManager campaignManager;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldInvokeVipUserCampaignToApplyOffer(){
        Cart cart = EntityTestObjectGenerator.buildCart();
        doNothing().when(buyTwoGetOneFreeCampaign).calculateFinalPriceForCampaign(cart);
        doNothing().when(vipUserCampaign).calculateFinalPriceForCampaign(cart);
        doNothing().when(vipUserCampaign).applyPriceCalculationForCampaign(cart);
        when(mediator.getBestOffer()).thenReturn(Optional.of(vipUserCampaign));

        campaignManager.applyBestOfferPromotionalCampaign(cart);

        verify(buyTwoGetOneFreeCampaign, times(1)).calculateFinalPriceForCampaign(cart);
        verify(vipUserCampaign, times(1)).calculateFinalPriceForCampaign(cart);
        verify(buyTwoGetOneFreeCampaign, times(0)).applyPriceCalculationForCampaign(cart);
        verify(vipUserCampaign, times(1)).applyPriceCalculationForCampaign(cart);
    }


    @Test
    public void shouldInvokeBuyTwoGetOneFreeCampaignToApplyOffer(){
        Cart cart = EntityTestObjectGenerator.buildCart();
        doNothing().when(buyTwoGetOneFreeCampaign).calculateFinalPriceForCampaign(cart);
        doNothing().when(vipUserCampaign).calculateFinalPriceForCampaign(cart);
        doNothing().when(buyTwoGetOneFreeCampaign).applyPriceCalculationForCampaign(cart);
        when(mediator.getBestOffer()).thenReturn(Optional.of(buyTwoGetOneFreeCampaign));

        campaignManager.applyBestOfferPromotionalCampaign(cart);

        verify(buyTwoGetOneFreeCampaign, times(1)).calculateFinalPriceForCampaign(cart);
        verify(vipUserCampaign, times(1)).calculateFinalPriceForCampaign(cart);
        verify(buyTwoGetOneFreeCampaign, times(1)).applyPriceCalculationForCampaign(cart);
        verify(vipUserCampaign, times(0)).applyPriceCalculationForCampaign(cart);
    }
}
