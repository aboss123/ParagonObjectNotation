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

package me.ashboss360.pson.nodes;

import me.ashboss360.pson.base.ElementProperties;
import pson.PSONContainer;
import pson.arrays.PSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles all the value conversions and everything to do with the
 * values of types that are parsed.
 * @author Ashish Bailkeri
 */

public class ValueNode extends  Node {

    public ValueNode() {
        super("Handles all the values");
    }

    @SuppressWarnings("unchecked")
    public static Map asArray(PSONContainer pcon, String value) {
        Map<String, Object> array = new LinkedHashMap<>();
        value = value.replace("{", "(");
        value = value.replace("}", ")");
        value = value.replace("=", ": ");
        System.out.println("TESTDSSD: " + value);
        PSONArray psonArray = new PSONArray(value);
        ArrayList<Map<String, Object>> ret = new ArrayList<>();
        ret = (ArrayList<Map<String, Object>>) psonArray.parse(pcon);
        return ret.get(0);
    }

    /** Converts the given value to an array */
    public static ArrayList<Object> toArray(String value) {
        ArrayList<Object> array = new ArrayList<>();
        if (value.contains(",")) {
            value = value.replace("]", "");
//            array = array.substring(array.indexOf("["));
            value = value.replace("[", "");
            value = value.replace("\"", "");
            value = value.replace(" ", "");
            value = value.replace("\n", "");
            String[] t = value.split(",");
            for (String word : t) {
                array.add(word);
            }
        }

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).toString().isEmpty()) {
                array.remove(i);
                i--;
            }
        }

        for(int i = 0; i < array.size(); i++) {
            if(ValueNode.isStringInt(array.get(i).toString()))
                array.set(i, Integer.parseInt(array.get(i).toString()));
            if(ValueNode.isStringDouble(array.get(i).toString()) && array.get(i).toString().contains("."))
                array.set(i, Double.parseDouble(array.get(i).toString()));
            if(array.get(i).toString().equals("true") || array.get(i).toString().contains("false"))
                array.set(i, Boolean.parseBoolean(array.get(i).toString()));
        }

        return array;
    }


    private static void retrieveArray(String file) {
        int count = 0;
        ArrayList<ArrayList<ElementProperties>> a = new ArrayList<>();
        ArrayList<Integer> lines = new ArrayList<>();
        ArrayList<Integer> lines2 = new ArrayList<>();
        ArrayList<Object> u = new ArrayList<>();
        System.out.println(u);
        for(String l : file.split("\n")) {
            l = l.trim();
            count++;
            for (int i = 0; i < l.length() ; i++) {
                if(l.contains("(") || l.contains(")")) {
                    lines.add(count);
                }
            }
        }
        for (int i = 0; i < lines.size() - 2; i++) {
            if (!lines.get(i).equals(lines.get(i + 1)))
                lines2.add(lines.get(i));
        }
        lines2.add(lines.get(lines.size() - 1));
        for (int i = 0; i < lines2.size() - 1; i++) {
//            a.add(this.getPropertiesFrom(lines2.get(i), lines2.get(i + 1)));
        }
    }

    /** Converts the given value to a double */
    private static double toDouble(String value) {
        return Double.parseDouble(value);
    }

    /** Converts the given value to a string */
    private static String toString(Object value) {
        return value.toString();
    }

    /** Converts the value to an Integer */
    private static int toInt(String value) {
        return Integer.parseInt(value);
    }

    /** Converts the value to a boolean */
    private static boolean toBoolean(String value) {
        return Boolean.parseBoolean(value);
    }


    /** Checks if the string is a int */
    public static boolean isStringInt(String s) {
        try {
            toInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Checks if the string is a double */
    public static boolean isStringDouble(String s) {
        try {
            toDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Checks if the string is a boolean */
    public static boolean isStringBoolean(String s) {
        try {
            toBoolean(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
