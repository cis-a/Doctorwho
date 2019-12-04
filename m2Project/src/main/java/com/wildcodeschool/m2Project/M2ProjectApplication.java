package com.wildcodeschool.m2Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import java.sql.*;

@Controller
@SpringBootApplication
public class M2ProjectApplication {


	static Connection con;

	@RequestMapping("/")
	@ResponseBody

								public String index () throws Exception{
									//run an sql statement and save the result in rs
								Statement stat=con.createStatement();
								ResultSet rs=stat.executeQuery("select * from team");
								String output="<h1>List of teams</h1><ul>";
								while (rs.next()) {
									output +="<li><a href=team/"+rs.getInt(1)+">"+rs.getString(2)+"</a></li>";
								}
								output += "</ul>";
								return output;
			}

	@RequestMapping("/team/{team_id}")
	@ResponseBody

						public String hello (@PathVariable int team_id) throws Exception{
							Statement stat=con.createStatement();
							ResultSet rs=stat.executeQuery("select * from team where id = " + team_id);
							//if there is such a team, give the players
										if (rs.next()) {
											//start writing the HTML
											String output = "<h1>Members of team " +rs.getString(2) +"</h1><ul>";
											//iterate through list of players for team submitted  in pathvariable
											Statement stat2=con.createStatement();
											ResultSet rs2=stat2.executeQuery("select firstname, lastname from wizard join player on wizard.id=player.wizard_id where player.team_id = " + team_id + ";");
														while (rs2.next()) {
															output += "<li>" + rs2.getString("firstname") + " " + rs2.getString("lastname") + "</li>";
														}
										output += "</ul>";
										return output;
										}
										else {
											throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such team!");
											//return "No such team!";
										}
			}


		public static void main(String[] args) throws Exception{
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wild_db_quest", "root", "dB+25q3st");
			SpringApplication.run(M2ProjectApplication.class, args);
		}
}
