package vn.edu.iuh.fit.pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.*;
import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.mappers.MedicineMapper;
import vn.edu.iuh.fit.pharmacy.repositories.*;
import vn.edu.iuh.fit.pharmacy.service.MedicineService;
import vn.edu.iuh.fit.pharmacy.service.RecommendService;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineMapper medicineMapper;

    @Override
    public MedicineResponse getMedicineById(Long medicineId) throws MedicineException {
        Medicine medicine = medicineRepository.findById(medicineId).orElse(null);
        if (medicine == null)
            throw new MedicineException("Thuốc không tồn tại.");
        return medicineMapper.toDto(medicine);
    }

    @Override
    public List<MedicineResponse> getMedicineByTagId(Long tagId) {
        List<Medicine> medicines = medicineRepository.findAllByTagId(tagId, PageRequest.of(0, 10)).getContent();
        return medicines.stream().map(medicine -> medicineMapper.toDto(medicine)).toList();
    }

    @Override
    public List<MedicineResponse> getMedicineByTags(List<Long> tagIds) {
        List<Medicine> medicines = medicineRepository.findAllByTagIds(tagIds, PageRequest.of(0, 10)).getContent();
        return medicines.stream().map(medicine -> medicineMapper.toDto(medicine)).toList();
    }

    @Autowired
    private RecommendService recommendService;

    @Override
    public List<MedicineResponse> getRecommendations(List<Long> medicineIds) {
        return recommendService.getRecommendations(medicineIds);
    }


    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<MedicineResponse> getRecommendations2(List<Long> medicineIds) throws Exception {

        // 1. Lấy tất cả sản phẩm
        List<Medicine> allMedicines = medicineRepository.findAll();

        // 2. Lấy tất cả đơn hàng
        List<Order> orders = orderRepository.findAll();

        // 3. Chuyển đổi dữ liệu đơn hàng thành instances
        Instances dataset = convertOrderDetailsToInstances(orders, allMedicines);

        // 4. Áp dụng K-Means phân cụm
        SimpleKMeans kMeans = applyKMeans(dataset, 3); // Số cụm là 3, có thể thay đổi tùy theo yêu cầu

        // 5. Gợi ý sản phẩm dựa trên các sản phẩm đã chọn
        List<Long> recommendedProducts = recommendProducts(dataset, kMeans, medicineIds, 10); // Gợi ý các sản phẩm có thể được mua sau khi mua sản phẩm có ID 101 và 102

        // 6. In kết quả gợi ý
        System.out.println("Sản phẩm gợi ý: " + recommendedProducts);

        // 7. Lấy thông tin chi tiết của các sản phẩm gợi ý
        List<Medicine> recommendedMedicines = recommendedProducts.stream()
                .map(productId -> allMedicines.stream().filter(medicine -> medicine.getId().equals(productId)).findFirst().orElse(null))
                .toList();
        return recommendedMedicines.stream().map(medicineMapper::toDto).collect(Collectors.toList());
    }

    // Phương thức áp dụng K-Means phân cụm
    public SimpleKMeans applyKMeans(Instances dataset, int numClusters) throws Exception {
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(numClusters); // Số cụm (clusters)
        kMeans.buildClusterer(dataset); // Huấn luyện mô hình K-Means với dữ liệu

        // In kết quả phân cụm
        System.out.println(kMeans);

        return kMeans;
    }

    public List<Long> recommendProducts(Instances dataset, SimpleKMeans kMeans, List<Long> selectedProductIds, int k) throws Exception {
        List<Long> recommendedProductIds = new ArrayList<>();

        // Duyệt qua tất cả các sản phẩm đã chọn và lấy chỉ số của các sản phẩm này trong dataset
        for (Long selectedProductId : selectedProductIds) {
            int productIndex = findProductIndex(selectedProductId, getAllMedicines());
            if (productIndex == -1) {
                continue;
            }

            // Ensure the product index is within the valid range of the dataset
            if (productIndex < dataset.numInstances()) {
                // Lấy chỉ số cụm của sản phẩm đã chọn
                int cluster = kMeans.clusterInstance(dataset.instance(productIndex));
                System.out.println("Sản phẩm " + selectedProductId + " thuộc cụm: " + cluster);

                // Sử dụng một Map để đếm số lần xuất hiện của sản phẩm trong cụm
                Map<Long, Integer> productFrequencyMap = new HashMap<>();

                // Duyệt qua tất cả các sản phẩm trong cụm đó và đếm số lần xuất hiện
                for (int i = 0; i < dataset.numInstances(); i++) {
                    if (kMeans.clusterInstance(dataset.instance(i)) == cluster) {
                        // Lấy sản phẩm trong cùng cụm
                        Medicine recommendedProduct = getProductByIndex(i);
                        if (recommendedProduct != null) {
                            // Đếm số lần xuất hiện của sản phẩm trong cụm
                            productFrequencyMap.put(recommendedProduct.getId(), productFrequencyMap.getOrDefault(recommendedProduct.getId(), 0) + 1);
                        }
                    }
                }

                // Sắp xếp các sản phẩm theo số lần xuất hiện giảm dần
                List<Map.Entry<Long, Integer>> sortedEntries = new ArrayList<>(productFrequencyMap.entrySet());
                sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                // Lấy ra K sản phẩm xuất hiện nhiều nhất, đảm bảo không vượt quá K
                for (Map.Entry<Long, Integer> entry : sortedEntries) {
                    if (recommendedProductIds.size() < k) {
                        // Chỉ thêm sản phẩm nếu chưa đủ K sản phẩm
                        recommendedProductIds.add(entry.getKey());
                    } else {
                        break; // Nếu đã đủ K sản phẩm thì dừng lại
                    }
                }

                // Nếu đã đủ K sản phẩm thì dừng lại
                if (recommendedProductIds.size() >= k) {
                    break;
                }
            }
        }

        return recommendedProductIds;
    }

    // Giả lập lấy danh sách tất cả sản phẩm
    private List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // Phương thức lấy sản phẩm từ chỉ số với kiểm tra chỉ số hợp lệ
    private Medicine getProductByIndex(int index) {
        List<Medicine> allMedicines = getAllMedicines();
        if (index >= 0 && index < allMedicines.size()) {
            return allMedicines.get(index); // Trả về sản phẩm nếu chỉ số hợp lệ
        } else {
            // Log lỗi hoặc trả về null nếu chỉ số không hợp lệ
            return null;
        }
    }

    // Phương thức chuyển đổi dữ liệu đơn hàng thành ma trận các sản phẩm đã đặt
    public Instances convertOrderDetailsToInstances(List<Order> orders, List<Medicine> allMedicines) {
        // Tạo một Set lưu trữ các ID sản phẩm đã được đặt
        Set<Long> orderedProductIds = new HashSet<>();
        for (Order order : orders) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                orderedProductIds.add(orderDetail.getMedicine().getId());
            }
        }

        // Lọc ra các sản phẩm đã được đặt
        List<Medicine> orderedMedicines = new ArrayList<>();
        for (Medicine medicine : allMedicines) {
            if (orderedProductIds.contains(medicine.getId())) {
                orderedMedicines.add(medicine);
            }
        }

        // Tạo danh sách các thuộc tính cho các sản phẩm đã được đặt
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (Medicine medicine : orderedMedicines) {
            attributes.add(new Attribute("product_" + medicine.getId()));
        }

        // Tạo dataset chỉ với các sản phẩm đã đặt
        Instances dataset = new Instances("OrderedProductDataset", attributes, orders.size());

        // Duyệt qua các đơn hàng và thêm dữ liệu vào dataset
        for (Order order : orders) {
            double[] values = new double[dataset.numAttributes()];

            // Đánh dấu sự xuất hiện của sản phẩm trong đơn hàng
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Medicine medicine = orderDetail.getMedicine();
                if (orderedProductIds.contains(medicine.getId())) {
                    int index = findProductIndex(medicine.getId(), orderedMedicines);
                    if (index != -1 && index < orderedMedicines.size()) {  // Ensure index is within bounds
                        values[index] = 1; // Sản phẩm có trong đơn hàng
                    }
                }
            }

            dataset.add(new DenseInstance(1.0, values));
        }
        return dataset;
    }

    // Tối ưu hóa việc tìm kiếm chỉ số sản phẩm
    private int findProductIndex(Long productId, List<Medicine> orderedMedicines) {
        // Tìm kiếm chỉ số của sản phẩm trong danh sách sản phẩm đã đặt
        for (int i = 0; i < orderedMedicines.size(); i++) {
            if (orderedMedicines.get(i).getId().equals(productId)) {
                return i;
            }
        }
        return -1; // Không tìm thấy sản phẩm
    }

}
