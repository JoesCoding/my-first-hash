// Joseph Shapiro
// cssc0051

package data_structures;

import java.util.ConcurrentModificationException; 
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinearList<E> implements LinearListADT<E>{

	private Node<E> head;
	private Node<E> tail;
	private int currentSize;
	private long modificationCounter;
	
	public LinearList(){
		currentSize = 0;
	}
	public class Node<T>{
		T element;
		Node<E> next;
		Node<E> prev;
		
		public Node(T element){
			this.element = element;
			next = prev = null;
		}
	}
	@Override
	public boolean addFirst(E obj) {
		Node<E> temp = new Node<E>(obj);
		if(head == null){
			head = tail = temp;
		}
		else {
			temp.next = head;
			head.prev = temp;
			head = temp;
		}
		currentSize++;
		modificationCounter++;
		return true;
	}

	@Override
	public boolean addLast(E obj) {
		Node<E> temp = new Node<E>(obj);
		if(head == null){
			head = tail = temp;
		}
		else {
			tail.next = temp;
			temp.prev = tail;
			tail = temp;
		}
		currentSize++;
		modificationCounter++;
		return true;
	}

	@Override
	public E removeFirst() {
		if(head == null) return null;
		Node<E> temp = head;
		head = head.next;
		if(head == null) tail = null;
		else head.prev = null;
		currentSize--;
		modificationCounter++;
		return temp.element;
		
	}

	@Override
	public E removeLast() {
		if(head == null) return null;
		Node<E> temp = tail;
		tail = tail.prev;
		if(tail == null) head = null;
		else tail.next = null;
		currentSize--;
		modificationCounter++;
		return temp.element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(E obj) {
		Node<E> current = head;
		while(current!=null && ((Comparable <E>) obj).compareTo(current.element) != 0) {
			current = current.next;
		}
		if(current == null)
			return null;
		if(current == head)
			return removeFirst();
		else if(current == tail)
			return removeLast();
		else {
			current.prev.next = current.next;
			current.next.prev = current.prev;
		}
		currentSize--;
		modificationCounter++;
		return obj;
	}

	@Override
	public E peekFirst() {
		if(currentSize==0)
			return null;
		return head.element;
	}

	@Override
	public E peekLast() {
		if(currentSize==0){
			return null;
		}
		return tail.element;
	}

	@Override
	public boolean contains(E obj) {
		return find(obj)!=null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E find(E obj) {
		Node<E> current = head;
		while(current!=null){
			if(((Comparable<E>) obj).compareTo(current.element)==0){
				return current.element;
			}
			current = current.next;
		}
		return null;
	}

	@Override
	public void clear() {
		head = null;
		tail = null;
		currentSize = 0;
		modificationCounter++;
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
	class IteratorHelper implements Iterator<E> {
		long modCounter;
		Node<E> current;
		
		public IteratorHelper(){
			current = head;
			modCounter = modificationCounter;
		}
		
		public boolean hasNext(){
			if(modCounter != modificationCounter){
				throw new ConcurrentModificationException();
			}
			return current != null;
		}
		
		
		public E next(){
			if(!hasNext()){
				throw new NoSuchElementException();
			}
			E tmp = current.element;
			current = current.next;
			return tmp;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	


}
