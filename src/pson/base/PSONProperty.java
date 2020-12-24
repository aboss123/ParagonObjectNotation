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

import me.ashboss360.pson.nodes.ValueNode;
import pson.nodes.Node;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class is the property under the {@link PSONElement} which is what
 * makes up the element.
 * @author Ashish Bailkeri
 */

public class PSONProperty extends Node<PSONElement> {

    /** The name of the property */
    private String name;

    /** The value that the property has */
    private Object value;

    /**
     * The constructor takes in a name that represents the property
     * of an element.
     *
     * @param name The name that the property has
     * @param value The value of the property
     */
    public PSONProperty(String name, Object value) {
        super(null, null);
        this.name = name;
        this.value = value;
    }

    /** Gets the name of the property */
    public String getName() {
        return name;
    }


    /** Sets the name (used for parsing) */
    public void setName(String name) {
        this.name = name;
    }

    /** Gets the value of the property */
    public Object getValue() {
        if(value.toString().equals("true") || value.toString().equals("false"))
            return Boolean.parseBoolean(value.toString());
        if(ValueNode.isStringInt(value.toString()))
            return Integer.parseInt(value.toString());
        return value;
    }

    /** Converts a string value to an array */
    private ArrayList<Object> toArray(String value) {
        return ValueNode.toArray(value);
    }

    /** Gets the map value as an array */
    public ArrayList<Object> asArray(Map<String, ArrayList<PSONProperty>> val, String objectName, int index) {
        return toArray(val.get(objectName).get(index).getValue().toString());
    }

    /** Get value as an integer */
    public int getAsInt() {
        if(ValueNode.isStringInt(value.toString()))
            return Integer.parseInt(value.toString());
        return -1;
    }

    /** Get value as a boolean */
    public boolean getAsBoolean() {
        if(value.toString().equals("true") || value.toString().equals("false"))
            return Boolean.parseBoolean(value.toString());
        return false;
    }

    /** Gets value as double */
    public double getAsDouble() {
        if(ValueNode.isStringDouble(value.toString()))
            return Double.parseDouble(value.toString());
        return -1;
    }


    /** Sets the value (used for parsing) */
    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        name = name.replace(" ", "");
        return name + ": " + value;
    }
}
