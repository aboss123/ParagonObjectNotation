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

package me.ashboss360.pson.compiler;

import me.ashboss360.pson.base.Type;
import me.ashboss360.pson.nodes.ErrorNode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to parse and register all the data
 * inside the file so it can be used to get information
 * and use them.
 * @author Ashish Bailkeri
 */

public class Lexer {

    /** The data of tokens that it will register */
    private ArrayList<TokenData> data;

    /** The tokens to get after lexing */
    private String tokens;

    /** The last token to push */
    private Token lastToken;

    /** Whether to push to the next token or not */
    private boolean toPush;

    public Lexer(String tokens) {
        this.data = new ArrayList<>();
        this.tokens = tokens;

        data.add(new TokenData(Pattern.compile("([a-zA-Z][a-zA-Z0-9]*)"), Type.STATEMENT));
        data.add(new TokenData(Pattern.compile("^((-)?[0-9]+)"), Type.INTEGER));
        data.add(new TokenData(Pattern.compile("^(-)?([.])?[0-9][0-9.]*"), Type.DOUBLE));
        data.add(new TokenData(Pattern.compile("(true|false)"), Type.BOOLEAN));
        data.add(new TokenData(Pattern.compile("^(\".*\")"), Type.STRING));
        data.add(new TokenData(Pattern.compile("\\{"), Type.TOKEN));
        data.add(new TokenData(Pattern.compile("}"), Type.TOKEN));
        data.add(new TokenData(Pattern.compile("\\["), Type.TOKEN));
        data.add(new TokenData(Pattern.compile("]"), Type.TOKEN));
        data.add(new TokenData(Pattern.compile(";"), Type.TOKEN));
        data.add(new TokenData(Pattern.compile("->"), Type.TOKEN));

        for (String t : new String[] { "=", "\\(", "\\)", "\\.", "\\,", ":"}) {
            data.add(new TokenData(Pattern.compile("^(" + t + ")"), Type.TOKEN));
        }
    }


    /**
     * Gets the next token from the tokens that were given
     * to the Lexer.
     *
     * @return The token as a string
     */
    public String nextTokenValue() {
        tokens = tokens.trim();

        if(toPush) {
            toPush = false;
            return lastToken.getToken();
        }

        if(tokens.isEmpty())
            return "";

        for (TokenData data : data) {
            Matcher matcher = data.getPattern().matcher(tokens);

            if (matcher.find()) {
                String token = matcher.group().trim();
                tokens = matcher.replaceFirst("");

                if(data.getType() == Type.STRING) {
                    lastToken = new Token(token.substring(1, token.length() - 1), Type.STRING);
                    return lastToken.getToken();
                }

                else {
                    lastToken = new Token(token, data.getType());
                    return lastToken.getToken();
                }

            }
        }
        return lastToken.getToken();
    }

    public Token nextToken() {
        tokens = tokens.trim();

        if(toPush) {
            toPush = false;
            return lastToken;
        }

        if(tokens.isEmpty()) {
            return (lastToken = new Token("", Type.EMPTY));
        }


        for (TokenData data : data) {
            Matcher matcher = data.getPattern().matcher(tokens);

            if (matcher.find()) {
                String token = matcher.group().trim();
                tokens = matcher.replaceFirst("");

                if(data.getType() == Type.STRING) {
                    return (lastToken = new Token(token.substring(1, token.length() - 1), Type.STRING));
                }

                else {
                    return (lastToken = new Token(token, data.getType()));
                }

            }
        }
        ErrorNode.throwError("The characters could not be parsed: " + "'" + tokens + "'");
        return null;
    }

    /** Checks whether there is another token when lexing */
    public boolean hasTheNextToken() {
        return !tokens.isEmpty();
    }

    /** Gets the data of the tokens */
    public ArrayList<TokenData> getData() {
        return data;
    }
}
