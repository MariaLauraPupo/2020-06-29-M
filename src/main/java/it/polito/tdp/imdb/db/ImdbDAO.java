package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public /*List<Director>*/ void listAllDirectors(Map<Integer,Director> map){
		String sql = "SELECT * FROM directors";
//		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
               if(!map.containsKey(res.getInt("id"))) {
     		   Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
               map.put(director.getId(), director);
               }
//				result.add(director);
			}
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
//			return null;
		}
	}
	
	public List<Director> getDirectorByAnno(int anno, Map<Integer, Director> map) {
		String sql = "SELECT d.id AS id, d.first_name AS nome, d.last_name AS cognome, COUNT(*) AS conteggio "
				+ "FROM directors d, movies m, movies_directors AS md "
				+ "WHERE d.id = md.director_id AND m.id = md.movie_id AND m.year = ? "
				+ "GROUP BY  d.id , d.first_name , d.last_name";
		List<Director> result = new LinkedList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				

			result.add(map.get(res.getInt("id")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(int anno, Map<Integer,Director> map){
		String sql ="SELECT md1.director_id AS id1, md2.director_id AS id2, COUNT(*) AS peso "
				+ "FROM movies_directors md1, movies_directors md2, movies m1, movies m2, roles r1, roles r2 "
				+ "WHERE md1.director_id <> md2.director_id AND md1.movie_id = m1.id AND md2.movie_id = m2.id AND m1.id = r1.movie_id AND m2.id = r2.movie_id AND r1.actor_id = r2.actor_id AND m1.year = m2.year AND m1.year = ? "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Arco> result = new LinkedList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

			Director d1 = map.get(res.getInt("id1"));
			Director d2 = map.get(res.getInt("id2"));
			if(d1 != null && d2 != null) {
			Arco arco = new Arco (d1,d2,res.getInt("peso"));
			result.add(arco);
			}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
