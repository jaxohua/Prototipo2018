package com.TanQuery;


import java.sql.*;

public class ConectorBD {
    public Connection con;
	private Statement st;
	private ResultSet rs;

	public ConectorBD(String server,String pto, String dbname, String user,String pass){
		try {
			//you should update it to com.mysql.jdbc.Driver as soon as possible.
			String myDriver = "org.gjt.mm.mysql.Driver";

			String servidor="jdbc:mysql://" + server + ":" + pto + "/" + dbname;
			Class.forName(myDriver);
			try {
				con= DriverManager.getConnection(servidor,user,pass);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Error clase DBConect " + e);
		}

	}

	public Statement getSt() {
		return st;
	}

	public void setSt(Statement st) {
		this.st = st;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}


}
