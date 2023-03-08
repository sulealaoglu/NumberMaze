
public class Queue {
	private Object[] elements;
	private int rear, front;

	public Queue(int capacity) {
		elements = new Object[capacity];
		rear = -1;
		front = 0;
	}

	public void enqueue(Object data) {
		if (isFull())
			System.out.println("Queue overflow!");
		else {
			rear++;
			elements[rear] = data;
		}
	}

	public Object dequeue() {
		Object data = null;
		if (isEmpty()) {
			System.out.println("Queue is empty!");
			return null;
		} else {
			data = elements[front];
			elements[front] = null;
			front++;
			return data;
		}
	}

	public Object peek() {
		return elements[front];
	}

	public boolean isFull() {
		if (rear + 1 == elements.length)
			return true;
		else
			return false;

	}

	public boolean isEmpty() {
		if (rear < front)
			return true;
		else
			return false;
	}

	public int size() {
		return rear - front + 1;
	}
}
