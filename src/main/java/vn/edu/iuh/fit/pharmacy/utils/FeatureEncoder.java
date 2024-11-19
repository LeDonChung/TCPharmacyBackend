package vn.edu.iuh.fit.pharmacy.utils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import vn.edu.iuh.fit.pharmacy.POJOs.Brand;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FeatureEncoder {

    // Mã hóa danh mục thành one-hot encoding
    public static Map<String, Integer> encodeCategory(Category category, List<String> allCategoryTitles) {
        Map<String, Integer> encodedCategory = new HashMap<>();
        for (String categoryTitle : allCategoryTitles) {
            encodedCategory.put(categoryTitle, category.getTitle().equals(categoryTitle) ? 1 : 0);
        }
        return encodedCategory;
    }

    // Mã hóa thương hiệu
    public static Map<String, Integer> encodeBrand(Brand brand, List<String> allBrandTitles) {
        Map<String, Integer> encodedBrand = new HashMap<>();
        for (String brandTitle : allBrandTitles) {
            encodedBrand.put(brandTitle, brand.getTitle().equals(brandTitle) ? 1 : 0);
        }
        return encodedBrand;
    }

    // Mã hóa thẻ sản phẩm
    public static Map<String, Integer> encodeTags(Collection<Tag> tags, List<String> allTagTitles) {
        Map<String, Integer> encodedTags = new HashMap<>();
        for (String tagTitle : allTagTitles) {
            encodedTags.put(tagTitle, tags.stream().anyMatch(tag -> tag.getTitle().equals(tagTitle)) ? 1 : 0);
        }
        return encodedTags;
    }

    // Hàm tính Cosine Similarity giữa hai vector
    public static double cosineSimilarity(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        // Kiểm tra kích thước của hai vector
        if (vector1.size() != vector2.size()) {
            throw new IllegalArgumentException("Vectors must have the same size for cosine similarity calculation.");
        }

        RealVector v1 = new ArrayRealVector(vector1.values().stream().mapToDouble(i -> i).toArray());
        RealVector v2 = new ArrayRealVector(vector2.values().stream().mapToDouble(i -> i).toArray());
        return v1.cosine(v2);
    }

    // Mã hóa tất cả thuộc tính của sản phẩm thành vector
    public static Map<String, Integer> encodeProduct(Medicine product, List<String> allFeatures) {
        Map<String, Integer> encodedProduct = new HashMap<>();

        // Mã hóa Category
        Map<String, Integer> encodedCategory = encodeCategory(product.getCategory(), allFeatures.subList(0, allFeatures.size() / 3));
        encodedProduct.putAll(encodedCategory);

        // Mã hóa Brand
        Map<String, Integer> encodedBrand = encodeBrand(product.getBrand(), allFeatures.subList(allFeatures.size() / 3, allFeatures.size() * 2 / 3));
        encodedProduct.putAll(encodedBrand);

        // Mã hóa Tags
        Map<String, Integer> encodedTags = encodeTags(product.getTags(), allFeatures.subList(allFeatures.size() * 2 / 3, allFeatures.size()));
        encodedProduct.putAll(encodedTags);

        return encodedProduct;
    }
}
