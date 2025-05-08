package model.dao;

import model.domain.Usuario;
import java.sql.*;
import model.database.Database;
import model.database.DatabaseFactory;


public class UsuarioDAO {
    private final Connection connection;

    public UsuarioDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, data_nascimento, saldo, senha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setDate(3, usuario.getDataNascimento());
            stmt.setDouble(4, usuario.getSaldo());
            stmt.setString(5, usuario.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public Usuario buscarPorEmailESenha(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setDataNascimento(rs.getDate("data_nascimento"));
                usuario.setSaldo(rs.getDouble("saldo"));
                usuario.setSenha(rs.getString("senha"));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // se não encontrar
    }
}
