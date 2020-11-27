package cn.com.connext.msf.framework.file.txt;

import cn.com.connext.msf.framework.file.constant.FileExtension;

import java.util.List;

public class TxtExportSetting<T> {
    private List<T> data;
    private String fileName;
    private String outPutPath;
    private FileExtension fileExtension;

    public TxtExportSetting() {
    }

    public static <T> TxtExportSetting<T> from(List<T> data, String fileName, String outPutPath, FileExtension fileExtension) {
        TxtExportSetting<T> txtExportSetting = new TxtExportSetting<>();
        txtExportSetting.setData(data);
        txtExportSetting.setFileName(fileName);
        txtExportSetting.setFileExtension(fileExtension);
        txtExportSetting.setOutPutPath(outPutPath);

        return txtExportSetting;
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


    public FileExtension getFileExtension() {
        return fileExtension;
    }

    private void setFileExtension(FileExtension fileExtension) {
        this.fileExtension = fileExtension;
    }
}
