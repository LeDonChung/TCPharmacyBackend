package vn.edu.iuh.fit.pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;
import vn.edu.iuh.fit.pharmacy.POJOs.TagGroup;
import vn.edu.iuh.fit.pharmacy.exceptions.ResourceNotFoundException;
import vn.edu.iuh.fit.pharmacy.mappers.MedicineMapper;
import vn.edu.iuh.fit.pharmacy.mappers.TagGroupMapper;
import vn.edu.iuh.fit.pharmacy.mappers.TagMapper;
import vn.edu.iuh.fit.pharmacy.repositories.MedicineRepository;
import vn.edu.iuh.fit.pharmacy.repositories.TagGroupRepository;
import vn.edu.iuh.fit.pharmacy.repositories.TagRepository;
import vn.edu.iuh.fit.pharmacy.service.TagGroupService;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;
import vn.edu.iuh.fit.pharmacy.utils.response.TagGroupResponse;
import vn.edu.iuh.fit.pharmacy.utils.response.TagResponse;

import java.util.Collection;
import java.util.List;

@Service
public class TagGroupServiceImpl implements TagGroupService {
    @Autowired
    private TagGroupRepository tagGroupRepository;
    @Autowired
    private TagGroupMapper tagGroupMapper;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MedicineMapper medicineMapper;
    @Override
    public TagGroupResponse getTagGroupById(Long id) {
        TagGroup tagGroup = tagGroupRepository.findById(id).orElse(null);
        if(tagGroup == null) {
            throw new ResourceNotFoundException("Nhóm tag không tồn tại với id: " + id);
        }

        Collection<Tag> tags = tagGroup.getTags();

        Collection<TagResponse> tagResponses = tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
        // Find Product
        for(TagResponse tagResponse : tagResponses) {
            List<Medicine> medicines = medicineRepository.findAllByTagId(tagResponse.getId(), PageRequest.of(0, 10)).getContent();
            List<MedicineResponse> medicineResponses = medicines.stream().map(medicine -> medicineMapper.toDto(medicine)).toList();
            tagResponse.setMedicines(medicineResponses);
        }

        TagGroupResponse tagResponse = tagGroupMapper.toDto(tagGroup);
        tagResponse.setTags(tagResponses);

        return tagResponse;
    }

    @Override
    public List<TagGroupResponse> getAllTagGroup() {
        List<TagGroup> tagGroups = tagGroupRepository.findAll();

        return tagGroups.stream().map(tagGroup -> {

            Collection<Tag> tags = tagGroup.getTags();
            Collection<TagResponse> tagResponses = tags.stream().map(tag -> tagMapper.toDto(tag)).toList();
            // Find Product
            for(TagResponse tagResponse : tagResponses) {
                List<Medicine> medicines = medicineRepository.findAllByTagId(tagResponse.getId(), PageRequest.of(0, 10)).getContent();
                List<MedicineResponse> medicineResponses = medicines.stream().map(medicine -> medicineMapper.toDto(medicine)).toList();
                tagResponse.setMedicines(medicineResponses);
            }

            TagGroupResponse tagResponse = tagGroupMapper.toDto(tagGroup);
            tagResponse.setTags(tagResponses);

            return tagResponse;
        }).toList();
    }
}
