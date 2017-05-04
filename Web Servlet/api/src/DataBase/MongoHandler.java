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
	// ���ӵ� mongodb ����
	private MongoClient mongoClient = new MongoClient("localhost", 27017);
	// ���ӵ����ݿ�
	private MongoDatabase mongoDatabase = mongoClient.getDatabase("crwdata");

	// ȡlist�б�
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

	// ȡ��ϸ����
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

	// ����
	public static void main(String[] args) {
		MongoHandler mongoHandler = new MongoHandler();
		List<Document> document = mongoHandler.GetInfo("1001");
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < document.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			// ��ȡID
			jsonObject.put("_id", document.get(i).get("_id"));
			// ��ȡ����
			jsonObject.put("name", document.get(i).get("name"));
			// ��ȡ����
			jsonObject.put("genres", document.get(i).get("genres"));
			// ��ȡ����
			jsonObject.put("rating", document.get(i).get("rating"));
			// ��ȡ����
			jsonObject.put("director", document.get(i).get("director"));
			// ��ȡ��Ա
			jsonObject.put("actors", document.get(i).get("actors"));
			// ��ȡ�ؼ���
			jsonObject.put("keyword", document.get(i).get("keyword"));
			jsonArray.add(jsonObject);
		}
		System.out.println(jsonArray.toString());
	}
}
