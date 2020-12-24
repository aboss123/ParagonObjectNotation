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

import me.ashboss360.pson.base.Type;
import me.ashboss360.pson.compiler.Lexer;
import me.ashboss360.pson.compiler.Token;
import me.ashboss360.pson.nodes.UtilNode;

import java.util.Scanner;

public class PSON {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("PSON>: ");
        String location = input.nextLine();
        compile(location);
    }

    /** Runs the script and calls the parser to parse */
    private static void compile(String scriptLocation) {
        String script = UtilNode.loadFileAsString(scriptLocation);
        Lexer lexer = new Lexer(script);
        while(lexer.hasTheNextToken()) {
            Token t = lexer.nextToken();
            if(t.getType() == Type.STATEMENT) {
                System.out.println(t.getToken() + " " + t.getType());
            }
        }
        //
//        PSONParser PSONParser = new PSONParser(script);
//        PSONParser.parse();
//        PSONParser.initArrays();
//        System.out.println(PSONParser.getElement("names").getProperties());
//        System.out.println(PSONParser.getArrayFrom(15, 22));
//        System.out.println(PSONParser.getPackages().get(0).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(0).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(0).getProperties());
//        System.out.println(PSONParser.getPackages().get(0).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(1).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(1).getProperties());
//        System.out.println(PSONParser.getPackages().get(0).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(2).getName() + " -> " + PSONParser.getPackages().get(0).getElements().get(2).getProperties());
//        System.out.println();
//        System.out.println(PSONParser.getPackages().get(1).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(0).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(0).getProperties());
//        System.out.println(PSONParser.getPackages().get(1).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(1).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(1).getProperties());
//        System.out.println(PSONParser.getPackages().get(1).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(2).getName() + " -> " + PSONParser.getPackages().get(1).getElements().get(2).getProperties());

    }

}
