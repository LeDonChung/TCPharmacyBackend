package vn.edu.iuh.fit.pharmacy.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.repositories.UserRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborativeFiltering {

    @Autowired
    private UserRepository userRepository;

    public void buildModel() throws IOException {
        List<User> users = userRepository.findAll();
        File dir = new File("src/main/resources");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter("src/main/resources/rules.csv"), CSVFormat.DEFAULT)) {
            for (User user : users) {
                for (Medicine medicine : user.getLikes()) {
                    csvPrinter.printRecord(user.getId(), medicine.getId(), 1);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error writing to CSV file", e);
        }
    }

    public List<RecommendedItem> recommendMedicinesForUser(Long userId) throws Exception {
        DataModel model = new FileDataModel(new File("src/main/resources/rules.csv"));

        // User-based Collaborative Filtering
        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, userSimilarity, model);
        GenericUserBasedRecommender userBasedRecommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
        List<RecommendedItem> userBasedRecommendations = userBasedRecommender.recommend(userId, 5);

        // Item-based Collaborative Filtering
        ItemSimilarity itemSimilarity = new TanimotoCoefficientSimilarity(model);
        GenericItemBasedRecommender itemBasedRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
        List<RecommendedItem> itemBasedRecommendations = itemBasedRecommender.recommend(userId, 5);

        // Kết hợp kết quả từ cả hai thuật toán
        Map<Long, Float> combinedRecommendations = new HashMap<>();

        for (RecommendedItem item : userBasedRecommendations) {
            combinedRecommendations.merge(item.getItemID(), item.getValue(), Float::sum);
        }

        for (RecommendedItem item : itemBasedRecommendations) {
            combinedRecommendations.merge(item.getItemID(), item.getValue(), Float::sum);
        }

        // Sắp xếp kết quả theo giá trị tổng hợp và lấy top N
        return combinedRecommendations.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Float>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new RecommendedItem() {
                    @Override
                    public long getItemID() {
                        return entry.getKey();
                    }

                    @Override
                    public float getValue() {
                        return entry.getValue();
                    }
                })
                .collect(Collectors.toList());
    }
}
