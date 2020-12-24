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

package pson.arrays;

import pson.PSONContainer;
import pson.Tokenizer;
import pson.base.PSONObject;
import pson.base.Type;
import pson.nodes.ErrorNode;
import pson.nodes.UtilNode;
import pson.nodes.ValueNode;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class PSONArray extends ArrayList {

    private static final int START = 0;
    private static final int IN_VALUE = 1;
    private static final int IN_OBJECT = 2;
    private static final int IN_ARRAY = 3;
    private static final int IN_ERROR = 4;
    private static final int PAIR_KEY = 5;
    private static final int IN_PAIR_VALUE = 6;
    private static int nested = -1;
    private static final int END = 7;

    private static final ArrayList<String> nestedNames = new ArrayList<>();

    private String line;
    private Tokenizer tokenizer = new Tokenizer();
    private Tokenizer.Token token = null;
    private ArrayList<Map<String, Object>> array;
    private int searchStatus = START;

    /** Resets the status */
    private int peek(LinkedList statusStack) {
        if(statusStack.size() == 0)
            return -1;
        return (Integer) statusStack.getFirst();
    }

    public PSONArray(String line) {
        super();
        this.line = line;
    }

    private PSONArray() {
        super();
    }

    @SuppressWarnings("unchecked")
    private PSONArray(ArrayList<Map<String, Object>> array) {
        this.array = array;
    }

    /** Arrays with multiple properties within them may contain, the nested values after the original array*/
    @SuppressWarnings("unchecked")
    public PSONArray(String line, PSONContainer pcon) {
        this.line = line;
        try {
            this.array = (ArrayList) parse(pcon);
            if(!line.contains("(") && !line.contains(")")) {
                List array = (List) parse(pcon);
                List array2 = ValueNode.toArray(array.toString());
                for(Object a : array2) this.add(a);
            }
            else this.add(array);
        } catch (Exception e) {
            System.out.println("DEBUG");
        }
    }

    /** Keep in mind, if the array is a default one, all methods for getting
     * nested arrays will not work and will result in an error
     * @return The element array
     * */
    public ArrayList<Map<String, Object>> getArray() {
        return array;
    }

    public String getLine() {
        return line;
    }

    @SuppressWarnings("unchecked, unused")
    private List asArray(String line) {
        line = UtilNode.replaceAll(line, ",", "[", "]");
        List array = new ArrayList<>();
        for(String a : line.split(" ")) {
            a = a.trim();
            array.add(ValueNode.convertValue(a));
        }
        if(array.get(0).toString().isEmpty())
            array.remove(0);
        return array;
    }

    /** Gets a default nested array ie: {1, 2, 3, 4} */
    @SuppressWarnings("unused")
    public List nestedArray(String line) {
        return asArray(this.get(line).toString());
    }

    /** Converts a string value to a map ie: {oops=yokes} */
    private Map<String, Object> toMap(String line) {
        line = UtilNode.replaceAll(line, ",", "[", "]");
        String key = line.substring(0, line.indexOf("="));
        Object val = line.substring(line.indexOf("=") + 1);
        Map<String, Object> array = new LinkedHashMap<>();
        val = ValueNode.convertValue(val.toString());

        array.put(key, val);
        return array;
    }

    /** Creates an array from the entry point {@code begin}, and then creates from the index of start and end */
    public ArrayList<Object> from(String begin, int start, int end) {
        return ValueNode.toArray(get(begin).toString(), start, end);
    }

    /** If the index contains nested arrays, it will not map the value correctly and
     * will partly merge it. This only works if it is an individual set of values.*/
    public Map<String, Object> map(String start, int index) {
        return toMap(ValueNode.toArray(get(start).toString()).get(index).toString());
    }

    /** Writes an array based off of a {@link java.util.List} */
    private static void writePSONString(List list, Writer out) throws IOException {
        if(list == null)
            out.write("null");
        else if(list.size() == 0)
            out.write("{}");
        else {
            out.write("{");
            out.write(list.get(0).toString());

            for (int i = 1; i < list.size(); i++) {
                out.write(",");
                out.write(String.valueOf(list.get(i)));
            }
            out.write("}");
        }
    }

    /** Converts a {@link List} to a formatted String */
    public static String toPSONString(List list) {
        final StringWriter writer = new StringWriter();
        try {
            writePSONString(list, writer);
            return writer.toString();
        } catch (IOException e) {
            ErrorNode.throwError("Unexpected Error Converting!");
        }
        return null;
    }

    /** Gets the next token in the tokenizer */
    private void nextToken() {
        token = tokenizer.nextToken();
        try {
            if (!tokenizer.hasTheNextToken())
                token.setType(Type.NULL);
        } catch (Exception e) {
            ErrorNode.throwError("Null Pointer Exception!");
        }
    }

    /** Parses default arrays */
    @SuppressWarnings("unchecked")
    public List parse() {
        List defaultArray = new ArrayList();
        tokenizer.tokenize(line);
        try {
            do {
                nextToken();
                System.out.println(token.getToken());
                switch (token.getType()) {
                    case STATEMENT:
                        break;
                    case COLON:
                        break;
                    case COMMA:
                        break;
                    case NUMBER:
                        Object val = ValueNode.convertValue(token.getToken());
                        defaultArray.add(val);
                        break;
                    case VALUE:
                        val = ValueNode.convertValue(token.getToken());
                        defaultArray.add(val);
                        break;
                    case STRING:
                        String v = token.getToken().replace("\"", "");
                        val = ValueNode.convertValue(v);
                        defaultArray.add(val);
                        break;
                    case SEMI_COLON:
                        System.out.println("Array Finished");
                        return defaultArray;
                }
                System.out.println(defaultArray);
            } while(tokenizer.hasTheNextToken());

            if(token.getToken().equals(";"))
                return defaultArray;
            else
                ErrorNode.throwError("Unexpected end of file parsing with token -> '" + token.getToken() + "'");

        } catch (Exception e) {
            ErrorNode.throwError("Unexpected parsing error with token -> '" + token.getToken() + "'");
        }
        return null;
    }

    public Object parseNew(PSONContainer pcon) {
        LinkedList statusStack = new LinkedList();
        LinkedList valStack = new LinkedList();
        ArrayList<Map<String, Object>> map = new ArrayList<>();
        tokenizer.tokenize(line);
        do {
            nextToken();
            switch (searchStatus) {
                case START:
                    switch (token.getType()) {
                        case STATEMENT:
                            if(!tokenizer.nextToken().getToken().equals(":"))
                                System.out.println("ERROR");
                    }
            }
        } while(tokenizer.hasTheNextToken());

        int[] test = {1, 3, 4, 24, 93, -13, 4};
        Arrays.sort(test);

        return null;
    }


    /** Parses all the arrays (Algorithm credits to Yidong Fang, Chris Nokleberg, and Dave Hughes) who used the
     * recursive switch algorithm to parse JSON, although this is only one aspect of PSON, it was a great learning
     * from them. PSON Arrays will have the ability to parse and apply functional expressions ie: value: "expr 5^5 * sqrt(4)".
     * They will also be able to combine with each ie: array1: {1, 2, 3}, array2: {4, 5, 6}, array3: {array1, array2} -> value of is
     * [[1, 2, 3], [4, 5, 6]].*/
    @SuppressWarnings("unchecked")
    public Object parse(PSONContainer pcon) {
        if(!line.contains("(") && !line.contains(")")) return parse();
        LinkedList statusStack =  new LinkedList();
        LinkedList valStack = new LinkedList();
        ArrayList<Map<String, Object>> map  = new ArrayList<>();
        tokenizer.tokenize(line);
        try {
            do {
                switch (searchStatus) {
                    case START:
                        nextToken();
                        System.out.println(token.getToken());
                        switch (token.getType()) {
                            case VALUE:
                                searchStatus = IN_ERROR;
                                break;
                            case COLON:
                                break;
                            case COMMA:
                                break;
                            case SEMI_COLON:
                                searchStatus = IN_VALUE;
                                break;
                            case STATEMENT:
                                break;
                            case LPARETHESIS:
                                searchStatus = IN_OBJECT;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(createObject(pcon));
                                break;
                            case LCURLY:
                                searchStatus = IN_ARRAY;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(createArray(pcon));
                                nested++;
                                break;
                           default:
                               searchStatus = IN_ERROR;
                        }
                        break;

                    case IN_VALUE:
                        nextToken();
                        if(token.getToken().equals(";")) {
                            if(map.isEmpty() && valStack.size() > 0)
                                map.add((Map) valStack.removeFirst());
                            nested = nested - (nested - map.size());
                            nested--;
                            System.out.println("NESTED: " + nested);
                            return map;
                        }
//                        if(!tokenizer.nextToken().equals("")) return valStack.removeFirst();
//                        else {
//                            System.out.println("SDSD");
//                            searchStatus = IN_ERROR;
//                        }
                        break;
                    case IN_OBJECT:
                        nextToken();
                        switch (token.getType()) {
                            case COLON:
                                break;
                            case COMMA:
                                break;
                            case SEMI_COLON:
                                searchStatus = IN_VALUE;
                                break;
                            case STATEMENT:
                                String key = token.getToken();
                                valStack.addFirst(key);
                                searchStatus = PAIR_KEY;
                                statusStack.addFirst(searchStatus);
                                System.out.println("ASHIHS : " + key);
                                break;
                            case NUMBER:
                                key = token.getToken();
                                valStack.addFirst(key);
                                searchStatus = PAIR_KEY;
                                statusStack.addFirst(searchStatus);
                                break;
                            case VALUE:
                                key = token.getToken();
                                valStack.addFirst(key);
                                searchStatus = PAIR_KEY;
                                statusStack.addFirst(searchStatus);
                                break;
                            case STRING:
                                key = token.getToken().replace("\"", "");
                                valStack.addFirst(key);
                                searchStatus = PAIR_KEY;
                                statusStack.addFirst(searchStatus);
                                break;
                            case RPARENTHESIS:
                                if(statusStack.size() > 1) {
                                    statusStack.removeFirst();
                                    valStack.removeFirst();
                                    searchStatus = peek(statusStack);
                                } else {
                                    searchStatus = IN_VALUE;
                                }
                                break;
                            default:
                                searchStatus = IN_ERROR;
                                break;
                        }
                        break;

                    case PAIR_KEY:
                        nextToken();
                        switch (token.getType()) {
                            case COLON:
                                break;
                            case COMMA:
                                break;
                            case SEMI_COLON:
                                searchStatus = IN_VALUE;
                                break;
                            case VALUE:
                                statusStack.removeFirst();
                                String key = (String) valStack.removeFirst();
                                Map parent = (Map) valStack.getFirst();
                                parent.put(key, Boolean.parseBoolean(token.getToken()));
                                map.add(parent);
                                searchStatus = peek(statusStack);
                                break;
                            case NUMBER:
                                statusStack.removeFirst();
                                key = (String) valStack.removeFirst();
                                parent = (Map) valStack.getFirst();
                                Object val = token.getToken();
                                if(token.getToken().contains("."))
                                    val = Double.parseDouble(val.toString());
                                else
                                    val = Integer.parseInt(val.toString());
                                parent.put(key, val);
                                map.add(parent);
                                searchStatus = peek(statusStack);
                                break;
                            case STRING:
                                statusStack.removeFirst();
                                key = (String) valStack.removeFirst();
                                parent = (Map) valStack.getFirst();
                                parent.put(key, token.getToken().replace("\"", ""));
                                searchStatus = peek(statusStack);
                                break;
//                            case STATEMENT:
//                                statusStack.removeFirst();
//                                key = (String) valStack.removeFirst();
//                                parent = (Map) valStack.getFirst();
//                                parent.put(key, token.getToken());
//                                searchStatus = peekStatus(statusStack);
//                                pson.primitive(token.getToken());
//                                pson.endObjectEntry();
//                                break;
                            case LCURLY:
                                statusStack.removeFirst();
                                key = (String) valStack.removeFirst();
                                parent = (Map) valStack.getFirst();
                                List newArray = createArray(pcon);
                                parent.put(key, newArray);
                                if(nested > 1) {System.out.println(key + " is nested with " + newArray); nestedNames.add(key);}
                                statusStack.addFirst(IN_PAIR_VALUE);
                                searchStatus = IN_ARRAY;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(newArray);
                                nested++;
                                break;
                            case LPARETHESIS:
                                statusStack.removeFirst();
                                key = (String) valStack.removeFirst();
                                parent = (Map) valStack.getFirst();
                                Map newObject = createObject(pcon);
                                parent.put(key, newObject);
                                statusStack.addFirst(IN_PAIR_VALUE);
                                searchStatus = IN_OBJECT;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(newObject);
                                break;
                            case RPARENTHESIS:
                                break;
                            default:
                                searchStatus = IN_ERROR;
                        }
                        break;

                    case IN_PAIR_VALUE:
                        statusStack.removeFirst();
                        searchStatus = peek(statusStack);
                        break;
                    case END:
                        break;
                    case IN_ARRAY:
                        nextToken();
                        switch (token.getType()) {
                            case COMMA:
                                break;
                            case SEMI_COLON:
                                searchStatus = IN_VALUE;
                                break;
                            case VALUE:
                                List val = (List) valStack.getFirst();
                                val.add(token.getToken());
                                System.out.println(val);
                                break;
                            case NUMBER:
                                List v = (List) valStack.getFirst();
                                v.add(token.getToken());
                                System.out.println(v);
                                break;
                            case STRING:
                                List vx = (List) valStack.getFirst();
                                vx.add(token.getToken().replace("\"", ""));
                                System.out.println(vx);
                                break;
                            case RCURLY:
                                if(statusStack.size() > 1) {
                                    statusStack.removeFirst();
                                    valStack.removeFirst();
                                    searchStatus = peek(statusStack);
                                } else {
                                    searchStatus = IN_VALUE;
                                }
                                break;
                            case LPARETHESIS:
                                v = (List) valStack.getFirst();
                                Map newObject = createObject(pcon);
                                v.add(newObject);
                                searchStatus = IN_OBJECT;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(newObject);
                                nested++;
                                break;
                            case LCURLY:
                                val = (List) valStack.getFirst();
                                List newArray = createArray(pcon);
                                val.add(newArray);
                                searchStatus = IN_ARRAY;
                                statusStack.addFirst(searchStatus);
                                valStack.addFirst(newArray);
                                break;
                                default:
                                    searchStatus = IN_ERROR;
                                    UtilNode.removeDuplicates(map);
                                    if(map.isEmpty() && valStack.size() > 0) {
                                        map.add(toMap(valStack.toString()));
                                    }
                                    System.out.println("NESTED: " + nested);
                                    return new PSONArray(map);
                        }
                        break;

                    case IN_ERROR:
                        ErrorNode.throwError("Unexpected or missing token '" + token.getToken() + "'");
                        break;
                }
                if(searchStatus == IN_ERROR)
                    ErrorNode.throwError("Unexpected or missing token '" + token.getToken() + "'");
            } while(tokenizer.hasTheNextToken());

        } catch (Exception e) {
            if(token.getToken().equals(";")) {
                UtilNode.removeDuplicates(map);
                List val = new ArrayList<>();
                if (map.isEmpty() && valStack.size() > 0)
                    val = valStack;
                nested = nested - (nested - map.size());
                nested--;
                System.out.println("NESTED: " + nested);
                return val;
            }
            else ErrorNode.throwError("Array syntax error! Empty array or missing ';' : token -> '" + token.getToken() + "'");
        }
        ErrorNode.throwError("Unknown parsing exception with token " + token.getToken());
        return null;
    }

    /** Gets all the nested array values */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> getNestedArrays() {
        ArrayList<Map<String, Object>> inner = new ArrayList<>();
        for (int i = 1; i < getArray().size() ; i++) {
            inner.add(getArray().get(i));
        }
        return inner;
    }

    /** High accuracy when retrieving all nested values inside an array that get PSON Properties. Nested values may also contain
     * normal numbers. This will get all the nested arrays from 2 and greater and allow you to retrieve values from those arrays based
     * on the index of the nested (in order) to get the array. This method natively used the {@link PSONArray#get(String)} method
     * to get the value.
     *
     * @return The nested value that corresponds to the {@code targetVal}
     */

    public Object getNestedValue(int index, String targetVal) {
        if(index >= getNestedArrays().size()) pson.nodes.ErrorNode.throwError("Index out of Bounds when retrieving nested array!");
        return getNestedArrays().get(index).get(targetVal);
    }


    public Map<String, Object> getValueAt(int index) {
        return getNestedArrays().get(index);
    }


    /**
     * Gets any array value directly by looping through the list of values and return the
     * value corresponding with the {@param targetVal} and otherwise returns null.
     *
     * @return The value behind the {@param targetVal} and otherwise return null if it isn't inside
     * the list
     */

    public Object get(String key) {
        for(Map<String, Object> prop : getArray()) {
            if(prop.get(key) != null)
                return prop.get(key);
            else if(getDuplicate(key) != null)
                return getDuplicate(key);
            else if(getArray().get(0).toString().contains(key + "=["))
                return getNestedArrays().get(nestedNames.indexOf(key));
        }
        return null;
    }

    private Object getDuplicate(String key) {
        for(Map<String, Object> m : getNestedArrays()) {
            if(m.get(key) != null)
                return m.get(key);
        }
        return null;
    }

    public Object get(String key, int set) {
        if(set <= 0) ErrorNode.throwError("Index out of Bounds for searching!");
        int count = 0;
        for (int i = 0; i < getNestedArrays().size() ; i++) {
            if(getNestedArrays().get(i).get(key) != null)
                count++;
            if(count == set) return getNestedArrays().get(i).get(key);
        }

        return null;
    }



    /**
     * Gets any array integer value directly by looping through the list of values and return the
     * value corresponding with the {@param targetVal} and otherwise returns false.
     *
     * @return The value behind the {@param targetVal} and otherwise return false if it isn't inside
     * the list
     */

    @SuppressWarnings("unchecked")
    public boolean getBoolean(String key) {
        for(Map<String, Object> prop : getArray()) {
            if(prop.get(key) != null) {
                if(ValueNode.isStringBoolean(prop.get(key).toString()))
                    return (boolean) prop.get(key);
            }
        }
        ErrorNode.throwError("Illegal conversion value to BOOLEAN!");
        return false;
    }

    /**
     * Gets any array integer value directly by looping through the list of values and return the
     * value corresponding with the {@param targetVal} and otherwise returns -1.
     *
     * @return The value behind the {@param targetVal} and otherwise return -1 if it isn't inside
     * the list or is not an integer
     */

    @SuppressWarnings("unchecked")
    public int getInt(String key) {
        for(Map<String, Object> prop : getArray()) {
            if(prop.get(key) != null) {
                if(ValueNode.isStringInt(prop.get(key).toString()))
                    return (int) prop.get(key);
            }
        }
        ErrorNode.throwError("Illegal conversion value to INTEGER!");
        return -1;
    }

    /**
     * Gets any array integer value directly by looping through the list of values and return the
     * value corresponding with the {@param targetVal} and otherwise returns -1.
     *
     * @return The value behind the {@param targetVal} and otherwise return -1 if it isn't inside
     * the list
     */

    @SuppressWarnings("unchecked")
    public double getDouble(String key) {
        for(Map<String, Object> prop : getArray()) {
            if(prop.get(key) != null) {
                if(ValueNode.isStringDouble(prop.get(key).toString()))
                    return (double) prop.get(key);
            }
        }
        ErrorNode.throwError("Illegal conversion value to DOUBLE!");
        return -1;
    }

    public int getInt(int index) {
        if(ValueNode.isStringInt(this.get(index).toString()))
            return (int) this.get(index);
        else
            ErrorNode.throwError("Illegal conversion from object value to INTEGER!");
        return -1;
    }

    public double getDouble(int index) {
        if(ValueNode.isStringDouble(this.get(index).toString()))
            return (double) this.get(index);
        else
            ErrorNode.throwError("Illegal conversion from object value to DOUBLE!");
        return -1;
    }

    public boolean getBoolean(int index) {
        if(ValueNode.isStringBoolean(this.get(index).toString()))
            return (boolean) this.get(index);
        else
            ErrorNode.throwError("Illegal conversion from object value to BOOLEAN!");
        return false;
    }

    /** Creates an array for the parser */
    private List createArray(PSONContainer pson) {
        if(pson == null)
            return new PSONArray();
        List list = pson.newArray();
        if(list == null)
            return new PSONArray();
        return list;
    }

    /** Creates a PSON object for the parser */
    private Map createObject(PSONContainer pson) {
        if(pson == null)
            return new PSONObject();
        Map map = pson.newObject();
        if(map == null)
            return new PSONObject();
        return map;
    }
}
