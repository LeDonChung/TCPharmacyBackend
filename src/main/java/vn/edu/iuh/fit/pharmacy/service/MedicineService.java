package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;

import java.util.List;

public interface MedicineService {
    MedicineResponse getMedicineById(Long medicineId) throws MedicineException;

    List<MedicineResponse> getMedicineByTagId(Long tagId);

    List<MedicineResponse> getMedicineByTags(List<Long> tagIds);

    List<MedicineResponse> getRecommendations(List<Long> medicineIds);

    List<MedicineResponse> getRecommendations2(List<Long> medicineIds) throws Exception;
}
