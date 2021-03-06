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

/**
 * This class represents a value of the {@link PSONProperty}
 * @author Ashish Bailkeri
 */

public class Value {

    /** The type of the value */
    private Type type;

    /** The input value to get inferred types from */
    private String input;

    /**
     * The constructor takes in a type that represents
     * its value.
     *
     * @param type The type of the value
     */
    public Value(String input, Type type) {
        this.input = input;
        this.type = type;
    }



    /** Gets the type of the value */
    public Type getType() {
        return type;
    }
}
