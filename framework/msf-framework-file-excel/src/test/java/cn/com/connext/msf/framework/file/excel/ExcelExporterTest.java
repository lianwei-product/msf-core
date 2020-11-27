package cn.com.connext.msf.framework.file.excel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelExporterTest {

    @Test
    public void testExport() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            userList.add(new User("name" + i, "phone" + i));
        }

        ExcelExportSetting<User> setting = new ExcelExportSetting<>();
        setting.setTitles("姓名, 电话");
        setting.setFields("name, phone");
        setting.setData(userList);

        ExcelExporter<User> excelExporter = new ExcelExporter<>(setting);
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
}