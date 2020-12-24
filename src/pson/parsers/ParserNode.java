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

package pson.parsers;

import pson.PSNON;
import pson.PSONContainer;
import pson.arrays.PSONArray;
import pson.base.PSONElement;
import pson.base.PSONProperty;
import pson.base.Package;
import pson.nodes.UtilNode;

import java.util.*;

/**
 * Main overall parsing procedures in this node class will be
 * used for parsing anything that can and needs to be parsed.
 *
 * @author Ashish Bailkeri
 */

public abstract class ParserNode {

    /** Checks if the line can be parsed */
    public abstract boolean canParse(String line);

    /** Parses the line if it can */
    public abstract void parse(String text);

    /** Returns an integer with the lines containing the line */
    int lines(String line) {
        int l = 0;
        if(canParse(line))
            l = PSNON.getCount();
        return l;
    }

    /** Gets the first index of a certain line for arrays*/
    int firstIndexOf(String file) {
        int count = 0;
        for(String line : file.split("\n")) {
            line = line.trim();
            count++;
            if(line.contains(":") && line.contains("{"))
                return count;
        }
        return count;
    }

    /** Gets the lines containing the string */
    ArrayList<Integer> getLine(String file, String line) {
        ArrayList<Integer> lines = new ArrayList<>();
        int count = 0;
        for(String l : file.split("\n")) {
            l = l.trim();
            count++;
            if(l.contains(line))
                lines.add(count);
        }
        UtilNode.removeDuplicates(lines);
        return lines;
    }

    ArrayList<Integer> getLine(String file, String line, String sLine) {
        ArrayList<Integer> lines = new ArrayList<>();
        int count = 0;
        for(String l : file.split("\n")) {
            l = l.trim();
            count++;
            if(l.contains(line) || l.contains(sLine))
                lines.add(count);
        }
        UtilNode.removeDuplicates(lines);
        return lines;
    }

    /** Gets all the lines in the file */
    int totalLines(String file) {
        return file.split("\n").length;
    }

    /** Gets all the {@link PSONProperty} from a range of lines in the file to parse */
    ArrayList<PSONProperty> getPropertiesFrom(String file, int startLine, int endLine) {
        int count = 0;
        String combined = "";
        ArrayList<PSONProperty> curr = new ArrayList<>();
        for (String line : file.split("\n")) {
            line = line.trim();
            count++;
            if (count == endLine) break;
            for (int i = 0; i < line.length(); i++) {
                if (count >= startLine + 1) {
                    if (!combined.contains(line) && !combined.contains("package") && (!combined.contains("{") || !combined.contains("}"))) {
                        if (line.contains(":")) {
                            String name = line.substring(0, line.indexOf(":"));
                            name = UtilNode.replaceAll(name, "array", " ");
                            String test = line.substring(line.indexOf(":"));
                            test = UtilNode.replaceAll(test, ":");
                            try {
                                if(test.substring(1).contains(" ")) test = test.substring(1);
                            } catch (StringIndexOutOfBoundsException e) {}

                            if(test.contains("true") || test.contains("false")) {
                                test = test.substring(1);
                            }

//                            if (test.contains("[")) test = test.replace(",", " , ");
//                            if(!test.contains("true") && !test.contains("false") && !test.contains("\"") && !ValueNode.isStringInt(test)
//                            && !test.contains("[") && !test.contains("]"))
//                                ErrorNode.throwError("Invalid Value!");
                            test = UtilNode.replaceAll(test, "\"", "(", ")");
                            if (test.contains("\\e")) test = "export";
                            if (test.contains("\\p")) test = "package";
                            if (test.contains("\\m")) test = "module";
                            curr.add(new PSONProperty(name, test));
                        }
                        combined = combined.concat(line);
                    }
                }
            }
        }
        return curr;
    }

    ArrayList<PSONElement> getElementsFrom(String file, int startLine, int endLine) {
        int count = 0;
        String combined = "";
        String concat = "";
        ArrayList<PSONElement> curr = new ArrayList<>();
        for(String line : file.split("\n")) {
            line = line.trim();
            count++;
            if(count >= endLine) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= startLine + 1) {
                    if(!combined.contains(line)) {
                        if(line.contains("export")) {
                            String name = line.substring(0, line.indexOf(":"));
                            name = UtilNode.replaceAll(name, "export", "\"", " ");
                            for(Map.Entry<String, ArrayList<PSONProperty>> entry : PSNON.getElements().entrySet()) {
                                if(entry.getKey().equals(name) && !concat.contains(name)) {
                                    concat = concat.concat(name);
                                    curr.add(new PSONElement(entry.getKey(), entry.getValue()));
                                }
                            }
                        }
                        combined = combined.concat(line);
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

    ArrayList<Package> getPackagesFrom(String file) {
        int count = 0;
        String combined = "";
        String concat = "";
        ArrayList<Package> curr = new ArrayList<>();
        for(String line : file.split("\n")) {
            line = line.trim();
            count++;
            if(count >= this.totalLines(file)) break;
            for (int i = 0; i < line.length() ; i++) {
                if(count >= 1) {
                    if(!combined.contains(line)) {
                        if(line.contains("package")) {
                            String name = UtilNode.replaceAll(line, "package", " ", "{", "}");
                            for(Map.Entry<String, ArrayList<PSONElement>> entry : PSNON.getPackages().entrySet()) {
                                if(entry.getKey().contains(name) && !concat.contains(name)) {
                                    concat = concat.concat(name);
                                    curr.add(new Package(entry.getKey(), entry.getValue()));
                                }
                            }
                        }
                        combined = combined.concat(line);
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

    PSONArray getArraysFrom(String file, int startLine, int endLine) {
        int count = 0;
        String combined = "";
        for(String line : file.split("\n")) {
            line = line.trim();
            count++;
            if(count >= endLine + 1) break;
            if(count >= startLine) {
                if(line.contains("array")) {
                    line = UtilNode.replaceAll(line, " ");
                    line = line.replaceFirst("array", "");
                }
                combined = combined.concat(line);
            }
        }
        PSONContainer pcon = new PSONContainer() {
            @Override
            public Map newObject() {
                return new LinkedHashMap();
            }

            @Override
            public List newArray() {
                return new LinkedList();
            }
        };
        combined = combined.replaceAll(",", ", ");
        PSONArray array = new PSONArray(combined, pcon);
        return array;
    }

}
