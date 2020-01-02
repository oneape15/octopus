import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TxtParseUtils {
    private static final Integer ONE = 1;

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        String filePath = "/Users/xiaodian/Downloads/abc.txt";

        /* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) { // 数据以逗号分隔
                String[] names = lineTxt.split(",");
                if (names.length != 25) {
                    System.out.println(lineTxt);
                    continue;
                }
                String key = names[2];
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                } else {
                    map.put(key, 1);
                }

            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }

        System.out.println("---------------------重复的key-----------------");
        int total = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                total++;
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }

        System.out.println("------>" + total);
    }
}
