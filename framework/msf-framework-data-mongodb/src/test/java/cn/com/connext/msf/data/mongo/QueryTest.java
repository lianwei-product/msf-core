package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.data.mongo.entity.User;
import cn.com.connext.msf.framework.utils.JSON;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class QueryTest {

    private static void checkLoop(List<String> fields) {
        String last = fields.get(fields.size() - 1);

        for (int i = fields.size() - 2; i >= 0; i--) {
            if (fields.get(i).equals(last)) {
                throw new RuntimeException("");
            }
        }
    }

    @Test
    public void testQuery() {
        Query query = QueryBuilder.build(User.class, "org.id eq 1");
        System.out.println(JSON.toJsonString(query));
    }

    @Test
    public void t() {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("age");
        checkLoop(fields);
    }
}
