import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Handler
{
  public static String chatroom_json(String obj)
  {
    Schema.SObjectType target = Schema.getGlobalDescribe().get(obj);
    Schema.DescribeSObjectResult fieldType = target.getDescribe();
    Map<String, Schema.SObjectType> fields = fieldType.fields.getMap();

    List<Object> cats = new List<Object>();
    for (String key : fields.keySet())
    {
      Schema.DescribeFieldResult dealio = fields.get(key).getDescribe();
      Map<String, Object> size = new Map<String, Object>();

      size.put('type', dealio.getType().name());
      size.put('username', dealio.getName());
      size.put('len', )
    }
  }

}
