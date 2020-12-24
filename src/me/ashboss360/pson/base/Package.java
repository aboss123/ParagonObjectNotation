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

package me.ashboss360.pson.base;

import pson.nodes.Node;

import java.util.ArrayList;

/**
 * The package is under the complete module and has many
 * elements under it that can be extracted.
 * @author Ashish Bailkeri
 */

public class Package extends Node<Module> {

    /** The name of the package */
    private String name;

    /** The elements under the package */
    private ArrayList<Element> elements;

    /**
     * This constructor takes in a name and an array
     *
     * @param name The name of the package
     */

    public Package(String name) {
        super(null, Element.class);
        this.name = name;
    }

    public Package(String name, ArrayList<Element> elements) {
        super(null, Element.class);
        this.name = name;
        this.elements = elements;
        for(Element element : elements)
            addChild(element);
    }

    /** Gets the name of the package */
    public String getName() {
        return name;
    }


    /** Gets all the elements under the package */
    public ArrayList<Element> getElements() {
        return elements;
    }

    /** Gets the element by the name */
    public Element getElement(String elementName) {
        for(Element element : elements) {
            if(element.getName().equals(elementName))
                return element;
        }
        return null;
    }

    /** Sets the elements to the package */
    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }
}
