
package vn.edu.iuh.fit.pharmacy.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryAPI {
    private String component;
    private String fullPathSlug;
    private String name;
    private ImageAPI image;
    private List<CategoryAPI> children;
}
