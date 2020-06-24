package cn.com.connext.msf.framework.agg;

import com.google.common.collect.Lists;

import java.util.List;

public class AggregationTable {
    private String name;
    private List<String> header;
    private List<Row> rows;

    public AggregationTable() {
        name = "unnamed";
        header = Lists.newArrayList();
        rows = Lists.newArrayList();
    }

    public void appendHeaded(String header) {
        this.header.add(header);
    }

    public void appendRow(Row row) {
        this.rows.add(row);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public static class Row {
        private String name;
        private List<Double> values;

        public Row() {
            name = "unnamed";
            values = Lists.newArrayList();
        }

        public void appendValue(double value) {
            values.add(value);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Double> getValues() {
            return values;
        }

        public void setValues(List<Double> values) {
            this.values = values;
        }
    }
}
