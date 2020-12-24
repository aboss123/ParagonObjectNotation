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

package pson.base;

import pson.nodes.Node;

import java.util.ArrayList;

/**
 * This represents an element inside the script which is less than
 * one single package.
 * @author Ashish Bailkeri
 */

public class PSONElement extends Node<Package> {

    /** The name of the element */
    private String name;

    /** Initializes the element with the two parameters */
    public PSONElement(String name, ArrayList<PSONProperty> properties) {
        super(null, PSONProperty.class);
        this.name = name;
        for(PSONProperty prop : properties) addChild(prop);
    }

    /** Initializes the element with the name */
    public PSONElement(String name) {
        super(null, PSONProperty.class);
        this.name = name;
    }

    /** Initializes the element with the properties */
    public PSONElement(ArrayList<PSONProperty> properties) {
        super(null, PSONProperty.class);
    }


    /** Get the property by the name */
    @SuppressWarnings("unused")
    public Node getProperty(String propName) {
        for(Node prop : getChildren()) {
            String name = prop.getName();
            name = name.replace(" ", "");
            if(name.equals(propName))
                return prop;
        }
        return null;
    }

    /** Gets the name of the element */
    public String getName() {
        return name;
    }

    /** Sets the name of the property */
    public void setName(String name) {
        this.name = name;
    }
}
