package service.impl;

import dao.dao.ReportDAO;
import dao.impl.ReportDAOImpl;
import model.TopProductReport;
import service.service.ReportService;

import java.util.List;

public class ReportServiceImpl implements ReportService {
    private final ReportDAO reportDAO = new ReportDAOImpl();

    @Override
    public List<TopProductReport> getTop5BestSellingProductsThisMonth() {
        return reportDAO.getTop5BestSellingProductsThisMonth();
    }
}