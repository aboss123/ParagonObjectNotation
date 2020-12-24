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

package me.ashboss360.pson;

import me.ashboss360.pson.base.ElementProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Map<String, ArrayList<ElementProperties>> values = new LinkedHashMap<>();
        String test = "test: [(element: 34, el: [1, 2, 3], gh: [\"hi\", \"cool\", 45, 67])]";
        String n1 = test.substring(0, test.indexOf(":"));
        String val = replaceAll(test, ")", "(");
        String n = replaceAll(test, "(", ")", "[", "]", ":");
        String[] names = n.split(" ");
        ArrayList<String> a = new ArrayList<>();
        for(String name : names) {
            name = name.replaceAll("[\\d.]", "");
            name = replaceAll(name, ",", " ");
            if(!name.contains("\"")) {
                a.add(name);
            }
        }
        for (int i = 0; i < a.size() ; i++) {
            if(a.get(i).isEmpty()) {
                a.remove(i);
                i--;
            }
        }
        for(String na : a) {
            val = replaceAll(val, na, ":", "\"", "[", "]");
        }
        String[] o = val.split(" ");
        ArrayList<Object> obj = new ArrayList<>();
        for(String r : o) {
            r = r.replace(" ", "");
            r = r.replace(",", "");
            obj.add(r);
            System.out.println(r);
        }

        for (int i = 0; i < obj.size() ; i++) {
            if(obj.get(i).toString().isEmpty()) {
                obj.remove(i);
                i--;
            }
        }
        System.out.println(obj.get(2));
//        System.out.println(val);
//        System.out.println(a);
    }

    public static String replaceAll(String s, String ... args) {
        for(String a : args)
            s = s.replace(a, "");
        return s;
    }

    public static void printMap(Map m) {
        m.forEach((n1, n2) -> System.out.println("\n" + n1 + ": " + n2));
    }

    private static ArrayList<Integer> getOccurences(String value, String toCheck) {
        int index = toCheck.indexOf(value);
        ArrayList<Integer> indexes = new ArrayList<>();
        while(index >= 0)  {
            System.out.println(index);
            index = toCheck.indexOf(value, index + 1);
            indexes.add(index);
        }
        return indexes;
    }

}
