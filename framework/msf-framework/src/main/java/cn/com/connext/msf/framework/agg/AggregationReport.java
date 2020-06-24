package cn.com.connext.msf.framework.agg;

import com.google.common.collect.Lists;

import java.util.List;

public class AggregationReport {

    private List<AggregationTable> tables;

    public AggregationReport() {
        tables = Lists.newArrayList();
    }

    public List<AggregationTable> getTables() {
        return tables;
    }

    public void appendTable(AggregationTable aggregationTable) {
        this.tables.add(aggregationTable);
    }

}
