package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.database.Database;
import model.database.DatabaseFactory;
import model.domain.Local;

/**
 *
 * @author lorrayne
 */
public class LocalDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public LocalDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public List<Local> listar() {
        List<Local> locais = new ArrayList<>();
        String sql = "SELECT id_local, nome FROM local_transacao ORDER BY nome";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Local local = new Local();
                local.setIdLocal(resultado.getInt("id_local"));
                local.setNome(resultado.getString("nome"));

                locais.add(local);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return locais;
    }

}
