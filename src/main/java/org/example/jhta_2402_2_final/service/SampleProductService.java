package org.example.jhta_2402_2_final.service;


import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.SampleProductDao;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleProductService {
    private final SampleProductDao sampleProductDao;

    public List<ProductDto> selectAll(){
        return sampleProductDao.findAll();
    }

    public int insertNewProduct(ProductDto productDto){
        ProductDto productDto1 = ProductDto.builder()
                .productName(productDto.getProductName())
                .build();
        return sampleProductDao.insertNewProduct(productDto1);


    }
}
