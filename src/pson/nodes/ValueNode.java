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

package pson.nodes;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Handles all the value conversions and everything to do with the
 * values of types that are parsed.
 * @author Ashish Bailkeri
 */

public class ValueNode  {

    public ValueNode() {

    }

    /** Converts the given value to an array */
    public static ArrayList<Object> toArray(String value) {
        ArrayList<Object> array = new ArrayList<>();
        if (value.contains(",")) {
            value = UtilNode.replaceAll(value, "]"," [", "{", "}", "\"", " ", "\n", "(", ")");
            value = value.substring(1);
            String[] t = value.split(",");
            Collections.addAll(array, t);
        }

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).toString().isEmpty()) {
                array.remove(i);
                i--;
            }
        }

        String n;
        for(int i = 0; i < array.size(); i++) {
            if(array.get(i).toString().contains("=[")) {
                n = array.get(i).toString().substring(array.get(i).toString().indexOf("[") + 1);
                array.set(i, n);
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


    public static ArrayList<Object> toArray(String line, int range) {
        ArrayList<Object> newArray = new ArrayList<>();
        for (int i = 0; i < range ; i++)
            newArray.add(toArray(line).get(i));
        return newArray;
    }

    public static ArrayList<Object> toArray(String line, int sRange, int eRange) {
        ArrayList<Object> newArray = new ArrayList<>();
        for (int i = sRange; i < eRange ; i++)
            newArray.add(toArray(line).get(i));
        return newArray;
    }


    /** Converts the values of a string appropriately */
    public static Object convertValue(String s) {
        if(ValueNode.isStringInt(s))
            return Integer.parseInt(s);
        if(s.equals("true") || s.equals("false"))
            return Boolean.parseBoolean(s);
        if(ValueNode.isStringDouble(s))
            return Double.parseDouble(s);
        return s;
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
        return s.equals("true") || s.equals("false");
    }

}
