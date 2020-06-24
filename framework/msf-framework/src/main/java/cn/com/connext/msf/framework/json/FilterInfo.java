package cn.com.connext.msf.framework.json;

import java.util.List;

public class FilterInfo {
    private List<String> and;
    private List<String> or;
    private List<String> not;

    public List<String> getAnd() {
        return and;
    }

    public void setAnd(List<String> and) {
        this.and = and;
    }

    public List<String> getOr() {
        return or;
    }

    public void setOr(List<String> or) {
        this.or = or;
    }

    public List<String> getNot() {
        return not;
    }

    public void setNot(List<String> not) {
        this.not = not;
    }
}
