public class CircularQueue {
    private int rear, front;
    private Object[] elements;

    public CircularQueue(int capacity) {
        this.rear = -1;
        this.front = 0;
        this.elements = new Object[capacity];
    }

    public void Enqueue(Object data){
        if (isFull()) {
            System.out.println("Queue overflow");
        } else {
            rear = (rear +1) % elements.length;
            elements[rear] = data;
        }
    }

    public Object Dequeue(){
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return null;
        } else {
            Object retData = elements[front];
            elements[front] = null;
            front = (front +1) % elements.length;
            return retData;
        }
    }

    public Object Peek(){
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return null;
        } else {
            return elements[front];
        }
    }

    public boolean isEmpty(){
        return elements[front] == null;
    }

    public boolean isFull(){
        return (front == (rear + 1) % elements.length && elements[front] != null && elements[rear] != null);
    }

    public int Size(){
        if (rear >= front) {
            return rear - front +1;
        }
        else if (elements[front] != null) {
            return elements.length - (front - rear) +1;
        }
        else {
            return 0;
        }
    }
}


