package webService;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.Document;
import DataBase.MongoHandler;

/**
 * Servlet implementation class VisionInfo
 */
@WebServlet("/VisionInfo")
public class VisionInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VisionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String result = "";
		// 设置解码方式为utf-8
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String StartIndex = request.getParameter("StartIndex");
		String NumIndex = request.getParameter("NumIndex");
		String TargetID = request.getParameter("TargetID");
		String Host = request.getParameter("Host");
		if (TargetID != null) {
			// 返回详细页
			result = Getinfo(TargetID, Host);
		} else if (StartIndex != null && NumIndex != null) {
			// 返回列表页
			result = Getlist(StartIndex, NumIndex, Host);
		} else {
			// 返回错误
			result = "error";
		}
		response.getWriter().write(result);
	}

	private String Getlist(String StartIndex, String NumIndex, String Host) {
		MongoHandler mongoHandler = new MongoHandler();
		List<Document> document = mongoHandler.GetInfo(StartIndex, NumIndex);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < document.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			// 获取ID
			String _id = document.get(i).get("_id").toString();
			jsonObject.put("_id", _id);
			// 获取名字
			jsonObject.put("name", document.get(i).get("name"));
			// 获取类型
			jsonObject.put("genres", document.get(i).get("genres"));
			// 获取评分
			jsonObject.put("rating", document.get(i).get("rating"));
			// 获取图片
			jsonObject.put("pic", Host + "/api/pic/" + _id
					+ ".webp");
			jsonArray.add(jsonObject);
		}
		return jsonArray.toString();
	}

	private String Getinfo(String TargetID, String Host) {
		MongoHandler mongoHandler = new MongoHandler();
		List<Document> document = mongoHandler.GetInfo(TargetID);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < document.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			// 获取ID
			jsonObject.put("_id", document.get(i).get("_id"));
			// 获取名字
			jsonObject.put("name", document.get(i).get("name"));
			// 获取类型
			jsonObject.put("genres", document.get(i).get("genres"));
			// 获取评分
			jsonObject.put("rating", document.get(i).get("rating"));
			// 获取导演
			jsonObject.put("director", document.get(i).get("director"));
			// 获取演员
			jsonObject.put("actors", document.get(i).get("actors"));
			// 获取关键词
			jsonObject.put("keyword", document.get(i).get("keyword"));
			// 获取图片
			jsonObject.put("pic", Host + "/api/pic/"
					+ TargetID + ".webp");
			jsonArray.add(jsonObject);
		}
		return jsonArray.toString();
	}
}
