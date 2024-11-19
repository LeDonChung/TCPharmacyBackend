package vn.edu.iuh.fit.pharmacy;

import net.datafaker.Faker;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import vn.edu.iuh.fit.pharmacy.POJOs.*;
import vn.edu.iuh.fit.pharmacy.api.*;
import vn.edu.iuh.fit.pharmacy.mappers.MedicineMapper;
import vn.edu.iuh.fit.pharmacy.repositories.*;
import vn.edu.iuh.fit.pharmacy.service.CollaborativeFiltering;
import vn.edu.iuh.fit.pharmacy.utils.RoleConstraints;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootTest
public class RenderTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void renderRole() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().code("ROLE_PATIENT").name("ROLE_PATIENT").build());
        roles.add(Role.builder().name("ROLE_DOCTOR").code("ROLE_DOCTOR").build());
        roles.add(Role.builder().code("ROLE_ADMIN").name("ROLE_ADMIN").build());

        roleRepository.saveAllAndFlush(roles);
    }

    @Autowired
    private TagGroupRepository tagGroupRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void renderBrand() {
        List<String> brands = List.of("new-nordic", "jpanwell", "brauer", "Vitabiotics", "pro-mum", "nunest", "okamoto", "pearlie-white", "kamicare", "kingphar", "fuji", "duy-thanh", "nutrimed", "uright-facare", "decumar", "sagami", "agimexpharm", "janssen", "pharbaco", "bilim-ilac", "medicraft", "fixderma", "crevil", "la-beauty", "terrapharm", "medinova", "mega-we-care", "garnier", "stella-pharm");


        List<BrandAPI> brandAPIS = new ArrayList<>();

        for (String brand : brands) {
            ResponseEntity<BrandAPI> response = restTemplate.getForEntity("https://nhathuoclongchau.com.vn/_next/data/4spj1LZ8XwtF7o1Jr_Lan/thuong-hieu/" + brand + ".json", BrandAPI.class);
            BrandAPI brandAPI = response.getBody();
            brandAPIS.add(brandAPI);
        }

        List<Brand> brandsToSave = brandAPIS.stream().map(brandAPI -> Brand.builder().title(brandAPI.pageProps.getInfoBrand().getBrand()).image(brandAPI.pageProps.getInfoBrand().getLogoBrand()).build()

        ).toList();


        brandRepository.saveAllAndFlush(brandsToSave);
    }

    @Test
    public void renderTag() {
        List<TagGroup> tagGroups = new ArrayList<>();

        // First TagGroup: "Đối tượng sử dụng"
        TagGroup group1 = TagGroup.builder().groupName("Đối tượng sử dụng").des("Đối tượng sử dụng").build();

        List<Tag> tagsForGroup1 = new ArrayList<>();
        tagsForGroup1.add(Tag.builder().title("Trẻ em").des("Trẻ em").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Tất cả").des("Tất cả").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Phụ nữ có thai").des("Phụ nữ có thai").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Phụ nữ cho con bú").des("Phụ nữ cho con bú").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Người lớn").des("Người lớn").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Người trưởng thành").des("Người trưởng thành").tagGroup(group1).build());
        tagsForGroup1.add(Tag.builder().title("Người cao tuổi").des("Người cao tuổi").tagGroup(group1).build());

        group1.setTags(tagsForGroup1);
        tagGroups.add(group1);

        // Second TagGroup: "Gợi ý hôm nay"
        TagGroup group2 = TagGroup.builder().groupName("Gợi ý hôm nay").des("Gợi ý hôm nay").build();

        List<Tag> tagsForGroup2 = new ArrayList<>();
        tagsForGroup2.add(Tag.builder().title("Đặc quyền").des("Đặc quyền").tagGroup(group2).build());
        tagsForGroup2.add(Tag.builder().title("Tìm kiếm nhiều").des("Tìm kiếm nhiều").tagGroup(group2).build());
        tagsForGroup2.add(Tag.builder().title("Sản phẩm mới").des("Sản phẩm mới").tagGroup(group2).build());
        tagsForGroup2.add(Tag.builder().title("Vitamin tổng hợp").des("Vitamin tổng hợp").tagGroup(group2).build());

        group2.setTags(tagsForGroup2);
        tagGroups.add(group2);

        tagGroupRepository.saveAllAndFlush(tagGroups);
    }

    @Test
    public void renderCategories() {

        ResponseEntity<ClazzAPI> response = restTemplate.getForEntity("https://nhathuoclongchau.com.vn/_next/data/4spj1LZ8XwtF7o1Jr_Lan/thuc-pham-chuc-nang/sinh-ly-noi-tiet-to.json?slug=thuc-pham-chuc-nang&slug=sinh-ly-noi-tiet-to", ClazzAPI.class);
        ClazzAPI clazzAPI = response.getBody();

        List<CategoryAPI> level01 = clazzAPI.getMasterLayoutDataProps().getMenu();

        List<CategoryAPI> level02 = new ArrayList<>();
        for (CategoryAPI categoryAPI : level01) {
            level02.addAll(categoryAPI.getChildren());
        }

        List<CategoryAPI> level03 = new ArrayList<>();
        for (CategoryAPI categoryAPI : level02) {
            if (categoryAPI.getChildren() != null) level03.addAll(categoryAPI.getChildren());
        }


        for (CategoryAPI categoryLevel01 : level01) {
            if (categoryLevel01.getImage() == null) continue;

            String image = categoryLevel01.getImage().getUrl();
            if (image == null) continue;

            Category category1 = Category.builder().title(categoryLevel01.getName()).level(1).icon(image).fullPathSlug(categoryLevel01.getFullPathSlug()).build();

            Category categoryNewLevel01 = categoryRepository.saveAndFlush(category1);

            if (categoryLevel01.getChildren() != null) {
                for (CategoryAPI categoryLevel02 : categoryLevel01.getChildren()) {
                    if (categoryLevel02.getImage() == null) continue;

                    String image2 = categoryLevel02.getImage().getUrl();
                    if (image2 == null) continue;

                    Category category2 = Category.builder().title(categoryLevel02.getName()).level(2).icon(image2).parent(categoryNewLevel01).fullPathSlug(categoryLevel02.getFullPathSlug()).build();

                    Category categoryNewLevel02 = categoryRepository.saveAndFlush(category2);

                    if (categoryLevel02.getChildren() != null) {
                        for (CategoryAPI categoryLevel03 : categoryLevel02.getChildren()) {
                            if (categoryLevel03.getImage() == null) continue;

                            String image3 = categoryLevel03.getImage().getUrl();
                            if (image3 == null) continue;

                            Category category3 = Category.builder().title(categoryLevel03.getName()).level(3).icon(image3).parent(categoryNewLevel02).fullPathSlug(categoryLevel03.getFullPathSlug()).build();

                            Category categoryNewLevel03 = categoryRepository.saveAndFlush(category3);
                        }
                    }
                }
            }

        }
    }

    @Test
    public void renderFix() {

        ResponseEntity<ClazzAPI> response = restTemplate.getForEntity("https://nhathuoclongchau.com.vn/_next/data/4spj1LZ8XwtF7o1Jr_Lan/thuc-pham-chuc-nang/sinh-ly-noi-tiet-to.json?slug=thuc-pham-chuc-nang&slug=sinh-ly-noi-tiet-to", ClazzAPI.class);
        ClazzAPI clazzAPI = response.getBody();

        List<CategoryAPI> level01 = clazzAPI.getMasterLayoutDataProps().getMenu();
        List<CategoryAPI> level02 = new ArrayList<>();
        for (CategoryAPI categoryAPI : level01) {
            level02.addAll(categoryAPI.getChildren());
        }
        List<CategoryAPI> level03 = new ArrayList<>();
        for (CategoryAPI categoryAPI : level02) {
            if (categoryAPI.getChildren() != null) level03.addAll(categoryAPI.getChildren());
        }
        List<CategoryAPI> result = new ArrayList<>(level01);
        result.addAll(level02);
        result.addAll(level03);

        for (CategoryAPI categoryAPI : result) {
            Category category = categoryRepository.findByFullPathSlug(categoryAPI.getFullPathSlug());
            if (category != null) {
                category.setIcon(categoryAPI.getImage().getUrl());
                categoryRepository.saveAndFlush(category);
            }
        }
    }

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void renderMedicine01() {

        List<Category> categories = categoryRepository.findAll();
        categories = categories.stream().filter(category -> category.getId() > 128).toList();
        List<Brand> brands = brandRepository.findAll();

        List<Tag> tags = tagRepository.findAll();

        for (Category category : categories) {
            String url = "https://nhathuoclongchau.com.vn/_next/data/4spj1LZ8XwtF7o1Jr_Lan" + category.getFullPathSlug() + ".json";
            ResponseEntity<MedicineRootAPI> response = restTemplate.getForEntity(url, MedicineRootAPI.class);

            // CategoryAPI
            MedicineRootAPI clazzAPI = response.getBody();

            if (clazzAPI.getPageProps() != null && clazzAPI.getPageProps().getViewData() != null && clazzAPI.getPageProps().getViewData().getProducts() != null && !clazzAPI.getPageProps().getViewData().getProducts().isEmpty()) {

                for (ProductAPI productAPI : clazzAPI.getPageProps().getViewData().getProducts()) {
                    String urlProduct = "https://nhathuoclongchau.com.vn/_next/data/4spj1LZ8XwtF7o1Jr_Lan/products/" + productAPI.getSlug() + ".json";
                    ResponseEntity<MedicineRootAPI> response2 = restTemplate.getForEntity(urlProduct, MedicineRootAPI.class);

                    MedicineRootAPI clazzAPIProduct = response2.getBody();

                    // ADD MEDICINE
                    if (clazzAPIProduct.getPageProps() != null && clazzAPIProduct.getPageProps().getProduct() != null) {
                        MedicineAPI medicineAPI = clazzAPIProduct.getPageProps().getProduct();
                        Optional<Medicine> medicineOptional = medicineRepository.findBySku(medicineAPI.getSku());
                        if (medicineOptional.isPresent()) {
                            continue;
                        }

                        Medicine medicine = Medicine.builder().category(category).sku(medicineAPI.getSku()).des(medicineAPI.getDescription()).desShort(medicineAPI.getShortDescription()).primaryImage(medicineAPI.getPrimaryImage()).status(1).specifications(medicineAPI.getSpecification()).name(medicineAPI.getWebName()).quantity(100).build();
                        // List Medical Image
                        List<MedicineImage> medicineImages = new ArrayList<>();
                        if (medicineAPI.getSecondaryImages() != null) {
                            for (String image : medicineAPI.getSecondaryImages()) {
                                MedicineImage medicineImage = MedicineImage.builder().url(image).medicine(medicine).build();
                                medicineImages.add(medicineImage);
                            }
                        }

                        // Brand
                        int indexBrand = new Random().nextInt(brands.size());

                        medicine.setBrand(brands.get(indexBrand));

                        // Tags
                        int numberOfTags = new Random().nextInt(4) + 1;


                        Set<Tag> selectedTags = new HashSet<>();
                        for (int i = 0; i < numberOfTags; i++) {

                            int randomIndex = new Random().nextInt(tags.size());

                            selectedTags.add(tags.get(randomIndex));
                        }

                        // Discount
                        int discount = new Random().nextInt(70);

                        // Star
                        int star = new Random().nextInt(5);

                        // Review
                        int review = new Random().nextInt(100);


                        // Price
                        // Unit
                        if (!medicineAPI.getPrices().isEmpty()) {
                            PriceAPI priceAPI = medicineAPI.getPrices().get(0);
                            medicine.setInit(priceAPI.getMeasureUnitName());
                            medicine.setPrice(priceAPI.getPrice());
                        }


                        medicine.setReviews(review);
                        medicine.setStar(star);
                        medicine.setDiscount(discount);
                        medicine.setTags(selectedTags);
                        medicine.setMedicineImages(medicineImages);
                        medicineRepository.saveAndFlush(medicine);
                    }
                }
            }

            System.out.println("Done category: " + category.getId());
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void renderUsers() {
        try {
            Faker faker = new Faker(Locale.forLanguageTag("vi"));
            List<User> users = new ArrayList<>();
            List<Gender> genders = List.of(Gender.values());
            for (int i = 0; i <= 100; i++) {
                User user = User.builder().dob(Timestamp.from(faker.date().birthday().toInstant())).gender(genders.get(new Random().nextInt(genders.size()))).fullName(faker.name().fullName()).phoneNumber(faker.phoneNumber().cellPhone()).password("default").verify(true).image("https://scontent.fsgn5-14.fna.fbcdn.net").enabled(true).build();

                // Tạo và thêm Address
                List<Address> addresses = new ArrayList<>();
                String fullName = user.getFullName();
                String phoneNumber = user.getPhoneNumber();
                if (userRepository.findByUsername(phoneNumber).isPresent()) {
                    continue;
                }
                for (int j = 0; j < 2; j++) {
                    Address address = Address.builder().user(user).addressType(j == 0 ? AddressType.Home : AddressType.Other).district(faker.address().city()).province(faker.address().state()).street(faker.address().streetAddress()).ward(faker.address().cityName()).isDefault(j == 0).fullName(j == 0 ? fullName : faker.name().fullName()).phoneNumber(j == 0 ? phoneNumber : faker.phoneNumber().cellPhone()).build();
                    addresses.add(address);
                }
                user.setAddresses(addresses);

                // Point
                Point point = Point.builder().currentPoint(0).rank(Rank.Silver).updateAt(Timestamp.from(Instant.now())).user(user).build();
                user.setPoint(point);
                users.add(user);
                userRepository.saveAndFlush(user);

            }

            Role role = Role.builder().id(1L).code(RoleConstraints.ROLE_PATIENT).name("ROLE_PATIENT").build();


            userRepository.findAll().stream().map(user -> {
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                user.setRoles(roles);
                return user;
            }).forEach(user -> userRepository.saveAndFlush(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private OrderRepository orderRepository;

//    @Test
//    public void renderOrder() {
//        PageRequest pageRequest = PageRequest.of(0, 1000);
//        List<User> users = userRepository.findAll(pageRequest).getContent();
//        Faker faker = new Faker(Locale.forLanguageTag("vi"));
//        Random random = new Random();
//
//        // Lấy ngẫu nhiên 100 sản phẩm
//        PageRequest pageRequestMedicine = PageRequest.of(0, 100);
//        List<Medicine> medicines = medicineRepository.findAll(pageRequestMedicine).stream().toList();
//        for(int x = 1; x <= 10; x++){
//            for (User user : users) {
//                // Tạo 2-5 đơn hàng cho mỗi khách hàng
//                int orderCount = 2 + random.nextInt(4); // Giữa 2 và 5 đơn hàng
//
//                for (int j = 0; j < orderCount; j++) {
//
////                // Tạo thời gian ngẫu nhiên trong khoảng từ 2023 đến hiện tại
////                LocalDateTime startDate = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0);  // Ngày bắt đầu là 1 tháng 1, 2023
////                LocalDateTime now = LocalDateTime.now();  // Ngày hiện tại
////                long daysBetween = ChronoUnit.DAYS.between(startDate, now);  // Tính số ngày giữa hai ngày
////                LocalDateTime randomOrderDate = startDate.plusDays(random.nextInt((int) daysBetween));
//
//                    // Xác định thời gian 3 tháng gần nhất
//                    LocalDateTime now = LocalDateTime.now();  // Ngày hiện tại
//                    LocalDateTime threeMonthsAgo = now.minusMonths(3);  // 3 tháng trước
//
//                    // Tính số ngày giữa 3 tháng trước và ngày hiện tại
//                    long daysBetween = ChronoUnit.DAYS.between(threeMonthsAgo, now);
//
//                    // Tạo ngày ngẫu nhiên trong khoảng thời gian 3 tháng
//                    LocalDateTime randomOrderDate = threeMonthsAgo.plusDays(random.nextInt((int) daysBetween));
//
//                    // Tạo đơn hàng giả
//                    Order order = Order.builder().orderDate(Timestamp.valueOf(randomOrderDate)).status(OrderStatus.DELIVERED).point(0).usePoint(false).feeShip(0.0).exportInvoice(false).note(faker.lorem().sentence()).build();
//
//                    Address address = user.getAddresses().stream().filter(Address::isDefault).findFirst().orElse(null);
//
//                    Address addressNew = Address.builder().addressType(address.getAddressType()).district(address.getDistrict()).province(address.getProvince()).street(address.getStreet()).ward(address.getWard()).isDefault(address.isDefault()).fullName(address.getFullName()).phoneNumber(address.getPhoneNumber()).build();
//
//
//                    // Inser Address to Order
//                    order.setAddress(addressNew);
//
//                    User finalUser = userRepository.findById(user.getId()).orElse(null);
//                    order.setUser(finalUser);
//
//                    order = orderRepository.saveAndFlush(order);
//
//                    // Tạo 2-3 sản phẩm cho mỗi đơn hàng
//                    List<OrderDetail> orderDetails = new ArrayList<>();
//                    int productCount = 4 + random.nextInt(8); // Giữa 2 và 3 sản phẩm cho mỗi đơn hàng
//                    for (int k = 0; k < productCount; k++) {
//                        // Lấy một sản phẩm từ danh sách sản phẩm
//                        Medicine medicine = medicines.get(random.nextInt(medicines.size()));
//                        double price = medicine.getPrice();
//                        double discount = medicine.getDiscount();
//
//
//                        OrderDetailId orderDetailId = new OrderDetailId(order.getId(), medicine.getId());
//                        OrderDetail orderDetail = OrderDetail.builder().id(orderDetailId).quantity(1 + random.nextInt(3)) // Random số lượng từ 1 đến 3
//                                .price(price) // Tính giá sau giảm
//                                .medicine(medicine).order(order).discount(discount).build();
//
//                        // Kiểm tra sự tồn tại của OrderDetail trong danh sách dựa trên order và medicine
//                        if (!orderDetails.contains(orderDetail)) {
//                            orderDetails.add(orderDetail);  // Thêm OrderDetail nếu chưa có
//                        }
//                        orderDetails.add(orderDetail);
//                    }
//
//
//                    order.setOrderDetails(new HashSet<>(orderDetails));
//                    orderRepository.saveAndFlush(order);
//
//                }
//
//            }
//        }
//
//    }

    @Test
    public void renderOrder() {
        PageRequest pageRequest = PageRequest.of(0, 1000);
        List<User> users = userRepository.findAll(pageRequest).getContent();
        Faker faker = new Faker(Locale.forLanguageTag("vi"));
        Random random = new Random();

        // Lấy ngẫu nhiên 5 sản phẩm
        PageRequest pageRequestMedicine = PageRequest.of(0, 5);
        List<Medicine> medicines = medicineRepository.findAll(pageRequestMedicine).stream().toList();

        // Random hóa đơn hàng cho mỗi user
        for (User user : users) {
            // Tạo 3-7 đơn hàng cho mỗi khách hàng
            int orderCount = 3 + random.nextInt(7); // Giữa 2 và 5 đơn hàng

            for (int j = 0; j < orderCount; j++) {

                // Xác định thời gian 3 tháng gần nhất
                LocalDateTime now = LocalDateTime.now();  // Ngày hiện tại
                LocalDateTime threeMonthsAgo = now.minusMonths(3);  // 3 tháng trước

                long daysBetween = ChronoUnit.DAYS.between(threeMonthsAgo, now);
                LocalDateTime randomOrderDate = threeMonthsAgo.plusDays(random.nextInt((int) daysBetween));

                // Tạo đơn hàng giả
                Order order = Order.builder().orderDate(Timestamp.valueOf(randomOrderDate)).status(OrderStatus.DELIVERED).point(0).usePoint(false).feeShip(0.0).exportInvoice(false).note(faker.lorem().sentence()).build();

                Address address = user.getAddresses().stream().filter(Address::isDefault).findFirst().orElse(null);

                Address addressNew = Address.builder().addressType(address.getAddressType()).district(address.getDistrict()).province(address.getProvince()).street(address.getStreet()).ward(address.getWard()).isDefault(address.isDefault()).fullName(address.getFullName()).phoneNumber(address.getPhoneNumber()).build();


                order.setAddress(addressNew);
                User finalUser = userRepository.findById(user.getId()).orElse(null);
                order.setUser(finalUser);

                // Save order vào DB
                order = orderRepository.saveAndFlush(order);

                // Random hóa số lượng sản phẩm và các sản phẩm trong đơn hàng
                List<OrderDetail> orderDetails = new ArrayList<>();
                int productCount = 4 + random.nextInt(8); // Giữa 4 và 12 sản phẩm cho mỗi đơn hàng

                for (int k = 0; k < productCount; k++) {
                    // Random một sản phẩm từ danh sách sản phẩm
                    Medicine medicine = medicines.get(random.nextInt(medicines.size()));
                    double price = medicine.getPrice();
                    double discount = medicine.getDiscount();

                    OrderDetailId orderDetailId = new OrderDetailId(order.getId(), medicine.getId());
                    OrderDetail orderDetail = OrderDetail.builder()
                            .id(orderDetailId)
                            .quantity(1 + random.nextInt(3)) // Random số lượng từ 1 đến 3
                            .price(price)  // Giá sau giảm
                            .medicine(medicine)
                            .order(order)
                            .discount(discount)
                            .build();

                    // Kiểm tra sự tồn tại của OrderDetail trong danh sách dựa trên order và medicine
                    if (!orderDetails.contains(orderDetail)) {
                        orderDetails.add(orderDetail);  // Thêm OrderDetail nếu chưa có
                    }
                }

                // Set order details vào order
                order.setOrderDetails(new HashSet<>(orderDetails));
                orderRepository.saveAndFlush(order);
            }
        }
    }


    private Medicine getRandomMedicine() {
        // Giả sử bạn có danh sách các sản phẩm, lấy một sản phẩm ngẫu nhiên từ danh sách
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.get(new Random().nextInt(medicines.size()));
    }


    @Test
    public void renderLikeMedicine() {
        List<User> users = userRepository.findAll(PageRequest.of(0, 50)).getContent();
        List<Medicine> medicines = medicineRepository.findAll();
        Random random = new Random();
        for (User user : users) {
            int likeCount = 10 + random.nextInt(15); // Giữa 3 và 7 sản phẩm yêu thích
            List<Medicine> likedMedicines = new ArrayList<>();
            for (int i = 0; i < likeCount; i++) {
                Medicine medicine = medicines.get(random.nextInt(medicines.size()));
                if (!likedMedicines.contains(medicine)) {
                    likedMedicines.add(medicine);
                }
            }
            user.setLikes(likedMedicines);
            userRepository.saveAndFlush(user);
        }
    }

    @Autowired
    private CollaborativeFiltering collaborativeFiltering;
    @Autowired
    private MedicineMapper medicineMapper;

    @Test
    public void testCollaborativeFiltering() throws Exception {
        collaborativeFiltering.buildModel();

        List<RecommendedItem> recommendedItems = collaborativeFiltering.recommendMedicinesForUser(45L);

        recommendedItems.forEach(System.out::println);

    }

    @Test
    // Xuất dữ liệu ra file Word
    public void exportToWord() throws IOException {
        List<Medicine> medicines = medicineRepository.findAll();

        // Tạo tài liệu Word
        XWPFDocument document = new XWPFDocument();

        // Thêm Title
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runTitle = title.createRun();
        runTitle.setText("Danh Sách Sản Phẩm Thuốc");
        runTitle.setBold(true);
        runTitle.setFontSize(16);

        // Thêm Heading
        XWPFParagraph heading = document.createParagraph();
        heading.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runHeading = heading.createRun();
        runHeading.setText("Thông tin chi tiết các sản phẩm thuốc:");
        runHeading.setBold(true);
        runHeading.setFontSize(14);

        // Thêm bảng để hiển thị dữ liệu
        XWPFTable table = document.createTable();

        // Tạo dòng tiêu đề cho bảng
        XWPFTableRow row = table.getRow(0);
        row.getCell(0).setText("Tên Sản Phẩm");
        row.addNewTableCell().setText("Giá");
        row.addNewTableCell().setText("Đơn Vị");
        row.addNewTableCell().setText("Thông Số Kỹ Thuật");
        row.addNewTableCell().setText("Mô Tả Ngắn");
        row.addNewTableCell().setText("Giảm Giá");
        row.addNewTableCell().setText("Thương Hiệu");
        row.addNewTableCell().setText("Thể Loại");
        row.addNewTableCell().setText("Mô Tả Dài");

        // Thêm dữ liệu từ danh sách thuốc vào bảng
        for (Medicine medicine : medicines) {
            row = table.createRow();
            row.getCell(0).setText(medicine.getName());
            row.getCell(1).setText(String.valueOf(medicine.getPrice()));
            row.getCell(2).setText(medicine.getInit()); // Đơn vị (nếu có thêm thuộc tính Unit trong entity)
            row.getCell(3).setText(medicine.getSpecifications());
            row.getCell(4).setText(medicine.getDesShort());
            row.getCell(5).setText(String.valueOf(medicine.getDiscount()+"%"));
            row.getCell(6).setText(medicine.getBrand() != null ? medicine.getBrand().getTitle() : "N/A");
            row.getCell(7).setText(medicine.getCategory() != null ? medicine.getCategory().getTitle() : "N/A");
            // Chuyển đổi HTML thành văn bản có thể hiển thị trong Word
            String htmlDescription = medicine.getDes(); // Mô tả dài dưới dạng HTML
            String cleanText = convertHtmlToText(htmlDescription);

            row.getCell(8).setText(cleanText);
        }

        // Ghi tài liệu vào file
        try (FileOutputStream fileOut = new FileOutputStream("src/main/resources/medicines.docx")) {
            document.write(fileOut);
        }
        document.close();
    }
    // Hàm chuyển HTML thành văn bản
    private String convertHtmlToText(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder text = new StringBuilder();

        // Xử lý các thẻ HTML và chuyển thành văn bản
        for (Element element : doc.body().children()) {
            if (element.tagName().equals("p")) {
                text.append(element.text()).append("\n");
            } else if (element.tagName().equals("ul")) {
                for (Element li : element.children()) {
                    text.append("- ").append(li.text()).append("\n");
                }
            } else {
                text.append(element.text()).append("\n");
            }
        }
        return text.toString();
    }
}
