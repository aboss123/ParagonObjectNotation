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

import me.ashboss360.pson.base.Element;
import me.ashboss360.pson.base.ElementProperties;
import me.ashboss360.pson.base.Package;
import me.ashboss360.pson.nodes.ErrorNode;
import me.ashboss360.pson.nodes.ParserNode;

import java.util.ArrayList;

/**
 * module TestModule {
 *
 *     package TestFamily {
 *         export "family":
 *             onlyChild: true
 *             mother: true
 *             father: false
 *             h: 342
 *     }
 *
 *     package Sweats {
 *         export "names":
 *             name1: "\p"
 *             name2: "DDD"
 *             names : [
 *                 "hi",
 *                 "jk",
 *                 "lmao"
 *             ]
 *             names: 34
 *     }
 * }
 */

public class PSONParser extends ParserNode {

    private static int count;

    private int curlyOne;
    private int curlyTwo;

    public PSONParser(String file) {
        super(file);
    }

    public void parse() {
        checkElementProperties(properties);
        for (String line : this.getFile().split("\n")) {
            line = line.trim();
            Lexer lexer = new Lexer(line);
            count++;
            for (int i = 0; i < line.length(); i++) {
                Token t = lexer.nextToken();
                getCurlyCount(t);
                parseElementName(t, lexer);
                parsePackageName(t, lexer);
            }
        }
        initElements();
        initPackages();
        evalCurlyBraces();
    }

    private void getCurlyCount(Token t) {
        if (t.getToken().equals("{"))
            curlyOne++;
        if (t.getToken().equals("}"))
            curlyTwo++;
    }

    /**
     * Will set error to last line due to parsing
     */
    private void evalCurlyBraces() {
        System.out.println(curlyOne);
        System.out.println(curlyTwo);
        if (curlyOne != curlyTwo)
            ErrorNode.throwError("Missing '}' character!");
    }

    private void initPackages() {
        int count = 0;
        ArrayList<Integer> lines = new ArrayList<>();
        ArrayList<Integer> lines2 = new ArrayList<>();
        for (String line : this.getFile().split("\n")) {
            line = line.trim();
            count++;
            for (int i = 0; i < line.length(); i++) {
                if (line.contains("package"))
                    lines.add(count);
            }
        }
        for (int i = 0; i < lines.size() - 2; i++) {
            if (!lines.get(i).equals(lines.get(i + 1)))
                lines2.add(lines.get(i));
        }
        lines2.add(lines.get(lines.size() - 1));
        for (int i = 0; i < lines2.size(); i++) {
            if (i == lines2.size() - 1)
                packages.get(i).setElements(this.getElementsFrom(lines2.get(i), (lines.size() + lines2.get(i))));
            else packages.get(i).setElements(this.getElementsFrom(lines2.get(i), lines2.get(i + 1)));
        }
    }

    /**
     * Gives packages their name
     */
    private void parsePackageName(Token t, Lexer lexer) {
        String name;
        if (t.getToken().equals("package")) {
            t = lexer.nextToken();
            if (t.getToken().equals("") || t.getToken().equals("{")) ErrorNode.throwError("Package must have a name!");
            else {
                name = t.getToken();
                packages.add(new Package(name));
            }
        }
    }

    /**
     * Gives existing elements their {@link me.ashboss360.pson.base.ElementProperties}
     */
    private void initElements() {
        int count = 0;
        ArrayList<Integer> lines = new ArrayList<>();
        ArrayList<Integer> lines2 = new ArrayList<>();
        for (String line : this.getFile().split("\n")) {
            line = line.trim();
            count++;
            for (int i = 0; i < line.length(); i++) {
                if (line.contains("export"))
                    lines.add(count);
            }
        }
        for (int i = 0; i < lines.size() - 2; i++) {
            if (!lines.get(i).equals(lines.get(i + 1)))
                lines2.add(lines.get(i));
        }
        lines2.add(lines.get(lines.size() - 1));
//        for (int i = 0; i < lines2.size(); i++) {
//            if (i == lines2.size() - 1)
//                elements.get(i).setProperties(this.getPropertiesFrom(lines2.get(i), (lines.size() + lines2.get(i))));
//            else elements.get(i).setProperties(this.getPropertiesFrom(lines2.get(i), lines2.get(i + 1)));
//        }
    }

