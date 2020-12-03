package cn.com.connext.msf.framework.mapping.model;

import cn.com.connext.msf.framework.exception.BusinessException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class AviatorModel {

    private List<AbstractFunction> functions;

    private String expression;

    private Map<String, Object> env;

    public AviatorModel() {
    }

    private AviatorModel(Builder builder) {
        this.functions = builder.functions;
        this.expression = builder.expression;
        this.env = builder.env;
    }

    public List<AbstractFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<AbstractFunction> functions) {
        this.functions = functions;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getEnv() {
        if (CollectionUtils.isEmpty(env))
            env = Maps.newHashMap();
        return env;
    }

    public void setEnv(Map<String, Object> env) {
        this.env = env;
    }

    public Object excute() {
        List<AbstractFunction> functions = this.getFunctions();
        if (!CollectionUtils.isEmpty(functions)) {
            functions.forEach(x -> AviatorEvaluator.addFunction(x));
        }
        return AviatorEvaluator.execute(this.getExpression(), this.getEnv());
    }


    public static class Builder {
        private List<AbstractFunction> functions;

        private String expression;

        private Map<String, Object> env;

        public Builder(String expression) {
            if (StringUtils.isEmpty(expression))
                throw new BusinessException("invalid_expression", " Aviator expression not allow empty!");
            this.expression = expression;
            functions = Lists.newArrayList();
            env = Maps.newHashMap();
        }

        public Builder setFunctions(List<AbstractFunction> functions) {
            this.functions = functions;
            return this;
        }

        public Builder addFunctions(AbstractFunction function) {
            this.functions.add(function);
            return this;
        }

        public Builder setEnv(Map<String, Object> env) {
            this.env = env;
            return this;
        }

        public AviatorModel build() {
            return new AviatorModel(this);
        }
    }
}
