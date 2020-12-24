/*
 * Copyright © 2019, Ashish Bailkeri. All rights reserved.
 *
 * 1. Redistribution in source and in binary forms is permitted
 * under the condition that credit is given to the creator of the software.
 *
 * 2. Any person(s) who have use this code must have this
 * license present in their code.
 *
 * 3. The name of the license holder may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package pson;
import pson.base.Type;
import pson.nodes.ErrorNode;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the tokenizer class that splits a string into all tokens.
 *
 * @author Ashish Bailkeri
 */
public class Tokenizer {

    /**
     * Represents all the information behind a token that needs
     * to be processed.
     * @author Ashish Bailkeri
     */
    private class TokenInfo {

        /** The regex representing the token value */
        private final Pattern regex;

        /** The type to be processed */
        public final Type type;

        /** Creates a new instance of token info with initializing types and regex */
        private TokenInfo(Pattern regex, Type type) {
            super();
            this.regex = regex;
            this.type = type;
        }

    }

    /**
     * Represents a single token that can be processed.
     * @author Ashish Bailkeri
     */
    public class Token {

        /** Immutable variable type of a token */
        private Type type;

        /** The string value of the type */
        private final String token;

        /** Creates a new token with {@link Type} type and the string value */
        private Token(Type type, String token) {
            super();
            this.type = type;
            this.token = token;
        }

        /** Retrieves the token */
        public String getToken() {
            return token;
        }

        /** Gets the token type */
        public Type getType() {
            return type;
        }

        /** Sets the token type */
        public void setType(Type t) {
            type = t;
        }
    }

    /** List holding all the information about the tokens */
    private LinkedList<TokenInfo> info;

    /** The list containing all the tokens in the string */
    private LinkedList<Token> tokens;

    /** The last token after one token has been processed */
    private Token lastToken;

    /** Initializes a tokenizer for lexing */
    public Tokenizer() {
        info = new LinkedList<>();
        tokens = new LinkedList<>();

        // Keywords
        add("module", Type.KEYWORD);
        add("package", Type.KEYWORD);
        add("export", Type.KEYWORD);
        add("array", Type.ARRAY);

        // Parsing tokens
        add("\\\\p", Type.TOKEN);
        add("\\(", Type.LPARETHESIS);
        add("\\)", Type.RPARENTHESIS);
        add("\\{", Type.LCURLY);
        add("}", Type.RCURLY);
        add("\\[", Type.LBRACKET);
        add("]", Type.RBRACKET);
        add(":", Type.COLON);
        add(",", Type.COMMA);
        add(";", Type.SEMI_COLON);

        // Values and statements
        add("true|false", Type.VALUE);
        add("^(-)?([.])?[0-9][0-9.]*", Type.NUMBER);
        add("^((-)?[0-9]+)", Type.INTEGER);
        add("[a-zA-Z][a-zA-Z0-9_]*", Type.STATEMENT);
        add("\\\"([^\\\"]*)\\\"", Type.STRING);
    }

    /** Add new regex to the token info */
    public void add(String regex, Type type) {
        info.add(new TokenInfo(Pattern.compile("^("+regex+")"), type));
    }

    /** Gets the last token */
    public Token getLastToken() {
        return lastToken;
    }
    /** Checks if there is another token */
    public boolean hasTheNextToken() {
        return tokens.descendingIterator().hasNext();
    }

    /** Gets the next token */
    public Token nextToken() {
        LinkedList<Token> tokens = getTokens();
        lastToken = tokens.getFirst();
        tokens.removeFirst();
        return lastToken;
    }

    /** Skips a certain number of tokens and returns the last in the sequence */
    public Token skip(int numTokens) {
        for (int i = 0; i < numTokens - 1; i++)
            nextToken();
        return nextToken();
    }

    /** The size of the tokens array */
    public final int length() {
        return getTokens().size();
    }

    /** Tokenize all the tokens */
    public void tokenize(String str) {
        String s = str.trim();
        tokens.clear();
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo info : info) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String token = m.group().trim();
                    s = m.replaceFirst("").trim();
                    tokens.add(new Token(info.type, token));
                    break;
                }
            }
            if (!match) ErrorNode.throwError("Unexpected character in input: "+ s);
        }
    }

    /** Gets all the tokens */
    private LinkedList<Token> getTokens() {
        return tokens;
    }
}
