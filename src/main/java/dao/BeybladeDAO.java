package dao;

import model.Beyblade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class BeybladeDAO extends DAO {	
	public BeybladeDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Beyblade beyblade) {
		boolean status = false;
		try {
			String sql = "INSERT INTO beyblade (descricao, preco, quantidade, datafabricacao, datavalidade) "
		               + "VALUES ('" + beyblade.getDescricao() + "', "
		               + beyblade.getPreco() + ", " + beyblade.getQuantidade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(beyblade.getDataFabricacao()));
			st.setDate(2, Date.valueOf(beyblade.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Beyblade get(int id) {
		Beyblade beyblade = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM beyblade WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 beyblade = new Beyblade(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getDate("datavalidade").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return beyblade;
	}
	
	
	public List<Beyblade> get() {
		return get("");
	}

	
	public List<Beyblade> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Beyblade> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Beyblade> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Beyblade> get(String orderBy) {
		List<Beyblade> beyblades = new ArrayList<Beyblade>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM beyblade" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Beyblade p = new Beyblade(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	        			                rs.getInt("quantidade"),
	        			                rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			                rs.getDate("datavalidade").toLocalDate());
	            beyblades.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return beyblades;
	}
	
	
	public boolean update(Beyblade beyblade) {
		boolean status = false;
		try {  
			String sql = "UPDATE beyblade SET descricao = '" + beyblade.getDescricao() + "', "
					   + "preco = " + beyblade.getPreco() + ", " 
					   + "quantidade = " + beyblade.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "datavalidade = ? WHERE id = " + beyblade.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(beyblade.getDataFabricacao()));
			st.setDate(2, Date.valueOf(beyblade.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM beyblade WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}