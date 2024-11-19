package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.POJOs.Brand;
import vn.edu.iuh.fit.pharmacy.utils.response.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getFavorite();
}
