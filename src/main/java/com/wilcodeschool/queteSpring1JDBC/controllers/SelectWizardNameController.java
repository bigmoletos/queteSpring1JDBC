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
public class SelectWizardNameController {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
	private static final String DB_USER = "bigmoletos";
	private static final String DB_PASSWORD = "password";
// pour tester
//	http://localhost:8080/api/selected_wizards?family=Weasley

	@GetMapping("/api/selected_wizards")
	public List<Wizard> getSelectedWizards(@RequestParam(required = false, defaultValue = "%") String family) {
		// requete sur choix nom de famille
		String sql = "SELECT * FROM wizard WHERE lastname LIKE ?";
		// si jamais on ne renseigne pas le parametre family, on retourne toutes les
		// familles
		// on peut supprimer le if avec defaultValue = "%" dans les @RequestParam
//		if (family == null) {
//			family = "%";
//		}
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement statementName = connection.prepareStatement(sql);) {
			statementName.setString(1, family);

			try (ResultSet resultSet = statementName.executeQuery();) {
				List<Wizard> wizards = new ArrayList<Wizard>();

				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String firstname = resultSet.getString("firstname");
					String lastname = resultSet.getString("lastname");
					wizards.add(new Wizard(id, firstname, lastname));
				}

				return wizards;
			}
		} catch (SQLException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "", e);
		}

	}
}
