/**
 * This file contains an implementation of an integer only stack which is
 * extremely quick and lightweight. In terms of performance it can outperform
 * java.util.ArrayDeque (Java's fastest stack implementation) by a factor of 50!
 * See the benchmark test below for proof. However, the downside is you need to
 * know an upper bound on the number of elements that will be inside the stack at
 * any given time for it to work correctly.
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/
package net.asg.games.utils;

public class IntStack {
    private int [] ar;
    private int pos = 0;

    // maxSize is the maximum number of items
    // that can be in the queue at any given time
    public IntStack(int maxSize) { ar = new int[maxSize]; }

    // Returns the number of elements insize the stack
    public int size() { return pos; }

    // Returns true/false on whether the stack is empty
    public boolean isEmpty() { return pos == 0; }

    // Returns the element at the top of the stack
    public int peek() { return ar[pos-1]; }

    // Add an element to the top of the stack
    public void push(int value) { ar[pos++] = value; }

    // Make sure you check that the stack is not empty before calling pop!
    public int pop() { return ar[--pos]; }

    public int length() { return ar.length; }
}
