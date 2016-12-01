package nfc.app.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectDatabase {
	private static String url;
	private static  String name;
	private static String pass;
	public static Stack<Connection> connPools;
	
	static{
		connPools=new Stack<Connection>();
		url="jdbc:mysql://172.16.0.199/82wafoodgo";
		name="duy";
		pass="kimduy";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		Connection connect=null;
		try {
			if(connPools.empty())
			{
				connect= (Connection) DriverManager.getConnection(url, name, pass);
				
			}
			else
			{
				connect=connPools.pop();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}
	
	public static void Disconnect(Connection conn)
	{
		connPools.push(conn);
	}
}
