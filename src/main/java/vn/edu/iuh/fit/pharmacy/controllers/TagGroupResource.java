package vn.edu.iuh.fit.pharmacy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.pharmacy.service.TagGroupService;
import vn.edu.iuh.fit.pharmacy.utils.response.TagGroupResponse;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("tag-groups")
public class TagGroupResource {
    @Autowired
    private TagGroupService tagGroupService;
    @GetMapping("/{tagGroupId}")
    public ResponseEntity<TagGroupResponse> getById(@PathVariable Long tagGroupId) {
        return ResponseEntity.ok(
                tagGroupService.getTagGroupById(tagGroupId)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<TagGroupResponse>> getAll() {
        return ResponseEntity.ok(
                tagGroupService.getAllTagGroup()
        );
    }
}
