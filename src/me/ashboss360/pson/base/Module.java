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

public class Module extends Node {

    /** The name of the module */
    private String name;

    /** The packages under the module */
    private ArrayList<Package> packages;

    /**
     * The constructor takes in a name and an array of packages
     * that make it up.
     *
     * @param name The name of the module
     */

    @SuppressWarnings("unchecked")
    public Module(String name) {
        super(null, Package.class);
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public Module(String name, ArrayList<Package> packages) {
        super(null, Package.class);
        this.name = name;
        this.packages = packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }

    /** Gets the name of the module */
    public String getName() {
        return name;
    }

    /** The packages in the module */
    public ArrayList<Package> getPackages() {
        return packages;
    }
}
