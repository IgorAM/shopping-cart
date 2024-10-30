package controllers;

import com.secretproject.shoppingcart.ShoppingCartApplication;
import com.secretproject.shoppingcart.apis.requests.CartRequest;
import com.secretproject.shoppingcart.apis.responses.CartDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
@TestPropertySource(locations = "classpath:application-integration.properties")
public class CartControllerIT implements AbstractIntegrationTest{

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void shouldGetNotFoundResultWhenCallingGetCartAPI(){
        String uuid = "123";
        ResponseEntity<CartDTO> response = testRestTemplate.
                getForEntity("/v1/carts/" + uuid, CartDTO.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldAddProductInCart(){
        CartRequest cartRequest = new CartRequest();
        cartRequest.setSku("PR001");
        cartRequest.setQuantity(2);

        HttpEntity httpEntity = new HttpEntity(cartRequest);
        ResponseEntity<Object> exchange = testRestTemplate.exchange("/v1/carts" , HttpMethod.PUT, httpEntity, Object.class);
        String location = exchange.getHeaders().get("Location").get(0);
        int lastIndex = location.lastIndexOf("/");
        String returnedUuid = location.substring(lastIndex + 1);

        ResponseEntity<CartDTO> response = testRestTemplate.
                getForEntity("/v1/carts/" + returnedUuid, CartDTO.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        assertEquals(returnedUuid, response.getBody().getUuid());

        response.getBody().getCartProducts().forEach(cartProductDTO -> {
            assertEquals(cartRequest.getSku(), cartProductDTO.getSku());
            assertEquals("Smartphone 1", cartProductDTO.getName());
            assertEquals(cartRequest.getQuantity(), cartProductDTO.getQuantity());
            assertEquals(new BigDecimal(3000.00).doubleValue(), cartProductDTO.getPrice().doubleValue());
            assertEquals(new BigDecimal(6000.00).doubleValue(), cartProductDTO.getTotalAmount().doubleValue());
        });


    }
}
