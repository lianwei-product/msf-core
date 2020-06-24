package cn.com.connext.msf.framework.query;


import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class QueryExpression {
    public String expression;
    public QueryRelationType queryRelationType;
    public List<QueryExpression> nodes;

    QueryExpression() {
        nodes = new ArrayList<>();
        queryRelationType = QueryRelationType.AND;
    }

    public static List<QueryExpression> parse(String queryString) {
        try {
            if (StringUtils.isEmpty(queryString)) {
                return new ArrayList<>();
            }
            List<QueryExpression> expressions = parseExpression(queryString);
            checkIsSameRelationType(expressions);
            return expressions;
        } catch (Exception ex) {
            throw new RuntimeException(MessageFormat.format("Invalid query expression, QueryString: {0}, reason: {1}", queryString, ex.getMessage()));
        }
    }

    private static List<QueryExpression> parseExpression(String queryString) {
        List<QueryExpression> root = new ArrayList<>();

        List<QueryExpressionToken> queryExpressionTokens = new QueryExpressionTokenParser().parse(queryString);
        List<String> tokenList = convert(queryExpressionTokens);
        List<List<QueryExpression>> parentsNodes = new ArrayList<>();
        List<QueryExpression> currentNodes = root;

        for (String token : tokenList) {
            switch (token) {
                case "OPEN":
                    QueryExpression openNode = new QueryExpression();
                    currentNodes.add(openNode);
                    parentsNodes.add(currentNodes);
                    currentNodes = openNode.nodes;
                    break;

                case "CLOSE":
                    if (parentsNodes.size() > 0) {
                        currentNodes = parentsNodes.get(parentsNodes.size() - 1);
                        parentsNodes.remove(parentsNodes.size() - 1);
                    } else {
                        throw new RuntimeException("Error closing bracket.");
                    }

                    break;

                case "AND":
                    if (currentNodes.size() > 0) {
                        currentNodes.get(currentNodes.size() - 1).queryRelationType = QueryRelationType.AND;
                    } else {
                        throw new RuntimeException("Error AND relation.");
                    }
                    break;

                case "OR":
                    if (currentNodes.size() > 0) {
                        currentNodes.get(currentNodes.size() - 1).queryRelationType = QueryRelationType.OR;
                    } else {
                        throw new RuntimeException("Error OR relation.");
                    }
                    break;

                default:
                    QueryExpression stringNode = new QueryExpression();
                    stringNode.expression = token;
                    currentNodes.add(stringNode);
                    break;
            }
        }

        if (parentsNodes.size() != 0) {
            throw new RuntimeException("Error closing bracket.");
        }

        return root;
    }

    private static List<String> convert(List<QueryExpressionToken> source) {
        List<String> list = new ArrayList<>();
        boolean lastTokenIsWord = false;
        for (QueryExpressionToken token : source) {
            if (!token.getToken().equals(QueryExpressionToken.Token.WORD)) {
                list.add(token.getLiteral());
                lastTokenIsWord = false;
            } else {
                if (lastTokenIsWord) {
                    list.set(list.size() - 1, list.get(list.size() - 1) + " " + token.getLiteral());
                } else {
                    list.add(token.getLiteral());
                    lastTokenIsWord = true;
                }
            }
        }
        return list;
    }

    private static void checkIsSameRelationType(List<QueryExpression> expressions) {
        String relation = null;
        for (int i = 0; i < expressions.size(); i++) {
            QueryExpression expression = expressions.get(i);
            if (expression.nodes.size() > 0) {
                checkIsSameRelationType(expression.nodes);
            }

            String tmpRelation = expression.queryRelationType.name();
            if (relation == null) {
                relation = tmpRelation;
            } else {
                if (i < expressions.size() - 1 && !relation.equals(tmpRelation)) {
                    throw new RuntimeException("Relation(AND/OR) must be same in single expression node.");
                }
            }
        }
    }
}
