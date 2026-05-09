package com.example.ShopSpring.features.product.service;

import com.example.ShopSpring.features.product.dto.ProductListResponse;
import com.example.ShopSpring.features.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;



@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    public String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest){
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id"))
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";

        return String.format("products:%s:%s:%s:%s:%s"
                , keyword, categoryId, pageNumber, pageSize, sortDirection);
    }

    @Override
    public ProductListResponse getAllProducts(
            String keyword, Long categoryId, PageRequest pageRequest
    ) throws JacksonException {
        String key = getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String)redisTemplate.opsForValue().get(key);

        return json != null ? redisObjectMapper.readValue(
                json,
                new TypeReference<ProductListResponse>() {}
        ):null;
    }



    @Override
    public void saveAllProducts(
            ProductListResponse productListResponse,
            String keyword, Long categoryId,
            PageRequest pageRequest) {
        String key = getKeyFrom(keyword,categoryId,pageRequest);
        String json = redisObjectMapper.writeValueAsString(productListResponse);
        redisTemplate.opsForValue().set(key, json );
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