    public void initArrays() {
        int count = 0;
        ArrayList<Integer> lines = new ArrayList<>();
        ArrayList<Integer> lines2 = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        ArrayList<Object> a1 = new ArrayList<>();
        ArrayList<Object> a2 = new ArrayList<>();

        ArrayList<ArrayList<ElementProperties>> a = new ArrayList<>();
        ArrayList<ArrayList<ElementProperties>> u = new ArrayList<>();
        String line = this.getArraysFrom(15, 33);
        u.add(this.getPropertiesFrom(15, 33));
//        u.add(this.getPropertiesFrom(19, 24));
//        u.add(this.getPropertiesFrom(24, 33));
//        u.add(this.getPropertiesFrom(28, 33));
        System.out.println(u);
        for(String l : this.getFile().split("\n")) {
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
             a.add(this.getPropertiesFrom(lines2.get(i), lines2.get(i + 1)));
        }
        for (int i = 0; i < a.size() ; i++) {
            if(a.get(i).isEmpty()) {
                a.remove(i);
                i--;
            }
        }
//        if (line.contains("[")) {
//            String name = line.substring(0, line.indexOf(":"));
//            line = line.replace("]", "");
//            line = line.substring(line.indexOf("["));
//            line = line.replace("[", "");
//            line = line.replace("\"", "");
//            line = line.replace(" ", "");
//            line = line.replace("\n", "");
//            String[] t = line.split(",");
//            for (String word : t) {
//                a.add(word);
//            }
//        }
//        for (int i = 0; i < a.size(); i++) {
//            if (a.get(i).toString().isEmpty()) {
//                a.remove(i);
//                i--;
//            }
//        }
//        System.out.println(a);
    }
//        for (int i = 0; i < lines.size() - 2; i++) {
//            if(!lines.get(i).equals(lines.get(i + 1)))
//                lines2.add(lines.get(i));
//        }
//        lines2.add(lines.get(lines.size() - 1));
//        for (int i = 0; i < names.size() - 1; i++) {
//            if(names.get(i).contains(names.get(i + 1))) {
//                names.remove(i);
//                i--;
//            }
//        }
//        for (int i = 0; i < lines2.size(); i++) {
//            if(i == lines2.size() - 1) getProperties(names.get(i)).setValue(this.getArrayFrom(lines2.get(i), (lines.size() + lines2.get(i))));
//            else getProperties(names.get(i)).setValue(this.getArrayFrom(lines2.get(i), lines2.get(i + 1)));
//        }
//        for (int i = 0; i < names.size() - 1; i++) {
//            a1 = (ArrayList<Object>) getProperties(names.get(i)).getValue();
//            a2 = (ArrayList<Object>) getProperties(names.get(i + 1)).getValue();
//            for (int j = 0; j < a1.size() - 1; j++) {
//                if(a1.get(j).equals(a1.get(j + 1))) {
//                    a1.remove(j);
//                    j--;
//                }
//            }
//            for (int j = 0; j < a2.size() - 1; j++) {
//                if(a2.get(j).equals(a2.get(j + 1))) {
//                    a2.remove(j);
//                    j--;
//                }
//            }
//        }
//        for(String name : names) {
//            for(PSONElement element : elements) {
//                for (int i = 0; i < element.getProperties().size() ; i++) {
//                    if(element.getProperties().get(i).getName().contains(name))
//                        element.getProperties().get(i).setValue(getProperties(name).getValue());
//                }
//            }
//        }



    /** Gives elements their name */
    private void parseElementName(Token t, Lexer lexer) {
        String name;
        if(t.getToken().equals("export")) {
            t = lexer.nextToken();
            if(t.getToken().equals("") || t.getToken().equals(":")) ErrorNode.throwError("PSONElement must have a name!");
            else {
                name = t.getToken();
                elements.add(new Element(name));
                lexer.nextToken();
                if(!lexer.nextTokenValue().equals(":")) ErrorNode.throwError("Missing ':' character!");
            }
        }
    }

    /** Gets the package by the name */
    public Package get(String packageName) {
        for(Package pk : packages) {
            if(pk.getName().equals(packageName))
                return pk;
        }
        return null;
    }

    /** Gets the element by the name */
    public Element getElement(String elementName) {
        for(Element element : elements) {
            if(element.getName().equals(elementName))
                return element;
        }
        return null;
    }

    /** Get the property by the name */
    public ElementProperties getProperties(String propName) {
        for(ElementProperties prop : properties) {
            String name = prop.getName();
            name = name.replace(" ", "");
            prop.setName(name);
            if(name.contains(propName))
                return prop;
        }
        return null;
    }



    public static int getLineCount() {
        return count;
    }

}
