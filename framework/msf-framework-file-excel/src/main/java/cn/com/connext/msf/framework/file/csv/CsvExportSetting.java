package cn.com.connext.msf.framework.file.csv;

import cn.com.connext.msf.framework.file.constant.FileExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvExportSetting<T> {
    private List<String> titles;
    private List<String> fields;
    private List<T> data;
    private String fileName;
    private String outPutPath;
    private boolean showTitle;
    private FileExtension fileExtension;

    public CsvExportSetting() {
    }

    public static<T> CsvExportSetting<T> from(List<String> titles, List<String> fields, List<T> data, String fileName,String outPutPath,Boolean showTitle,FileExtension fileExtension){
        CsvExportSetting<T> csvExportSetting=new CsvExportSetting<>();
        csvExportSetting.setData(data);
        csvExportSetting.setFields(fields);
        csvExportSetting.setFileName(fileName);
        csvExportSetting.setTitles(titles);
        csvExportSetting.setShowTitle(showTitle);
        csvExportSetting.setFileExtension(fileExtension);
        csvExportSetting.setOutPutPath(outPutPath);

        return csvExportSetting;
    }

    public static<T> CsvExportSetting<T> from(String titles, String fields, List<T> data, String fileName,String outPutPath,Boolean showTitle,FileExtension fileExtension){
        CsvExportSetting<T> csvExportSetting=new CsvExportSetting<>();
        csvExportSetting.setData(data);
        csvExportSetting.setFields(fields);
        csvExportSetting.setFileName(fileName);
        csvExportSetting.setTitles(titles);
        csvExportSetting.setOutPutPath(outPutPath);
        csvExportSetting.setShowTitle(showTitle);
        csvExportSetting.setFileExtension(fileExtension);
        return csvExportSetting;
    }

    public List<String> getTitles() {
        return titles;
    }

    private void setTitles(List<String> titles) {
        this.titles = titles;
    }

    private void setTitles(String titles) {
        this.titles = new ArrayList<>();
        Arrays.asList(titles.split(",")).forEach(title -> {
            this.titles.add(title.trim());
        });
    }

    public List<String> getFields() {
        return fields;
    }

    private void setFields(List<String> fields) {
        this.fields = fields;
    }

    private void setFields(String fields) {
        this.fields = new ArrayList<>();
        Arrays.asList(fields.split(",")).forEach(field -> {
            this.fields.add(field.trim());
        });
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutPutPath() {
        return outPutPath;
    }

    private void setOutPutPath(String outPutPath) {
        this.outPutPath = outPutPath;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    private void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public FileExtension getFileExtension() {
        return fileExtension;
    }

    private void setFileExtension(FileExtension fileExtension) {
        this.fileExtension = fileExtension;
    }
}
