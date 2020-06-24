/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.com.connext.msf.framework.query;

import java.util.ArrayList;
import java.util.List;

class QueryExpressionTokenParser {

    List<QueryExpressionToken> parse(String queryString) {

        char[] chars = queryString.trim().toCharArray();

        State state = new ExpressionStateExpression();
        List<QueryExpressionToken> states = new ArrayList<QueryExpressionToken>();
        for (char aChar : chars) {
            State next = state.nextChar(aChar);
            if (state.isFinished()) {
                states.add(state);
            }
            state = next;
        }

        if (state.close().isFinished()) {
            states.add(state);
        } else {
            throw new RuntimeException("Last parsed state '" + state.toString() + "' is not finished.");
        }

        return states;
    }

    private static abstract class State implements QueryExpressionToken {
        protected static final char CHAR_N = 'N';
        protected static final char CHAR_O = 'O';
        protected static final char CHAR_A = 'A';
        protected static final char CHAR_D = 'D';
        protected static final char CHAR_R = 'R';
        protected static final char CHAR_CLOSE = ')';
        protected static final char CHAR_OPEN = '(';
        private Token token = null;
        private boolean finished = false;

        public State() {
        }

        public State(final Token t) {
            token = t;
        }

        public State(final Token t, final boolean finished) {
            this(t);
            this.finished = finished;
        }

        static boolean isAllowedWord(final char character) {
            return character != ' ' && character != ')' && character != '(';
        }

        static boolean isWhitespace(final char character) {
            return character == ' ' || character == '\t';
        }

        protected abstract State nextChar(char c);

        /**
         * @param c allowed character
         */
        public State allowed(final char c) {
            return this;
        }

        public State forbidden(final char c) {
            throw new RuntimeException("Forbidden character in state " + token + "->" + c);
        }

        public State invalid() {
            throw new RuntimeException("Token " + token + " is in invalid state.");
        }

        public State finish() {
            finished = true;
            return this;
        }

        public State finishAs(final Token token) {
            finished = true;
            return changeToken(token);
        }

        public boolean isFinished() {
            return finished;
        }

        @Override
        public Token getToken() {
            return token;
        }

        public String getTokenName() {
            if (token == null) {
                return "NULL";
            }
            return token.name();
        }

        public State close() {
            return this;
        }

        protected State changeToken(final Token token) {
            this.token = token;
            return this;
        }

        @Override
        public String getLiteral() {
            return token.toString();
        }

        @Override
        public String toString() {
            return token + "=>{" + getLiteral() + "}";
        }
    }

    private static abstract class LiteralState extends State {
        protected final StringBuilder literal = new StringBuilder();

        public LiteralState() {
            super();
        }

        public LiteralState(final Token t, final char c) {
            super(t);
            init(c);
        }

        public LiteralState(final Token t, final String initLiteral) {
            super(t);
            literal.append(initLiteral);
        }

        @Override
        public State allowed(final char c) {
            literal.append(c);
            return this;
        }

        @Override
        public String getLiteral() {
            return literal.toString();
        }

        public State init(final char c) {
            if (isFinished()) {
                throw new RuntimeException(toString() + " is already finished.");
            }
            literal.append(c);
            return this;
        }
    }

    private class ExpressionStateExpression extends LiteralState {
        @Override
        public State nextChar(final char c) {
            if (c == CHAR_OPEN) {
                return new OpenState();
            } else if (isWhitespace(c)) {
                return new RwsState();
            } else if (c == CHAR_CLOSE) {
                return new CloseState();
            } else {
                return new TermStateExpression().init(c);
            }
        }

        @Override
        public State init(final char c) {
            return nextChar(c);
        }
    }

    private class TermStateExpression extends LiteralState {
        @Override
        public State nextChar(final char c) {
            if (isAllowedWord(c)) {
                return new WordStateExpression(c);
            }
            return forbidden(c);
        }

        @Override
        public State init(final char c) {
            return nextChar(c);
        }
    }

