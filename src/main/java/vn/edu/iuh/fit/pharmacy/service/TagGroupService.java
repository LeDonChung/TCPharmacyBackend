package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.utils.response.TagGroupResponse;

import java.util.List;

public interface TagGroupService {
    TagGroupResponse getTagGroupById(Long id);

    List<TagGroupResponse> getAllTagGroup();
}
