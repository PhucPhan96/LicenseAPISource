package nfc.app.order.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nfc.app.common.Utils;
import nfc.app.order.Order;
import nfc.app.order.OrderDetail;

public class TransformJsonToOrder {
	public Order tranJsonToOrder(String data)
	{
		//Order order = new Order();
		JSONObject orderJsonObj = Utils.convertStringToJsonObject(data);
		Order order = (Order) Utils.convertJsonObjectToClass(orderJsonObj, "nfc.app.order.Order", new String[]{"orderDetails"});
		if(orderJsonObj.containsKey("orderDetails"))
		{
			List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
			JSONArray arrOrderDetail = (JSONArray) orderJsonObj.get("orderDetails");
			for(int i=0;i<arrOrderDetail.size();i++)
			{
				JSONObject jsonOrderDetail = (JSONObject) arrOrderDetail.get(i);
				OrderDetail orderDetail = (OrderDetail) Utils.convertJsonObjectToClass(jsonOrderDetail, "nfc.app.order.OrderDetail", new String[]{});
				lstOrderDetail.add(orderDetail);
			}
			order.setOrderDetails(arrOrderDetail);	
		}
		return order;
	}
	public String tranOrderToJson(Order order)
	{
		return "aaaaaa";
	}
}
