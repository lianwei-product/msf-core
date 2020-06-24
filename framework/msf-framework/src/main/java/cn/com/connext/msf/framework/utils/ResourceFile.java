package cn.com.connext.msf.framework.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceFile {

    // just a utils
    private static Logger logger = LoggerFactory.getLogger(ResourceFile.class);

    /**
     * 获取资源文件夹下指定路径下的所有文件名称。
     *
     * @param path 目标路径，如"json"
     * @return 返回文件，如["json/a.json","json/b.json"]
     */
    public static List<String> loadFileNames(String path) {
        List<String> files = Lists.newArrayList();
        try {


            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            // must ext equals json?
            
            Resource[] resources = resolver.getResources("classpath*:" + path + "/*.json");
            for (Resource resource : resources) {
                // resource.getFilename() maybe null.
                files.add(path.concat("/").concat(resource.getFilename()));
            }

            // this log is not necessary.
            logger.info("Config folder path: {}, child files: {}", JSON.toJsonString(path), JSON.toJsonString(files));
            return files;
        } catch (IOException e) {
            logger.error("Load file path error.", e);
            return files;
        }
    }

    /**
     * 获取资源文件夹下指定文本文件的正文。
     *
     * @param file 指定文本文件
     */
    public static String loadFile(String file) throws IOException {
        ClassPathResource resource = new ClassPathResource(file);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
