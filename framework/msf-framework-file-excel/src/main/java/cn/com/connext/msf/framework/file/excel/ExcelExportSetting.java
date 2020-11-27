package cn.com.connext.msf.framework.file.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelExportSetting<T> {
    private String sheetName = "Sheet1";
    private List<String> titles;
    private List<String> fields;
    private List<Integer> widths = Arrays.asList(20);
    private List<T> data;
    private List<HSSFCellStyle> styles;

    public ExcelExportSetting() {
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setTitles(String titles) {
        this.titles = new ArrayList<>();
        Arrays.asList(titles.split(",")).forEach(title -> {
            this.titles.add(title.trim());
        });
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setFields(String fields) {
        this.fields = new ArrayList<>();
        Arrays.asList(fields.split(",")).forEach(field -> {
            this.fields.add(field.trim());
        });
    }

    public List<Integer> getWidths() {
        return widths;
    }

    public void setWidths(List<Integer> widths) {
        this.widths = widths;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<HSSFCellStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<HSSFCellStyle> styles) {
        this.styles = styles;
    }
}
