package cn.com.connext.msf.framework.file.excel;

import cn.com.connext.msf.framework.utils.ClassManager;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelExporter<T> {

    private final List<ExcelExportSetting<T>> settings = new ArrayList<>();


    public ExcelExporter(ExcelExportSetting<T> excelExportSetting) {
        settings.add(excelExportSetting);
    }

    public ExcelExporter(List<ExcelExportSetting<T>> excelExportSettingList) {
        settings.addAll(excelExportSettingList);
    }


    public HSSFWorkbook export() {
        prepare();
        HSSFWorkbook wb = new HSSFWorkbook();
        settings.forEach(setting -> {
            List<String> titles = setting.getTitles();
            List<String> fields = setting.getFields();
            List<T> data = setting.getData();

            HSSFSheet sheet = wb.createSheet(setting.getSheetName());
            sheet.setDefaultColumnWidth((short) (int) setting.getWidths().get(0));//设置表格宽度
            Row titleRow = sheet.createRow(0);

            HSSFCellStyle titleStyle = wb.createCellStyle();
            // 设置样式
            if (setting.getStyles() == null || setting.getStyles().size() < 1) {
                HSSFFont titleFont = wb.createFont(); // 创建字体样式
                titleFont.setBold(true); // 字体加粗
                titleFont.setFontName("黑体"); // 设置字体类型
                titleFont.setFontHeightInPoints((short) 10); // 设置字体大小
                titleStyle.setFont(titleFont); // 设置字体样式
            } else {
                titleStyle.cloneStyleFrom(setting.getStyles().get(0));
            }

            for (int colIndex = 0; colIndex < titles.size(); colIndex++) {
                Cell cell = titleRow.createCell(colIndex);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titles.get(colIndex));
            }

            HSSFCellStyle sheetStyle = wb.createCellStyle();
            // 设置样式
            if (setting.getStyles() == null || setting.getStyles().size() < 2) {
                HSSFFont sheetFont = wb.createFont(); // 创建字体样式
                sheetFont.setBold(false); // 字体加粗
                sheetFont.setFontName("黑体"); // 设置字体类型
                sheetFont.setFontHeightInPoints((short) 10); // 设置字体大小
                sheetStyle.setFont(sheetFont); // 设置字体样式
            } else {
                sheetStyle.cloneStyleFrom(setting.getStyles().get(1));
            }

            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row dataRow = sheet.createRow(rowIndex + 1);
                T item = data.get(rowIndex);
                for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                    Cell cell = dataRow.createCell(colIndex);
                    String fieldName = fields.get(colIndex);
                    String fieldValue = ClassManager.getFieldValue(item, fieldName).toString();
                    cell.setCellStyle(sheetStyle);
                    cell.setCellValue(fieldValue);
                }
            }
        });


        return wb;
    }

    private void prepare() {
        settings.forEach(setting -> {
            if (setting.getTitles() == null) throw new RuntimeException("Titles can not be null.");
            if (setting.getFields() == null) throw new RuntimeException("Fileds can not be null.");
            if (setting.getData() == null) throw new RuntimeException("Data can not be null.");

            if (setting.getTitles().size() != setting.getFields().size()) {
                throw new RuntimeException("Titles and fields length must be same.");
            }
        });
    }


}
