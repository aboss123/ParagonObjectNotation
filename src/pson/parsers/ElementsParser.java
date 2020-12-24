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

import me.ashboss360.pson.nodes.UtilNode;
import pson.PSNON;

import java.util.ArrayList;

public class ElementsParser extends ParserNode {

    private ArrayList<Integer> lines = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private String toParse;

    @Override
    public boolean canParse(String line) {
        toParse = line;
        return line.contains("export");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void parse(String text) {
//        ArrayList<PSONProperty> p = new ArrayList<>();
//        ArrayList<PSONProperty> pp = this.getPropertiesFrom(text, 25, 51);
//        ArrayList<ArrayList> b = new ArrayList();
//        for (int i = 0; i < pp.size()  ; i++) {
//            if(!pp.get(i).getValue().toString().contains("{")) {
//                p.add(pp.get(i));
//            }
//            else {b.add(p); p = new ArrayList<>();}
//            if(i == pp.size() - 1) b.add(p);
//        }
//        System.out.println(b.get(0).toArray()[0]);
        String name = UtilNode.replaceAll(toParse, "export", "\"", "", " ", ":");
        names.add(name);
        lines.add(this.lines(text));
        if(lines.size() > 1 && names.size() > 1) {
            for (int i = 0; i < lines.size(); i++) {
                if(i == lines.size() - 1) PSNON.getElements().put(names.get(i), this.getPropertiesFrom(text, lines.get(i), (this.totalLines(text))));
                else PSNON.getElements().put(names.get(i), this.getPropertiesFrom(text, lines.get(i), lines.get(i + 1)));
            }
        } else {
            PSNON.getElements().put(names.get(0), this.getPropertiesFrom(text, lines.get(0), (this.totalLines(text))));
        }

        // Handles packages here
        if(PSNON.getPkg().size() > 1) {
            for (int i = 0; i < PSNON.getPkg().size(); i++) {
                if(i == PSNON.getPkg().size() - 1) PSNON.getPackages().put(PSNON.getPkg().get(i).getName(), this.getElementsFrom(text, PackageParser.lines.get(i), (this.totalLines(text))));
                else {
                    PSNON.getPackages().put(PSNON.getPkg().get(i).getName(), this.getElementsFrom(text, PackageParser.lines.get(i), PackageParser.lines.get(i + 1)));
                }
            }
        }
        PackageParser.fillPackages();

        // Handles module here
        PSNON.getModule().put(PSNON.getMod().get(0).getName(), this.getPackagesFrom(text));
    }
}
