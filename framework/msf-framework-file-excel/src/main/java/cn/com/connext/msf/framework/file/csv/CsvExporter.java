package cn.com.connext.msf.framework.file.csv;

import cn.com.connext.msf.framework.file.constant.FileExtension;
import cn.com.connext.msf.framework.file.domain.ObjectNodeManager;
import cn.com.connext.msf.framework.utils.ClassManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CsvExporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(CsvExporter.class);
    private final CsvExportSetting<T> setting;
    private File csvFile = null;


    public CsvExporter(CsvExportSetting<T> csvExportSetting) {
        this.setting = csvExportSetting;
    }

    public File export() {
        boolean append = false;
        prepare();
        if (setting.getOutPutPath() == null) throw new RuntimeException("OutPutPath can not be null.");

        List<String> titles = setting.getTitles();
        List<String> fields = setting.getFields();
        List<T> data = setting.getData();
        String outPutPath = setting.getOutPutPath();
        String filename = setting.getFileName();
        FileExtension fileExtension = setting.getFileExtension() == null ? FileExtension.csv : setting.getFileExtension();

        csvFile = null;
        BufferedWriter csvWriter = null;
        try {
            csvFile = new File(outPutPath + File.separator + filename + "." + fileExtension.toString());
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            append = csvFile.exists();

            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile, append), "GB2312"), 1024);
            // 写入文件头部
            if (!append && setting.isShowTitle()) {
                writeRow(titles, csvWriter);
            }
            // 写入文件内容
            writeData(data, fields, csvWriter);
            csvWriter.flush();
        } catch (Exception e) {
            logger.error("导出csv文件时发生错误：" + e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if (csvWriter != null) csvWriter.close();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        return csvFile;
    }

    public void export(HttpServletRequest request, HttpServletResponse response) {
        prepare();

        List<String> titles = setting.getTitles();
        List<String> fields = setting.getFields();
        List<T> data = setting.getData();
        FileExtension fileExtension = setting.getFileExtension() == null ? FileExtension.csv : setting.getFileExtension();
        String filename = setting.getFileName() + "." + fileExtension.toString();
        try {
            filename = buildFileName(filename, request);//解决文件名乱码
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "gbk");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            //osw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));//解决excel中展示乱码

            // 写入文件头部
            if (setting.isShowTitle()) {
                writeRow(titles, osw);
            }

            // 写入文件内容
            writeData(data, fields, osw);
            osw.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void prepare() {
        if (setting.getFileName() == null) throw new RuntimeException("FileName can not be null.");
        if (setting.getTitles() == null) throw new RuntimeException("Titles can not be null.");
        if (setting.getFields() == null) throw new RuntimeException("Fileds can not be null.");
        if (setting.getData() == null) throw new RuntimeException("Data can not be null.");

        if (setting.getTitles().size() != setting.getFields().size()) {
            throw new RuntimeException("Titles and fields length must be same.");
        }
    }

    private void writeRow(List<String> row, Writer csvWriter) throws IOException {
        String rowStr = "";
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            rowStr += sb.append(data).append(",").toString();
        }
        rowStr = rowStr.substring(0, rowStr.length() - 1);
        csvWriter.write(rowStr);
        csvWriter.write("\r\n");
    }

    private String buildFileName(String filename, HttpServletRequest request) throws Exception {
        //获得请求头中的User-Agent
        String agent = request.getHeader("User-Agent");
        //根据不同浏览器进行不同的编码
        String filenameEncoder = "";
        if (agent.contains("MSIE")) {
            // IE浏览器
            filenameEncoder = URLEncoder.encode(filename, "utf-8");
            filenameEncoder = filenameEncoder.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filenameEncoder = "=?utf-8?B?"
                    + Base64.getEncoder().encodeToString(filename.getBytes("utf-8")) + "?=";
        } else {
            // 其它浏览器
            filenameEncoder = URLEncoder.encode(filename, "utf-8");
        }
        return filenameEncoder;
    }

    private void writeData(List<T> data, List<String> fields, Writer csvWtriter) throws Exception {
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            List<String> row = new ArrayList<>();
            T item = data.get(rowIndex);
            for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                String fieldName = fields.get(colIndex);

                if (item instanceof ObjectNode) {
                    String fieldValue = ObjectNodeManager.getFieldValue(item, fieldName).toString();
                    row.add(fieldValue);
                    continue;
                }

                String fieldValue = ClassManager.getFieldValue(item, fieldName).toString();
                row.add(fieldValue);
            }
            writeRow(row, csvWtriter);
        }
    }
}
