package cn.com.connext.msf.framework.file.txt;

import cn.com.connext.msf.framework.file.constant.FileExtension;
import cn.com.connext.msf.framework.file.domain.ObjectNodeManager;
import cn.com.connext.msf.framework.utils.ClassManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtExporter<T> {
    private static final Logger logger = LoggerFactory.getLogger(TxtExporter.class);

    private final TxtExportSetting<T> setting;
    private File txtFile = null;

    public TxtExporter(TxtExportSetting<T> setting) {
        this.setting = setting;
    }

    public File export() {
        boolean append = false;
        if (setting.getOutPutPath() == null) throw new RuntimeException("OutPutPath can not be null.");

        List<T> data = setting.getData();
        String outPutPath = setting.getOutPutPath();
        String filename = setting.getFileName();
        FileExtension fileExtension = setting.getFileExtension() == null ? FileExtension.txt : setting.getFileExtension();

        txtFile = null;
        BufferedWriter csvWriter = null;
        try {
            txtFile = new File(outPutPath + File.separator + filename + "." + fileExtension.toString());
            File parent = txtFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            append = txtFile.exists();

            txtFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    txtFile, append), "GB2312"), 1024);
            // 写入文件内容
            writeRow(data, csvWriter);
            csvWriter.flush();
        } catch (Exception e) {
            logger.error("导出txt文件时发生错误：" + e.toString());
            throw new RuntimeException(e);
        } finally {
            try {
                if (csvWriter != null) csvWriter.close();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        return txtFile;
    }

    private void writeRow(List<T> row, Writer csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            csvWriter.write(data.toString());
            csvWriter.write("\r\n");
        }
    }
}
