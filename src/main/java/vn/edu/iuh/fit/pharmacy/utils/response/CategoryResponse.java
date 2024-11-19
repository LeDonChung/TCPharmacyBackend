package vn.edu.iuh.fit.pharmacy.utils.response;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long id;

    private String fullPathSlug;
    private String title;

    private int level;

    private String icon;

    private Long parent;

    private Collection<CategoryResponse> children;
}
