package service.service;

import model.TopProductReport;
import java.util.List;

public interface ReportService {
    List<TopProductReport> getTop5BestSellingProductsThisMonth();
}