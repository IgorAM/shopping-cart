package com.secretproject.shoppingcart.promotionalcampaign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PromotionalCampaignMediatorTest {

    @Mock
    private Set<CampaignContract> campaigns;

    @Mock
    private BuyTwoGetOneFreeCampaign buyTwoGetOneFreeCampaign;

    @InjectMocks
    private PromotionalCampaignMediator mediator;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        doReturn(BigDecimal.TEN).when(buyTwoGetOneFreeCampaign).getTotalPrice();

    }

    @Test
    void shouldCollectPriceCalculation(){
        doReturn(true).when(buyTwoGetOneFreeCampaign).isEligible();

        mediator.collectPriceCalculation(buyTwoGetOneFreeCampaign);
        verify(campaigns, times(1)).add(buyTwoGetOneFreeCampaign);

        when(campaigns.stream()).then(i -> Stream.of(buyTwoGetOneFreeCampaign));
        Optional<CampaignContract> bestOffer = mediator.getBestOffer();
        assertThat(bestOffer.get()).isNotNull();
        assertThat(bestOffer.get()).isSameAs(buyTwoGetOneFreeCampaign);
    }

    @Test
    void shouldReturnOptionalCampaignIfNoEligibleCampaign(){
        doReturn(false).when(buyTwoGetOneFreeCampaign).isEligible();

        mediator.collectPriceCalculation(buyTwoGetOneFreeCampaign);
        verify(campaigns, times(1)).add(buyTwoGetOneFreeCampaign);

        when(campaigns.stream()).then(i -> Stream.of(buyTwoGetOneFreeCampaign));
        Optional<CampaignContract> bestOffer = mediator.getBestOffer();
        assertTrue(bestOffer.isEmpty());
    }

}
