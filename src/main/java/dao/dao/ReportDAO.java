package dao.dao;

import model.TopProductReport;
import java.util.List;

public interface ReportDAO {
    List<TopProductReport> getTop5BestSellingProductsThisMonth();
}