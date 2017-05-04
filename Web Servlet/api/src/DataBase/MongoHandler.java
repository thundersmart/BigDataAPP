package DataBase;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoHandler {
	// 连接到 mongodb 服务
	private MongoClient mongoClient = new MongoClient("localhost", 27017);
	// 连接到数据库
	private MongoDatabase mongoDatabase = mongoClient.getDatabase("crwdata");

	// 取list列表
	public List<Document> GetInfo(String StartIndex, String NumIndex) {
		List<Document> document = new ArrayList<Document>();
		MongoCollection<Document> collection = mongoDatabase
				.getCollection("name_list");
		FindIterable<Document> findIterable = collection.find(Filters.gt("_id",
				Integer.parseInt(StartIndex)-1));
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			if (document.size() > Integer.parseInt(NumIndex) - 1)
				break;
			document.add(mongoCursor.next());
		}
		return document;
	}

	// 取详细内容
	public List<Document> GetInfo(String TargetID) {
		List<Document> document = new ArrayList<Document>();
		MongoCollection<Document> collection = mongoDatabase
				.getCollection("name_list");
		FindIterable<Document> findIterable = collection.find(Filters.eq("_id",
				Integer.parseInt(TargetID)));
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			document.add(mongoCursor.next());
		}
		return document;
	}

	// 测试
	public static void main(String[] args) {
		MongoHandler mongoHandler = new MongoHandler();
		List<Document> document = mongoHandler.GetInfo("1001");
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
			jsonArray.add(jsonObject);
		}
		System.out.println(jsonArray.toString());
	}
}
