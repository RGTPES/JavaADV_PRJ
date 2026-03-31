package presentation;

import model.TopProductReport;
import service.impl.ReportServiceImpl;

import java.util.List;

public class ReportMenu {
    private final ReportServiceImpl reportService = new ReportServiceImpl();

    public void showTop5BestSellingProductsThisMonth() {
        List<TopProductReport> list = reportService.getTop5BestSellingProductsThisMonth();

        if (list == null || list.isEmpty()) {
            System.out.println("Khong co du lieu top 5 san pham ban chay thang nay.");
            return;
        }

        System.out.println("\n========== TOP 5 SAN PHAM BAN CHAY NHAT THANG ==========");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-10s %-30s %-10s%n", "ID", "Ten san pham", "Da ban");
        System.out.println("------------------------------------------------------------");

        for (TopProductReport item : list) {
            System.out.printf("%-10d %-30s %-10d%n",
                    item.getProductId(),
                    item.getProductName(),
                    item.getTotalSold());
        }

        System.out.println("------------------------------------------------------------");
    }
}