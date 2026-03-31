package dao.impl;

import dao.dao.ProductDAO;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> findAll(int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from products where status = 'ACTIVE' order by product_id limit ? offset ?";
        int offset = (page - 1) * pageSize;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int countActiveProducts() {
        String sql = "select count(*) from products where status = 'ACTIVE'";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Product findById(int id) {
        String sql = "select * from products where product_id = ? and status = 'ACTIVE'";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "select 1 from products where product_id = ? and status = 'ACTIVE'";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "select * from products where status = 'ACTIVE' order by product_id";
        List<Product> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean insert(Product p) {
        String sql = "insert into products(product_name, storage, color, price, stock, description, category_id, status) "
                + "values (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, p.getProductName().trim());
            ps.setString(2, p.getStorage().trim());
            ps.setString(3, p.getColor().trim());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getDescription().trim());
            ps.setInt(7, p.getCategoryId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Product p) {
        String sql = "update products set product_name = ?, storage = ?, color = ?, price = ?, stock = ?, description = ?, category_id = ? "
                + "where product_id = ? and status = 'ACTIVE'";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, p.getProductName().trim());
            ps.setString(2, p.getStorage().trim());
            ps.setString(3, p.getColor().trim());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getDescription().trim());
            ps.setInt(7, p.getCategoryId());
            ps.setInt(8, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean softDelete(int id) {
        String sql = "update products set status = 'DELETED' where product_id = ? and status = 'ACTIVE'";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from products where product_name like ? and status = 'ACTIVE' order by product_id";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> sortByPriceAsc() {
        return getSortedProducts("asc");
    }

    @Override
    public List<Product> sortByPriceDesc() {
        return getSortedProducts("desc");
    }

    @Override
    public List<Product> searchAdvanced(String keyword,
                                        Double minPrice,
                                        Double maxPrice,
                                        Integer categoryId,
                                        Boolean inStock,
                                        String sortByPrice) {
        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("select * from products where status = 'ACTIVE'");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" and product_name like ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (minPrice != null) {
            sql.append(" and price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" and price <= ?");
            params.add(maxPrice);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append(" and category_id = ?");
            params.add(categoryId);
        }

        if (inStock != null && inStock) {
            sql.append(" and stock > 0");
        }

        sql.append(" order by ");
        if ("DESC".equalsIgnoreCase(sortByPrice)) {
            sql.append("price desc, product_id desc");
        } else if ("ASC".equalsIgnoreCase(sortByPrice)) {
            sql.append("price asc, product_id asc");
        } else {
            sql.append("product_id asc");
        }

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                Object value = params.get(i);
                if (value instanceof String) {
                    ps.setString(i + 1, (String) value);
                } else if (value instanceof Double) {
                    ps.setDouble(i + 1, (Double) value);
                } else if (value instanceof Integer) {
                    ps.setInt(i + 1, (Integer) value);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private List<Product> getSortedProducts(String order) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from products where status = 'ACTIVE' order by price " + order;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Product mapResultSet(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setProductName(rs.getString("product_name"));
        p.setStorage(rs.getString("storage"));
        p.setColor(rs.getString("color"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setDescription(rs.getString("description"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getString("created_at"));
        return p;
    }
}