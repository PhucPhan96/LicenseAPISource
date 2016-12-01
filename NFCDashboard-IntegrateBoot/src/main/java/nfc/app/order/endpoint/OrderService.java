package nfc.app.order.endpoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import nfc.app.common.Utils;
import nfc.app.dal.ConnectDatabase;
import nfc.app.order.Order;
import nfc.app.order.OrderDetail;

public class OrderService {
	public Order saveOrder(Order order)
	{
		Connection connection = ConnectDatabase.getConnection();
		Statement state = null;
		try {
			state=(Statement) connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			connection.setAutoCommit(false);
			
			String sql = Utils.ConvertObjectToInsertSQL(order, "gs_orders");
			System.out.println("SQL " + sql);
			state.executeUpdate(sql);
			for (OrderDetail orderDetail: order.getOrderDetails()) {
				sql = Utils.ConvertObjectToInsertSQL(orderDetail, "gs_details");
				System.out.println("SQL " + sql);
				state.executeUpdate(sql);
			}
			
			connection.commit();
			state.close();
			connection.close();
		} catch (SQLException e) {
			try {
				if(connection!=null)
					connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally{
			try {
				if(state!=null)
					state.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(connection!=null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("SupplID " + order.getSuppl_id());
		System.out.println("Detail " + order.getOrderDetails().size());
		
		return order;
	}
}
