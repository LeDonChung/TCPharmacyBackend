package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;

import java.util.List;

public interface RecommendService {
    List<MedicineResponse> getRecommendations(List<Long> productIds);

}
