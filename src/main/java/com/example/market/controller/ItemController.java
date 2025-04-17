package com.example.market.controller;

import com.example.market.dto.requests.CreateItemRequest;
import com.example.market.dto.requests.UpdateItemRequest;
import com.example.market.dto.responses.GetAllItemsResponse;
import com.example.market.dto.responses.GetByIdItemResponse;
import com.example.market.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true" )
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping()
    public List<GetAllItemsResponse> getAll(){
        return itemService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdItemResponse getById(@PathVariable int id){
        return itemService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CreateItemRequest createFruitRequest) {
        this.itemService.add(createFruitRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateItemRequest updateItemRequest){
        this.itemService.update(updateItemRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        this.itemService.delete(id);
    }


}
