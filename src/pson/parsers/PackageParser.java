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

import pson.base.*;
import pson.base.Package;
import pson.nodes.*;
import pson.PSNON;

import java.util.ArrayList;
import java.util.Map;

public class PackageParser extends ParserNode {

    static ArrayList<Integer> lines = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private String toParse;

    @Override
    public boolean canParse(String line) {
        toParse = line;
        return line.contains("package");
    }

    @Override
    public void parse(String text) {
        String name = UtilNode.replaceAll(toParse, "package", "\"", "", " ", "{", "}");
        names.add(name);
        lines.add(this.lines(text));
        if(names.size() > 1) {
            for(String n : names) {
                PSNON.getPkg().add(new Package(n));
            }
        }
    }

    /** Fills all packages in case there is only one */
    static void fillPackages() {
        ArrayList<PSONElement> el = new ArrayList<>();
        if(names.size() == 1) {
            for(Map.Entry<String, ArrayList<PSONProperty>> entry : PSNON.getElements().entrySet())
                el.add(new PSONElement(entry.getKey(), entry.getValue()));
            PSNON.getPackages().put(names.get(0), el);
        }
    }
}
