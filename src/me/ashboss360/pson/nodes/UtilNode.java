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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A node that contains all useful functions for parsing and getting
 * tokens from a string.
 * @author Ashish Bailkeri
 */

public class UtilNode extends Node {

    public UtilNode() {
        super("Utility Functions");
    }

    /** @return The file when it is converted to a string */
    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null)
                builder.append(line + "\n");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /** Removes all duplicate items within an ArrayList */
    public static ArrayList removeDuplicates(ArrayList list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if((list.get(i).equals(list.get(i + 1))))
                list.remove(i + 1);
        }
        return list;
    }

    /** @return The string without the duplicates */
    public static String removeDuplicates(String line) {
        String result = "";
        String[] words = getWords(line);
        for (String word : words) {
            if(!result.contains(String.valueOf(word))) {
                result = result.concat(String.valueOf(word));
                result = result.concat("  ");
            }
        }
        return result;
    }

    /** Removes all duplicates from a map */
    public static void removeDuplicates(Map map) {
        for (int i = 0; i < map.size() ; i++) {
            if(map.get(i).equals(map.get(i + 1)))
                map.remove(i + 1);
        }
    }

    /** @return The array of strings that are made up of words */
    public static String[] getWords(String line) {
        ArrayList<Integer> posList = getBlankPositions(line);
        int numWords = getBlankPositions(line).size() + 1;
        String[] words = new String[numWords];
        for (int i = 0; i < numWords ; i++) {
            if(i == 0) {
                if(posList.size() != 0)
                    words[i] = line.substring(0, posList.get(i));
                else
                    words[i] = line;
            }
            else if(i == posList.size())
                words[i] = line.substring(posList.get(i - 1) + 1);
            else
                words[i] = line.substring(posList.get(i - 1) + 1, posList.get(i));
        }
        return words;
    }

    /** @return The array list that contains all the blank positions in the string */
    private static ArrayList<Integer> getBlankPositions(String line) {
        ArrayList<Integer> posList = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            if(line.substring(i, i + 1).equals(" "))
                posList.add(i);
        }
        return posList;
    }

    /** @return The number of instances of a value in a string */
    public static int getInstances(String text, String value) {
        int count = 0;
        ArrayList<String> tempArr = new ArrayList<>();
        for(String line : text.split("\n")) {
            line = line.trim();
            for (int i = 0; i < line.length() ; i++)
                tempArr.add(line);
        }
        UtilNode.removeDuplicates(tempArr);
        for(String line : tempArr)
            System.out.println(line);
        return count;
    }

    public static String diff(String s1, String s2) {
        if(s1.length() > s2.length())
            return s1.substring(s2.length() - 1);
        else if(s2.length() > s1.length())
            return s2.substring(s1.length() - 1);
        else
            return null;
    }

    public static String replaceAll(String s, String ... args) {
        for(String a : args)
            s = s.replace(a, "");
        return s;
    }

    public static ArrayList<Integer> getOccurences(String value, String toCheck) {
        int index = toCheck.indexOf(value);
        ArrayList<Integer> indexes = new ArrayList<>();
        while(index >= 0)  {
            System.out.println(index);
            index = toCheck.indexOf(value, index + 1);
            indexes.add(index);
        }
        return indexes;
    }

    /** @return Whether the two objects are equal to each other */
    public static boolean compare(Object v1, Object v2) {
        return v1 == v2;
    }

}
