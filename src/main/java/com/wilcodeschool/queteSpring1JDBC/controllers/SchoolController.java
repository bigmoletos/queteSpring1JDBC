/**
 * 
 *
 */
package com.wilcodeschool.queteSpring1JDBC.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Controller
@ResponseBody
public class SchoolController {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
	private static final String DB_USER = "bigmoletos";
	private static final String DB_PASSWORD = "password";
// pour tester
//	http://localhost:8080/api/school?country=france

	@GetMapping("/api/school")
	public List<School> getSchool(@RequestParam(required = false, defaultValue = "%") String country) {
		String sql = "SELECT * FROM school WHERE country LIKE ?";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement statementName = connection.prepareStatement(sql);) {
			statementName.setString(1, country);

			try (ResultSet resultSet = statementName.executeQuery();) {
				List<School> school = new ArrayList<School>();

				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					String pays = resultSet.getString("country");
					int capacity = resultSet.getInt("capacity");
					school.add(new School(id, name, pays, capacity));
				}

				return school;
			}
		} catch (SQLException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "", e);
		}

	}
}
