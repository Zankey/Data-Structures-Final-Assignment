/*
Trevor Marsano / tlmarsano@gmail.com
CIS 233 / Scovil
Assignment 2
 */

package cis233.a2;

// java imports for writing .txt file
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weiss.util.Stack;

public class A2233TMarAVL <AnyType extends Comparable<? super AnyType>> {
    //tree constructor *Weiss*
    public A2233TMarAVL() {
        root = null;
    }
    
    //*Weiss*
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    // when removing, only logically delete it by setting deleted field to true.
    public void remove(AnyType x) {
        remove(x, root);
    }

    //returns Leftmost leaf
    public AnyType findMin() {
        if(isEmpty()) {
            System.out.println("No minimum - empty tree");
            return null;
        }
        return findMin(root).element;
    }

    //returns Rightmost leaf
    public AnyType findMax() {
        if(isEmpty()) {
            System.out.println("No minimum - empty tree");
            return null;
        }
        return findMax(root).element;
    }

    //returns true if x exists *Weiss*
    public boolean contains(AnyType x) {
        return contains(x, root);
    }


    //Makes the tree logically empty.
    public void makeEmpty() {
        root = null;
    }


    //Test if the tree is logically empty. *Weiss*
    public boolean isEmpty() {
        return root == null;
    }

    //*Weiss*
    public void printTree() { 
        if (isEmpty())
            System.out.println("Tree is currently Empty");
        else
            printTree(root, true);
    }

    //Print the tree contents in sorted order.
    public void printTree(boolean b) {
        if (isEmpty())
            System.out.println("Tree is currently Empty");
        else
            printTree(root, b);
    }

    public void printBalTree(boolean b) {
        if (isEmpty())
            System.out.println("Tree is currently Empty");
        else
            printBalTree(root, b);
    }

    //Internal method to insert into a subtree.
    //
    // @param x the item to insert.
    // @param t the node that roots the subtree.
    // @return the new root of the subtree.
    public A2233TMarAvlNode<AnyType> insert(AnyType x, A2233TMarAvlNode<AnyType> t) {
        if (t == null)
            return new A2233TMarAvlNode(x, null, null, null);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = insert(x, t.left);
            if (height(t.left) - height(t.right) == 2)
                if (x.compareTo(t.left.element) < 0)
                    t = rotateWithLeftChild(t);
                else
                    t = doubleWithLeftChild(t);

        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
            if (height(t.right) - height(t.left) == 2)
                if (x.compareTo(t.right.element) > 0)
                    t = rotateWithRightChild(t);
                else
                    t = doubleWithRightChild(t);

            //if the node already exists, increment the duplicateCounter
            //insert the duplicate node behind the original and copy its height
        } else if (compareResult == 0) {
            t.duplicateCount++;
            t.duplicate = insert(x, t.duplicate);
            t.duplicate.dupValue++;
            t.duplicate.height = height(t);
        }

