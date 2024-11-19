package vn.edu.iuh.fit.pharmacy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.service.MedicineService;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;

import java.util.List;

@RestController
@RequestMapping("/medicines")
public class MedicineResource {
    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long medicineId) throws MedicineException {
        return ResponseEntity.ok(
                medicineService.getMedicineById(medicineId)
        );
    }

    @GetMapping("/getByTagId/{tagId}")
    public ResponseEntity<List<MedicineResponse>> getMedicineByTagId(@PathVariable Long tagId) throws MedicineException {
        return ResponseEntity.ok(
                medicineService.getMedicineByTagId(tagId)
        );
    }

    @PostMapping("/getByTags")
    public ResponseEntity<List<MedicineResponse>> getMedicineByTags(@RequestBody List<Long> tagIds) throws MedicineException {
        return ResponseEntity.ok(
                medicineService.getMedicineByTags(tagIds)
        );
    }
}
