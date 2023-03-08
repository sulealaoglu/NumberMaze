public class Stack {
    private int top;
    private Object[] myStack;

    public Stack(int lenght) {
        myStack = new Object[lenght];
        top = -1;
    }

    public void push(Object data) {
        if (isFull())
            System.out.println("Stack Overflow!");
        else {
            top++;
            myStack[top] = data;
        }

    }

    public Object pop() {
        if (isEmpty()) {
            System.out.println("Stack is Empty!");
            return null;}
        else {
            Object data = myStack[top];
            myStack[top] = null;
            top--;
            return data;
        }

    }

    public boolean isFull() {
        if (top == myStack.length - 1)
            return true;
        else
            return false;
    }

    public boolean isEmpty() {
        if (top == -1)
            return true;
        else
            return false;
    }

    public Object peek() {
        Object data = myStack[top];
        return data;
    }

    public int size() {
        return top+1;
    }

}