    private class WordStateExpression extends LiteralState {
        public WordStateExpression(final char c) {
            super(Token.WORD, c);
            if (!isAllowedWord(c)) {
                forbidden(c);
            }
        }

        public WordStateExpression(final State toConsume) {
            super(Token.WORD, toConsume.getLiteral());
            for (int i = 0; i < literal.length(); i++) {
                if (!isAllowedWord(literal.charAt(i))) {
                    forbidden(literal.charAt(i));
                }
            }
        }

        @Override
        public State nextChar(final char c) {
            if (isAllowedWord(c)) {
                return allowed(c);
            } else if (c == CHAR_CLOSE) {
                finish();
                return new CloseState();
            } else if (isWhitespace(c)) {
                finish();
                return new RwsState();
            }
            return forbidden(c);
        }

        @Override
        public State finish() {
            String tmpLiteral = literal.toString();
            if (tmpLiteral.length() == 3) {
                if (Token.AND.name().equals(tmpLiteral)) {
                    return finishAs(Token.AND);
                }
            } else if (tmpLiteral.length() == 2 && Token.OR.name().equals(tmpLiteral)) {
                return finishAs(Token.OR);
            }
            return super.finish();
        }

        @Override
        public State close() {
            return finish();
        }
    }

    private class OpenState extends State {
        public OpenState() {
            super(Token.OPEN, true);
        }

        @Override
        public State nextChar(final char c) {
            finish();
            if (isWhitespace(c)) {
                return forbidden(c);
            }
            return new ExpressionStateExpression().init(c);
        }
    }

    private class CloseState extends State {
        public CloseState() {
            super(Token.CLOSE, true);
        }

        @Override
        public State nextChar(final char c) {
            return new ExpressionStateExpression().init(c);
        }
    }

    private class AndState extends LiteralState {
        public AndState(final char c) {
            super(Token.AND, c);
            if (c != CHAR_A) {
                forbidden(c);
            }
        }

        @Override
        public State nextChar(final char c) {
            if (literal.length() == 1 && c == CHAR_N) {
                return allowed(c);
            } else if (literal.length() == 2 && c == CHAR_D) {
                return allowed(c);
            } else if (literal.length() == 3 && isWhitespace(c)) {
                finish();
                return new BeforeExpressionRwsStateExpression();
            } else if (isWhitespace(c)) {
                changeToken(Token.WORD).finish();
                return new RwsState();
            }
            literal.append(c);
            return new WordStateExpression(this);
        }

        @Override
        public State close() {
            if (Token.AND.name().equals(literal.toString())) {
                return finish();
            }
            return changeToken(Token.WORD).finish();
        }
    }

    private class OrState extends LiteralState {
        public OrState(final char c) {
            super(Token.OR, c);
            if (c != CHAR_O) {
                forbidden(c);
            }
        }

        @Override
        public State nextChar(final char c) {
            if (literal.length() == 1 && (c == CHAR_R)) {
                return allowed(c);
            } else if (literal.length() == 2 && isWhitespace(c)) {
                finish();
                return new BeforeExpressionRwsStateExpression();
            } else if (isWhitespace(c)) {
                changeToken(Token.WORD).finish();
                return new RwsState();
            }
            literal.append(c);
            return new WordStateExpression(this);
        }

        @Override
        public State close() {
            if (Token.OR.name().equals(literal.toString())) {
                return finish();
            }
            return changeToken(Token.WORD).finish();
        }
    }

    private class BeforeExpressionRwsStateExpression extends State {
        @Override
        public State nextChar(final char c) {
            if (isWhitespace(c)) {
                return allowed(c);
            } else {
                return new ExpressionStateExpression().init(c);
            }
        }
    }

    private class RwsState extends State {
        @Override
        public State nextChar(final char c) {
            if (isWhitespace(c)) {
                return allowed(c);
            } else if (c == CHAR_O) {
                return new OrState(c);
            } else if (c == CHAR_A) {
                return new AndState(c);
            } else {
                return new ExpressionStateExpression().init(c);
            }
        }
    }


}