        t.height = Math.max(height(t.left), height(t.right)) +1;
        return t;
    }

    public A2233TMarAvlNode<AnyType> findMin(A2233TMarAvlNode<AnyType> t) {

        A2233TMarAvlNode<AnyType> min = root;
        if (t == null)
            return t;

        while (t.left != null) {
            t = t.left;
            if (t.deleted == false) {
                min = t;
            }
        }
        return min;
    }

    public A2233TMarAvlNode<AnyType> findMax(A2233TMarAvlNode<AnyType> t) {
        A2233TMarAvlNode<AnyType> max = root;
        if (t == null)
            return t;

        while (t.right != null) {
            t = t.right;
            if (t.deleted == false) {
                max = t;
            }
        }
        return max;
    }

    //Internal method to find an item in a subtree.
    //
    //@param x is item to search for.
    //@param t the node that roots the tree.
    //@return true if x is found in subtree.
    // *Weiss*
    public boolean contains(AnyType x, A2233TMarAvlNode<AnyType> t) {

        while (t != null) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else
                return true;    // Match
        }
        return false;   // No match
    }

    //if a match is found it will flag as logically deleted
    //if the node is already deleted, it will search for duplicates to logically delete
    public boolean remove(AnyType x, A2233TMarAvlNode<AnyType> t) {
        while (t != null) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else {

                if (t.deleted == false) {
                    t.deleted = true;
                    System.out.println("Data Node " + x + " has been lazily deleted");
                    return true;
                } else if (t.duplicate != null) {
                    t = t.duplicate;
                }
            }
        }
        return false;
    }

    // recursive traversal.
    public void printTree(A2233TMarAvlNode<AnyType> t, boolean b) {
        if (t != null) {

            if (b == true) {
                printTree(t.left, b);
                // if there is a duplicate present
                // print all duplicates behind the original node
                // before continuing method.
                if (t.duplicate != null) {
                    printTree(t.duplicate, b);
                }
                if (t.deleted != true) {
                    System.out.println(t.element);
                }
                printTree(t.right, b);
            } else {
                printTree(t.right, b);
                // if there is a duplicate present
                // print all duplicates behind the original node
                // before continuing method.
                if (t.duplicate != null) {
                    printTree(t.duplicate, b);
                }
                if (t.deleted != true) {
                    System.out.println(t.element);
                }
                printTree(t.left, b);
            }
        }
    }

    //similar to printTree() with output formatting
    public void printBalTree(A2233TMarAvlNode<AnyType> t, boolean order) {

        if (isEmpty()) {
            System.out.println("Tree is currently empty.");
        } else {

            if (order == true) {
                if (t != null) {
                    printBalTree(t.left, true);
                    // if there is a duplicate present
                    // print all duplicates behind the original node
                    // before continuing method.
                    if (t.duplicate != null) {
                        printBalTree(t.duplicate, true);
                    }

                    System.out.format("%-6s%-7d%-8s%-7s%-9s%-7d\n",
                            "Data:", t.element, "Height:",  t.dupValue > 0 ? "Copy": height(t), "Balance:",
                            (height(t.right) - height(t.left)));

                    if (t.left == null) {
                        System.out.format("%-6s%-7s%-7s\n", "", "Left:", "null");

                    } else
                        System.out.format("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n", "",
                                "Left:", "Data:", t.left.element, "Height:",
                                height(t.left), "Balance:",
                                (height(t.left.right) - height(t.left.left)));

                    if (t.right == null) {
                        System.out.format("%-6s%-7s%-7s\n\n", "", "Right:", "null");

                    } else
                        System.out.format("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n\n",
                                "", "Right:", "Data:", t.right.element, "Height:",
                                height(t.right), "Balance:",
                                (height(t.right.right) - height(t.right.left)));

                    printBalTree(t.right, true);
                }
            } else {
                if (t != null) {
                    printBalTree(t.right, false);

                    if (t.duplicate != null) {
                        printBalTree(t.duplicate, false);
                    }
                    System.out.format("%-6s%-7d%-8s%-7s%-9s%-7d\n",
                            "Data:", t.element, "Height:", t.dupValue > 0 ? "Copy": height(t), "Balance:",
                            (height(t.right) - height(t.left)));

                    if (t.left == null) {
                        System.out.format("%-6s%-7s%-7s\n", "", "Left:", "null");

                    } else
                        System.out.format("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n",
                                "", "Left:", "Data:", t.left.element, "Height:",
                                height(t.left), "Balance:",
                                (height(t.left.right) - height(t.left.left)));

                    if (t.right == null) {
                        System.out.format("%-6s%-7s%-7s\n\n", "", "Right:", "null");

                    } else
                        System.out.format("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n\n",
                                "", "Right:", "Data:", t.right.element, "Height:",
                                height(t.right), "Balance:",
                                (height(t.right.right) - height(t.right.left)));

                    printBalTree(t.left, false);
                }
            }
        }
    }


    public void writeBalTree() {
        if (isEmpty())
            System.out.println("Tree is currently Empty");
        else {
            new File("A2233TMarAVLout.txt");
            writeBalTree(root, true);
        }
    }

    public void writeBalTree(boolean b) {
        if (isEmpty())
            System.out.println("Tree is currently Empty");
        else {
            new File("A2233TMarAVLout.txt");
            writeBalTree(root, b);
        }
    }

    //Wraps a FileWriter in a PrintWriter in order to append while keeping format
    public void writeBalTree(A2233TMarAvlNode<AnyType> t, boolean b) {

        if (t != null) {
            if ( b == true ) {
                writeBalTree(t.left, b);

                if (t.duplicate != null) {
                    writeBalTree(t.duplicate, b);
                }
                FileWriter fw = null;
                try {
                    fw = new FileWriter("A2233TMarAVLout.txt", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter pw = new PrintWriter(fw);

                pw.format("%-6s%-7d%-8s%-7s%-9s%-7d\n",
                        "Data:", t.element, "Height:", t.dupValue > 0 ? "Copy" : height(t), "Balance:",
                        (height(t.right) - height(t.left)));
                pw.println();

                if (t.left == null) {
                    pw.format("%-6s%-7s%-7s\n", "", "Left:", "null");

                } else
                    pw.printf("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n",
                            "", "Left:", "Data:", t.left.element, "Height:",
                            height(t.left), "Balance:",
                            (height(t.left.right) - height(t.left.left)));
                pw.println();

                if (t.right == null) {
                    pw.printf("%-6s%-7s%-7s\n", "", "Right:", "null");

                } else
                    pw.printf("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n",
                            "", "Right:", "Data:", t.right.element, "Height:",
                            height(t.right), "Balance:",
                            (height(t.right.right) - height(t.right.left)));
                pw.println();
                pw.println();
                pw.close();

                writeBalTree(t.right, b);
            }

            else{
                writeBalTree(t.right, b);

                if (t.duplicate != null) {
                    writeBalTree(t.duplicate, b);
                }
                FileWriter fw = null;
                try {
                    fw = new FileWriter("A2233TMarAVLout.txt", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter pw = new PrintWriter(fw);

                pw.format("%-6s%-7d%-8s%-7s%-9s%-7d\n",
                        "Data:", t.element, "Height:",  t.dupValue > 0 ? "Copy": height(t), "Balance:",
                        (height(t.right) - height(t.left)));
                pw.println();

                if (t.left == null) {
                    pw.format("%-6s%-7s%-7s\n", "", "Left:", "null");

                } else
                    pw.printf("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n",
                            "", "Left:", "Data:", t.left.element, "Height:",
                            height(t.left), "Balance:",
                            (height(t.left.right) - height(t.left.left)));
                pw.println();

                if (t.right == null) {
                    pw.printf("%-6s%-7s%-7s\n", "", "Right:", "null");

                } else
                    pw.printf("%-6s%-7s%-6s%-9d%-8s%-7d%-9s%-7d\n",
                            "", "Right:", "Data:", t.right.element, "Height:",
                            height(t.right), "Balance:",
                            (height(t.right.right) - height(t.right.left)));
                pw.println();
                pw.println();
                pw.close();

                writeBalTree(t.left, b);
            }
        }
    }

    //Return the height of node t, or -1, if null.
    //*Weiss*
    public int height(A2233TMarAvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    //Roatation methods are all from Weiss
    public A2233TMarAvlNode<AnyType> rotateWithLeftChild(A2233TMarAvlNode<AnyType> k2) {
        A2233TMarAvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    public A2233TMarAvlNode<AnyType> rotateWithRightChild(A2233TMarAvlNode<AnyType> k1) {
        A2233TMarAvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    public A2233TMarAvlNode<AnyType> doubleWithLeftChild(A2233TMarAvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    public A2233TMarAvlNode<AnyType> doubleWithRightChild(A2233TMarAvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    public static class A2233TMarAvlNode<AnyType> {
        // Constructors
        // @field duplicateCount, keeps track of how many duplicates there are of that node
        // @field deleted, stores a flag for lazy deletion
        // @field duplicate, stores the chain of duplicates in a
        // third dimension "behind" the tree
        // duplicateCount keeps track of how many duplicates for mode function

        A2233TMarAvlNode(AnyType theElement) {
            this(theElement, null, null, null);
        }

        A2233TMarAvlNode(AnyType theElement, A2233TMarAvlNode<AnyType> lt,
                         A2233TMarAvlNode<AnyType> rt, A2233TMarAvlNode<AnyType> dup) {
            element = theElement;
            left = lt;
            right = rt;
            duplicate = dup; // third branch located "behind" a node for duplicate storage.
            height = 0;
            deleted = false; // flag for lazy deletion
            duplicateCount = 0;
            dupValue = 0;
        }

        AnyType element;      // The data in the node
        A2233TMarAvlNode<AnyType> left;         // Left child
        A2233TMarAvlNode<AnyType> right;        // Right child
        int height;       // Height
        boolean deleted;  // Lazy deletion
        int duplicateCount; // Holds number of duplicates for comparisons
        int dupValue = 0; // which duplicate it is


        A2233TMarAvlNode<AnyType> duplicate;
    }


    //The tree root.
    public A2233TMarAvlNode<AnyType> root;

    public String author() {
        return "Trevor Marsano";
    }

    //find mode uses a stack to traverse the tree.
    public Result<AnyType> findMode() {

        Stack<A2233TMarAvlNode<AnyType>> stk = new Stack();
        A2233TMarAvlNode<AnyType> current = root;
        A2233TMarAvlNode<AnyType> newMode = root;
        int count = 0;

        while (current != null) {
            stk.push(current);
            current = current.left;
        }

        while (stk.size() > 0) {
            current = stk.pop();
            if (current.duplicateCount > count) {
                newMode = current;
                count = current.duplicateCount;
            }
            if (current.right != null) {
                current = current.right;

                while (current != null) {
                    stk.push(current);
                    current = current.left;
                }
            }
        }

        A2233TMarResult<AnyType> r =
                new A2233TMarResult(newMode.element, newMode.duplicateCount + 1);
        return r;
    }
    
    //Result Obj that stores mode info
    public static class A2233TMarResult
            <AnyType extends Comparable<? super AnyType>> implements Result {

        private AnyType mode;
        private int count = 0;

        public A2233TMarResult(AnyType newMode, int newCount) {
            mode = newMode;
            count = newCount;
        }

        public AnyType mode() {
            return mode;
        }

        public int count() {
            return count;
        }
    }
}

