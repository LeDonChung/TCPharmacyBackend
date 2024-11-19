package vn.edu.iuh.fit.pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.Brand;
import vn.edu.iuh.fit.pharmacy.mappers.BrandMapper;
import vn.edu.iuh.fit.pharmacy.repositories.BrandRepository;
import vn.edu.iuh.fit.pharmacy.service.BrandService;
import vn.edu.iuh.fit.pharmacy.utils.response.BrandResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public List<BrandResponse> getFavorite() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(
                brand -> {
                    BrandResponse brandResponse = brandMapper.toDto(brand);
                    brandResponse.setImageProduct(brand.getProducts().get(0).getPrimaryImage());
                    return brandResponse;
                }
        ).collect(Collectors.toList());
    }
}
