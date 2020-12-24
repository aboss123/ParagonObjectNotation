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

import pson.PSONContainer;
import pson.arrays.PSONArray;
import pson.nodes.UtilNode;

import java.util.*;

public class ArrayParser extends ParserNode {

    private String toParse;

    @Override
    public boolean canParse(String line) {
        toParse = line;
        return line.contains("array");
    }

    @Override
    public void parse(String text) {
        String name = UtilNode.replaceAll(toParse, "array", " ", ":", "{", "}", ";");
        System.out.println("NAME: " + name);
        ArrayList<Integer> lines = getLine(text, "};");
        ArrayList<Integer> startLines = getLine(text, "array");
        int st = this.lines(toParse);
        ArrayList<PSONArray> arrays = new ArrayList<>();
        System.out.println("END: " +  lines);
        System.out.println("START: " + startLines);
        for (int i = 0; i < lines.size() ; i++) {
            System.out.println("START: " + startLines.get(i));
            System.out.println("END: " + lines.get(i));
            arrays.add(this.getArraysFrom(text, startLines.get(i), lines.get(i)));
        }
        System.out.println(arrays);


//        System.out.println(this.getArraysFrom(text, startLines.get(0), lines.get(0)));
//        System.out.println(this.getArraysFrom(text, startLines.get(1), lines.get(1)));
//        System.out.println(this.getArraysFrom(text, startLines.get(2), lines.get(2)));


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
        PSONArray array = new PSONArray("default: {1, 2, 3, \"hi\", \"bye\", (hi: 2, cool: true)};", pcon);
        System.out.println();
    }
}
