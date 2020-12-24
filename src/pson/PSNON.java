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

import pson.arrays.PSONArray;
import pson.base.Module;
import pson.base.PSONElement;
import pson.base.PSONProperty;
import pson.base.Package;
import pson.nodes.UtilNode;
import pson.parsers.*;

import java.util.*;

public class PSNON {

    private static Map<String, Object> properties = new LinkedHashMap<>();
    private static Map<String, ArrayList<PSONProperty>> elements = new LinkedHashMap<>();
    private static Map<String, ArrayList<PSONElement>> packages = new LinkedHashMap<>();
    private static Map<String, ArrayList<Package>> module = new LinkedHashMap<>();
    private static Map<String, PSONArray> arrays = new LinkedHashMap<>();

    private static ArrayList<Package> pkg = new ArrayList<>();
    private static ArrayList<Module> mod = new ArrayList<>();
    private static ArrayList<PSONProperty> el = new ArrayList<>();
    private static int count = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("PSON>: ");
        String location = input.nextLine();
        compile(location);
    }

    private static void compile(String scriptLocation) {
        ParserNode[] nodes = {
                new PropertiesParser(),
                new ElementsParser(),
                new PackageParser(),
                new ModuleParser(),
                new ArrayParser()
        };
        String script = UtilNode.loadFileAsString(scriptLocation);
        for(String line : script.split("\n")) {
            line = line.trim();
            count++;
            for(ParserNode node : nodes) {
                if(node.canParse(line)) {
                    node.parse(script);
                }
            }
        }

        setupNodes();
        printNodeTree();
        System.out.println(arrays);

        Tokenizer tokenizer = new Tokenizer();
        String line = "newArray: {(wow: \"cool\", nicely: 34, heart: {5, 4, 3, 2, 1,  (oops: \"yokes\", yikes: {(wow: 34, yeet: \"harneet\"), (randoms: 342.343, ar: {1, 2, \n3, 4})})}, nice: {(hello: \"hello\", isSolid: true, aa: {(vbar: \"d/t\", super: 3434)})})};";
        String line3 = "newArray: {(wow: \"cool\", nicely: 34, heart: {5, 4, 3, 2, 1, (oops: \"yokes\", yikes {(wow: 34, yeet: \"harneet\"), (random: 343.343, ar: {1, 2, \n3, 4})})}, nice: {(hello: \"hello\", isSolid: true, aa: {(vbar: \"d/t\", super: 3434)})})};";
        String line2 = "newArray: {\n" +
                "            (\n" +
                "                wow: \"kj\",\n" +
                "                nicely: 34,\n" +
                "                heart: {\n" +
                "                    5, 4, 3, 2, 1,\n" +
                "                    (\n" +
                "                        wow: \"yokes\",\n" +
                "                        yikes: { \n" +
                "                            (\n" +
                "                                wow: 34,\n" +
                "                                yeet: \"harneet\"\n" +
                "                            ),\n" +
                "                            (\n" +
                "                                wow: 342.343,\n" +
                "                                ar: {1, 2, " +
                "                                   3, 4}\n" +
                "                            )\n" +
                "                        }\n" +
                "                     )\n" +
                "                },\n" +
                "                nice: {\n" +
                "                    (\n" +
                "                        wow: \"hello\",\n" +
                "                        isSolid: true,\n" +
                "                        aa: {\n" +
                "                        (\n" +
                "                            vbar: \"d/t\",\n" +
                "                            super: 3434\n" +
                "                        )\n" +
                "                    }\n" +
                "                   )\n" +
                "               }\n" +
                "           )\n" +
                "        };";

        Handler pson = new Handler() {
            @Override
            public void start() {
                System.out.println("startPSON()");
            }

            @Override
            public void end() {
                System.out.println("endPSON()");
            }

            @Override
            public boolean startObject() {
                System.out.println("startObject()");
                return true;
            }

            @Override
            public boolean endObject() {
                System.out.println("endObject()");
                return true;
            }

            @Override
            public boolean startObjectEntry(String key) {
                System.out.println("startObjectEntry(), key:" + key);
                return true;
            }

            @Override
            public boolean endObjectEntry() {
                System.out.println("endObjectEntry()");
                return true;
            }

            @Override
            public boolean startArray() {
                System.out.println("startArray()");
                return true;
            }

            @Override
            public boolean endArray() {
                System.out.println("endArray()");
                return true;
            }

            @Override
            public boolean primitive(Object value) {
                System.out.println("primitive(): " + value);
                return true;
            }

            @Override
            public boolean startPrimitiveObject(String key) {
                System.out.println("primitiveObject(): " + key);
                return true;
            }
        };

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

    }



    public static List<PSONProperty> valuesBetween(String from, String to, Map<String, PSONProperty> map) {
        List<PSONProperty> result = new ArrayList<>();
        boolean inRange = false;
        for (Map.Entry<String, PSONProperty> entry : map.entrySet()) {
            if (entry.getKey().equals(from))
                inRange = true;
            if (inRange)
                result.add(entry.getValue());
            if (entry.getKey().equals(to))
                break;
        }
        return result;
    }


    /**
     * Inside all the values in the script, there are all node tree values. This means one class of
     * values in the child of the super node. The only class that doesn't have sub-node is the {@link PSONProperty} class
     * because it represents the lowest level of information that can be attained.
     */

    private static void printNodeTree() {
        for(Map.Entry<String, ArrayList<Package>> entry: module.entrySet()) {
            for (int i = 0; i < packages.size() ; i++) {
                for (int j = 0; j < module.get(mod.get(0).getName()).get(i).getElements().size() ; j++) {
                    System.out.println(entry.getKey() + " -> " + entry.getValue().get(i).getName() + " -> " + entry.getValue().get(i).getChildren().get(j).getName() + " -> " + entry.getValue().get(i).getChildren().get(j).getChildren());
                }
            }
        }
    }

    /**
     * It setups all the nodes by applying the children of the nodes and setting appropriate
     * super nodes to all the given nodes currently. It will setup all the information needed to
     * print the node tree and use it.
     */

    @SuppressWarnings("unchecked")
    private static void setupNodes() {
        for(Map.Entry<String, ArrayList<Package>> entry : module.entrySet()) {
            for (int i = 0; i < packages.size() ; i++) {
                for (int j = 0; j < module.get(mod.get(0).getName()).get(i).getElements().size() ; j++) {
                    for (int k = 0; k < packages.size() ; k++) {
                        entry.getValue().get(i).setSuperNode(new Module(entry.getKey(), entry.getValue()));
                        entry.getValue().get(i).getElements().get(j).setSuperNode(entry.getValue().get(i));
                        entry.getValue().get(i).getElements().get(j).getChildren().get(k).setSuperNode(entry.getValue().get(i).getElements().get(j));
                    }
                }
            }
        }
    }

    /** Gets all the arrays in the file and allows access to them and their elements */
    public static Map<String, PSONArray> getArrays() {
        return arrays;
    }

    /** Gets all the properties detected in the file */
    public static Map<String, Object> getProperties() {
        return properties;
    }

    /** Gets all the elements detected in the file */
    public static Map<String, ArrayList<PSONProperty>> getElements() {
        return elements;
    }

    /** Gets all the packages detected in the file */
    public static Map<String, ArrayList<PSONElement>> getPackages() {
        return packages;
    }

    /** Gets the module in the file */
    public static Map<String, ArrayList<Package>> getModule() {
        return module;
    }

    /** Placeholder for map */
    public static ArrayList<Module> getMod() {
        return mod;
    }

    /** Placeholder for map */
    public static ArrayList<Package> getPkg() {
        return pkg;
    }

    /** Placeholder for map */
    public static ArrayList<PSONProperty> getEl() {
        return el;
    }

    /** Get the current line count when parsing */
    public static int getCount() {
        return count;
    }
}
