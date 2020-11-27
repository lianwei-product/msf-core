package cn.com.connext.msf.framework.file.csv;

import cn.com.connext.msf.framework.file.constant.FileExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvExporterTest {

    @Test
    public void testExportFile() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new User("name" + i, "phone" + i));
        }
        CsvExportSetting setting = CsvExportSetting.from("姓名, 电话", "name, phone", userList, "测试csv文件_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"), "C:", true, FileExtension.csv);

        CsvExporter<User> excelExporter = new CsvExporter<>(setting);
        excelExporter.export();
    }

    @Test
    public void testExportFileAppend() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new User("name" + i, "phone" + i));
        }

        CsvExportSetting setting = CsvExportSetting.from("姓名, 电话", "name, phone", userList, "测试csv文件_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"), "C:", true, FileExtension.txt);

        CsvExporter<User> excelExporter = new CsvExporter<>(setting);
        excelExporter.export();

        List<User> userList2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList2.add(new User("name2" + i, "phone2" + i));
        }
        setting.setData(userList2);
        excelExporter.export();

    }

    @Test
    public void testExportFileObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode member = objectMapper.createObjectNode();
        member.put("id", "jd001");
        member.put("realName", "ChengHan");

        ObjectNode member2 = objectMapper.createObjectNode();
        member2.put("id", "jd002");
        member2.put("realName", "ChengHan2");

        List<ObjectNode> userList = new ArrayList<>();
        userList.add(member);
        userList.add(member2);

        CsvExportSetting setting = CsvExportSetting.from("姓名, 电话", "name, phone", userList, "测试csv文件_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"), "C:", true, FileExtension.txt);

        CsvExporter<ObjectNode> excelExporter = new CsvExporter<>(setting);
        excelExporter.export();

        ObjectNode member3 = objectMapper.createObjectNode();
        member3.put("id", "jd003");
        member3.put("realName", "ChengHan3");

        List<ObjectNode> userList2 = new ArrayList<>();
        userList2.add(member3);
        setting.setData(userList2);
        excelExporter.export();

    }

    public class User {
        private String name;
        private String phone;

        public User() {
        }

        public User(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public class tenant {
        private String name;
        private String code;

        public tenant(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
