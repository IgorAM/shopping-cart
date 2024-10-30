package com.secretproject.shoppingcart.controllers;

import com.secretproject.shoppingcart.apis.CartApi;
import com.secretproject.shoppingcart.adapters.CartServiceAdapter;
import com.secretproject.shoppingcart.apis.requests.CartRequest;
import com.secretproject.shoppingcart.apis.responses.ApiError;
import com.secretproject.shoppingcart.apis.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CartController implements CartApi {

    Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartServiceAdapter cartService;

    public CartController(CartServiceAdapter cartService) {
        this.cartService = cartService;
    }

    @Override
    public ResponseEntity<ApiResponse> getCart(String uuid) {
        logger.info("fetching cart for uuid {}", uuid);
        if(ObjectUtils.isEmpty(uuid)){
            return new ResponseEntity<>(new ApiError("cart not found"), HttpStatus.NOT_FOUND);
        }

        try {
            return ResponseEntity.ok(cartService.loadCart(uuid));
        }catch(IllegalArgumentException e){
            logger.error("An IllegalArgumentException occurred: {}", e.getStackTrace());
            ApiError apiError = new ApiError(e.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error("An Exception occurred: {}", e.getStackTrace());
            ApiError apiError = new ApiError(e.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addProductToCart(CartRequest cartRequest) {
        try {
            String returnedUuid = cartService.addToCart(cartRequest);
            return ResponseEntity.accepted().location(URI.create("/api/v1/carts/"+returnedUuid)).build();
        }catch(IllegalArgumentException e){
            logger.error("An IllegalArgumentException occurred: {}", e.getStackTrace());
            ApiError apiError = new ApiError(e.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error("An Exception occurred: {}", e.getStackTrace());
            ApiError apiError = new ApiError(e.getMessage());
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
