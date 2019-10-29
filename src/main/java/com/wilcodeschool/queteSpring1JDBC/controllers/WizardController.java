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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
@Controller
@ResponseBody
public class WizardController {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
	private static final String DB_USER = "bigmoletos";
	private static final String DB_PASSWORD = "password";

	@GetMapping("/api/wizards")
	public List<Wizard> getWizards() {
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM wizard");
				ResultSet resultSet = statement.executeQuery();) {
			List<Wizard> wizards = new ArrayList<Wizard>();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				wizards.add(new Wizard(id, firstname, lastname));
			}

			return wizards;
		} catch (SQLException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "", e);
		}
	}

}
