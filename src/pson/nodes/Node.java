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

package pson.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node<SUPER extends Node> {

    /** This is the node that is above the current node */
    private SUPER superNode;

    /** The children of the current node */
    private ArrayList<Node> subNode;

    /** The direct class of the node */
    private Class child;

    /** The constructor that contains the {@param The node that is the super node} of
     * the current node */
    public Node(SUPER superNode, Class child) {
        this.superNode = superNode;
        this.child = child;
        this.subNode = new ArrayList<>();
    }

    /** Retrieves all the children from a node */
    @SuppressWarnings("unchecked")
    public <T extends Node> List<T> getChildren() {
        return getSubNode().stream()
                .filter(node -> child.isAssignableFrom(node.getClass()))
                .map(node -> (T) node)
                .collect(Collectors.toList());
    }

    /** Adds another child to the node */
    protected <T extends Node> void addChild(T node) {
        subNode.add(node);
    }


    /** Retrieves the highest node above the current */
    @SuppressWarnings("unused")
    public SUPER getSuperNode() {
        return superNode;
    }

    /** Sets the super node of a node */
    public void setSuperNode(SUPER node) {
        this.superNode = node;
    }

    /** Gets all the remaining children of the current node */
    private ArrayList<Node> getSubNode() {
        return subNode;
    }

    public abstract String getName();

}
