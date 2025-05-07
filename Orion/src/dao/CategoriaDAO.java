package dao;

// @author lorrayne
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.database.Database;
import model.database.DatabaseFactory;

public class CategoriaDAO {

    private final Connection connection;

    public CategoriaDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public void inserir(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, tipo, descricao, ativo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setBoolean(4, categoria.isAtivo());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, tipo = ?, descricao = ?, ativo = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setBoolean(4, categoria.isAtivo());
            stmt.setInt(5, categoria.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Categoria buscarPorId(int id) throws SQLException {
        Categoria categoria = null;
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                categoria = new Categoria(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getBoolean("ativo")
                );
            }
        }
        return categoria;
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY nome";
        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("tipo"),
                    rs.getString("descricao"),
                    rs.getBoolean("ativo")
                );
                categorias.add(c);
            }
        }
        return categorias;
    }
}
