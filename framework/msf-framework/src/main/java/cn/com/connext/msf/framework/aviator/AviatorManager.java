package cn.com.connext.msf.framework.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: ming.wang
 * @Date: 2020/10/27 0027 11:21
 * @Description: AviatorManager
 */
public class AviatorManager {
    Logger logger = LoggerFactory.getLogger(AviatorManager.class);

    public AviatorManager(List<AviatorImportFunction> functionList) {
        logger.info("init AviatorImportFunction!");
        if (!CollectionUtils.isEmpty(functionList)) {
            for (AviatorImportFunction function : functionList) {
                try {
                    logger.info("AviatorEvaluator.importFunctions import :{}", function.getClass().getSimpleName());
                    AviatorEvaluator.importFunctions(function.getClass());
                } catch (Exception e) {
                    logger.error("AviatorEvaluator.importFunctions import :{}, error:{}", function.getClass().getSimpleName(), e);
                }
            }
        }
    }
}

