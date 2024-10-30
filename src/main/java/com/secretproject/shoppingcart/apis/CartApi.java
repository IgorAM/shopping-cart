package com.secretproject.shoppingcart.apis;

import com.secretproject.shoppingcart.apis.requests.CartRequest;
import com.secretproject.shoppingcart.apis.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RequestMapping("v1/carts")
public interface CartApi {

    @GetMapping("/{uuid}" )
    ResponseEntity<ApiResponse> getCart(@PathVariable("uuid") String uuid);

    @PutMapping
    ResponseEntity<ApiResponse> addProductToCart(@RequestBody CartRequest cartRequest);


}
