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

import me.ashboss360.pson.base.Element;
import me.ashboss360.pson.base.ElementProperties;
import me.ashboss360.pson.base.Package;
import me.ashboss360.pson.compiler.LanguageConstants;
import me.ashboss360.pson.compiler.Token;

import java.util.ArrayList;

public abstract class ParserNode extends Node {

    private String fileToParse;

    protected ArrayList<ElementProperties> properties = new ArrayList<>();
    protected ArrayList<Element> elements = new ArrayList<>();
    protected ArrayList<Package> packages = new ArrayList<>();


    public ParserNode(String fileToParse) {
        super("Parsing");
        this.fileToParse = fileToParse;
    }


    /** @return The array list that contains all the properties within a given text */
    public ArrayList<ElementProperties> retrieveElementProperties() {
        String[] tokens;
        Object fullVal;
        ArrayList<ElementProperties> curr = new ArrayList<>();
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            tokens = UtilNode.getWords(line);
            for (int i = 0; i < tokens.length; i++) {
                if(tokens[i].contains(":")) {
                    tokens[i] = tokens[i].replaceFirst(":", "");
                    String name = line.substring(0, line.indexOf(":"));
                    String test = line.substring(line.indexOf(":"));
                    test = test.replace(":", "");
                    test = test.replace("\"", "");
                    if(test.contains("true") || test.contains("false")) fullVal = Boolean.parseBoolean(test);
                    if(test.contains(LanguageConstants.INTEGER)) fullVal = Integer.parseInt(test);
                    else {test = test.replace(" ", ""); fullVal = test;}
                    curr.add(new ElementProperties(name, fullVal));
                }
            }
        }
        UtilNode.removeDuplicates(curr);
        return curr;
    }

    /** Adds all the {@link ElementProperties} in the file to parse */
    protected void checkElementProperties(ArrayList<ElementProperties> properties) {
        String[] tokens;
        Object fullVal;
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            tokens = UtilNode.getWords(line);
            for (int i = 0; i < tokens.length; i++) {
                if(tokens[i].contains(":")) {
                    tokens[i] = tokens[i].replaceFirst(":", "");
                    String name = line.substring(0, line.indexOf(":"));
                    String test = line.substring(line.indexOf(":"));
                    test = test.replace(":", "");
                    test = test.replace("\"", "");
                    test = test.replace(" ", "");
                    if(test.equals("\\e")) test = "export";
                    if(test.equals("\\p")) test = "package";
                    if(test.equals("\\m")) test = "module";
                    fullVal = test;
                    properties.add(new ElementProperties(name, fullVal));
                }
            }
        }
        UtilNode.removeDuplicates(properties);
    }

    /** Gets all the {@link ElementProperties} from a range of lines in the file to parse */
    public ArrayList<ElementProperties> getPropertiesFrom(int startLine, int endLine) {
        int count = 0;
        String combined = "";
        Object fullVal;
        ArrayList<ElementProperties> curr = new ArrayList<>();
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            count++;
            if(count == endLine) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= startLine + 1) {
                    if(!combined.contains(line) && !combined.contains("package")  && (!combined.contains("{") || !combined.contains("}"))) {
                        if(line.contains(":")) {
                            String name = line.substring(0, line.indexOf(":"));
                            String test = line.substring(line.indexOf(":"));
                            test = test.replace(":", "");
                            test = test.replace(" ", "");
                            if(test.contains("[")) test = test.replace(",", " , ");
//                            if(!test.contains("true") && !test.contains("false") && !test.contains("\"") && !ValueNode.isStringInt(test)
//                            && !test.contains("[") && !test.contains("]"))
//                                ErrorNode.throwError("Invalid Value!");
                            test = test.replace("\"", "");
                            test = test.replace("(", "");
                            test = test.replace(")", "");
                            if(test.equals("\\e")) test = "export";
                            if(test.equals("\\p")) test = "package";
                            if(test.equals("\\m")) test = "module";
                            fullVal = test;
                            curr.add(new ElementProperties(name, fullVal));
                        }
                        combined = combined.concat(line);
                    }
                }
            }
        }
        return curr;
    }

    public void parseArrayValues() {
        ArrayList<Object> array = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        String[] tokens = fileToParse.split("\n");
        String name = "";
        for (int i = 0; i < tokens.length ; i++) {
            if(tokens[i].contains("[")) {
                name = tokens[i].replace(" ", "");
                name = name.substring(0, name.indexOf(":"));
                names.add(name);
            }
            if(!tokens[i].contains("]") && !tokens[i].contains("[") && !tokens[i].contains("package")
            && !tokens[i].contains("module") && !tokens[i].contains("export") && !tokens[i].contains(":") && !tokens[i].contains(":")
            && !tokens[i].contains("}") && !tokens[i].contains("{")) {
                tokens[i] = tokens[i].replace(" ", "");
                tokens[i] = tokens[i].replace(",", "");
                tokens[i] = tokens[i].replace("\"", "");
                array.add(tokens[i]);
            }
        }
        for (int i = 0; i < array.size() ; i++) {
            if(array.get(i).toString().isEmpty()) {
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
        for(String n : names) {
            for(ElementProperties prop : properties) {
                if(prop.getName().contains(n)) {
                    prop.setValue(array);
                    array = null;
                }
            }
        }
//        for(PSONProperty prop : properties) {
//            for(String n : names) {
//                if(prop.getName().contains(n))
//                    prop.setValue(array);
//            }
//        }
//        for(String n : names) {
//            for (PSONElement element : elements) {
//                for (int i = 0; i < element.getProperties().size(); i++) {
//                    System.out.println(n);
//                    if (element.getProperties().get(i).getName().contains(n))
//                        element.getProperties().get(i).setValue(array);
//                }
//            }
//        }
    }

    public ArrayList<Object> getArrayFrom(int startLine, int endLine) {
        int count = 0;
        String combined = "";
        ArrayList<Object> curr = new ArrayList<>();
        Object parsedVal;
        boolean isVar = false;
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            count++;
            if(count >= endLine + 1) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= startLine) {
                    if (!combined.contains(line)) {
                        if(!line.contains("]") && !line.contains("[") && !line.contains("package")
                                && !line.contains("module") && !line.contains("export") && !line.contains(":") && !line.contains(":")
                                && !line.contains("}") && !line.contains("{")) {
                            line = line.replace(" ", "");
                            line = line.replace(",", "");
                            line = line.replace("\"", "");
//                            for(PSONProperty prop : properties) {
//                                if(prop.getName().contains(line)) {
//                                    curr.add(prop.getValue());
//                                    isVar = true;
//                                }
//                            }
//                            if(!isVar)
                            System.out.println(line);
                                curr.add(line);
//                            else break;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < curr.size(); i++) {
            if(ValueNode.isStringInt(curr.get(i).toString()))
                curr.set(i, Integer.parseInt(curr.get(i).toString()));
            if(ValueNode.isStringDouble(curr.get(i).toString()) && curr.get(i).toString().contains("."))
                curr.set(i, Double.parseDouble(curr.get(i).toString()));
            if(curr.get(i).toString().equals("true") || curr.get(i).toString().contains("false"))
                curr.set(i, Boolean.parseBoolean(curr.get(i).toString()));
        }
        return curr;
    }

    public String getArraysFrom(int startLine, int endLine) {
        int count = 0;
        String combined = "";
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            count++;
            if(count >= endLine) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= startLine) {
                    if(!combined.contains(line)) {
                        combined = combined.concat("\n" + line);
                    }
                }
            }
        }
        return combined;
    }

    protected ArrayList<Element> getElementsFrom(int startLine, int endLine) {
        int count = 0;
        String combined = "";
        ArrayList<Element> curr = new ArrayList<>();
        for(String line : fileToParse.split("\n")) {
            line = line.trim();
            count++;
            if(count >= endLine) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= startLine + 1) {
                    if(!combined.contains(line)) {
                        if(line.contains("export")) {
                            String name = line.substring(0, line.indexOf(":"));
                            name = name.replace("export", "");
                            name = name.replace("\"", "");
                            name = name.replace(" ", "");
                            for(Element element : elements) {
                                if(name.contains(element.getName())) {
                                    curr.add(element);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < curr.size() - 1; i++) {
            if(curr.get(i).equals(curr.get(i + 1))) {
                curr.remove(i);
                i--;
            }
        }
        return curr;
    }

    /** @return The amount of a string when lexing */
    public int getAmountOf(String text, Token t) {
        int count = 0;
        if(t.getToken().equals(text))
            count++;
        return count;
    }


    protected String getFile() {
        return fileToParse;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public ArrayList<ElementProperties> getProperties() {
        return properties;
    }

    public ArrayList<Package> getPackages() {
        return packages;
    }
}
