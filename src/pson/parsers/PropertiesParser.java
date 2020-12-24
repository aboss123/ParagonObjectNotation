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
import pson.nodes.UtilNode;
import pson.nodes.ValueNode;

import java.util.ArrayList;

public class PropertiesParser extends ParserNode {

    private String toParse;

    @Override
    public boolean canParse(String line) {
        toParse = line;
        return line.contains(":") && !line.contains("export");
    }

    @Override
    public void parse(String text) {
        ArrayList<Integer> lines = getLine(text, "};");
        ArrayList<Integer> startLines = getLine(text, "array");
        // Setup
        String val;
        Object convertedValue;
        String name = toParse.substring(0, toParse.indexOf(":"));
        name = name.replaceFirst("array", "");
        name = name.replace(" ", "");
        val = toParse.substring(toParse.indexOf(":") + 1);
        val = UtilNode.replaceAll(val, "\"", ",");

        // Escape characters
        val = val.replace("\\t", "\t");
        val = val.replace("\\\\", "\\");

        // Keyword escape
        if (val.contains("\\p")) val = "package";
        if (val.contains("\\e")) val = "export";
        if (val.contains("\\m")) val = "module";
        val = val.substring(1);

        // Convert string value
        if (!val.contains("{") && !val.contains("}")) {
            convertedValue = ValueNode.convertValue(val);
            PSNON.getProperties().put(name, convertedValue);
        }
    }
}

