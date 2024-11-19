package vn.edu.iuh.fit.pharmacy.utils.response;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;

import java.util.Collection;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TagGroupResponse {
    private Long id;

    private String groupName;

    private String des;

    private Collection<TagResponse> tags;
}
