package dao.impl;

import dao.dao.ReportDAO;
import model.TopProductReport;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<TopProductReport> getTop5BestSellingProductsThisMonth() {
        List<TopProductReport> list = new ArrayList<>();

        String sql = """
                select p.product_id, p.product_name, sum(od.quantity) as total_sold
                from order_details od
                join orders o on od.order_id = o.order_id
                join products p on od.product_id = p.product_id
                where month(o.created_at) = month(curdate())
                  and year(o.created_at) = year(curdate())
                  and o.status in ('SHIPPING', 'DELIVERED')
                group by p.product_id, p.product_name
                order by total_sold desc
                limit 5
                """;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                TopProductReport item = new TopProductReport();
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setTotalSold(rs.getInt("total_sold"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}