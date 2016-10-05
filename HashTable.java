// Joseph Shapiro
// cssc0051

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements DictionaryADT<K, V> {

	private int tableSize, maxSize, currentSize, modCounter;

	private int getIndex(K key) {
		return  (key.hashCode() & 0x7FFFFFFF) % tableSize;
		}
	
	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>> {	
		K key;
		V value;
		
		public DictionaryNode(K key, V value)  {
			this.key = key;
			this.value = value;
		}
		public int compareTo(DictionaryNode<K, V> node){
			return (((Comparable<K>)key).compareTo((K)node.key));
		}
		public int compareValue(DictionaryNode<K, V> node){
			return (((Comparable<V>)value).compareTo((V)node.value));
		}
	}
	
	private LinearListADT<DictionaryNode<K, V>> [] list;
	
	public HashTable(){
		maxSize = 50000;
		tableSize = (int) (maxSize*1.3f);
		currentSize = modCounter = 0;
		list = new LinearList[tableSize];
		for(int i=0; i<tableSize; i++){
			list[i] = new LinearList<DictionaryNode<K,V>>();
		}
	}
	@Override
	public boolean contains(K key) {
		return list[getIndex(key)].find(new DictionaryNode<K,V>(key, null)) != null;
	}
	
	@Override
	public boolean add(K key, V value) {
		if(contains(key)) return false;
		DictionaryNode<K, V> newNode = new DictionaryNode(key, value);
		list[getIndex(key)].addLast(newNode);
		currentSize++;
		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		if( (list[getIndex(key)]).remove(new DictionaryNode<K,V>(key, null)) == null)
			return false;
		currentSize--;
		modCounter++;
		return true;
	}

	@Override
	public V getValue(K key) {
		DictionaryNode<K,V> tmp = list[getIndex(key)].find(new DictionaryNode<K,V>(key, null));
		if(tmp == null) return null;
		return tmp.value;
	}

	@Override
	public K getKey(V value) {
		for(LinearListADT<DictionaryNode<K, V>> n: list){
			for(DictionaryNode<K,V> tmp: n){
				if(tmp.compareValue(new DictionaryNode(null, value)) == 0){
					return tmp.key;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public void clear() {
		currentSize = 0;
		modCounter++;
		list = new LinearList[tableSize];
		for(int i=0; i<tableSize; i++){
			list[i] = new LinearList<DictionaryNode<K,V>>();
		}
	}

	abstract class IteratorHelper<E> implements Iterator<E>{
		protected DictionaryNode<K, V> [] nodes;
		protected int indx;
		protected long modCheck;
	
		private void sort(){
			int in, out, h = 1;
			DictionaryNode temp;
			int size = nodes.length;
			
			while(h<= size/3){
				h = h*3 + 1;
			}
			while(h>0){
				for(out=h; out<size; out++){
					temp = nodes[out];
					in = out;
					while(in>h-1 && (nodes[in-h]).compareTo(temp)>=0){
						nodes[in]= nodes[in-h];
						in -= h;
					}
					nodes[in] = temp;
				}
				h= (h-1)/3;
			}
		}
		
		public IteratorHelper(){
			nodes = new DictionaryNode[currentSize];
			indx = 0;
			int j = 0;
			modCheck = modCounter;
			for(int i=0; i<tableSize; i++){
				for(DictionaryNode n : list[i]){
					nodes[j++] = n;
				}
			}
			sort();
		}
		public boolean hasNext(){
			if(modCheck != modCounter){
				throw new ConcurrentModificationException();
			}
			return indx < currentSize;
		}
		public abstract E next();
		
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}

	class Keys<K> extends IteratorHelper<K>{
		public Keys(){
			super();
		}
		public K next(){
			if(hasNext()) return (K) nodes[indx++].key;
			throw new NoSuchElementException();
		}
	}
	public Iterator<K> keys() {
		return new Keys();
	}

	class Values<V> extends IteratorHelper<V>{
		public Values(){
			super();
		}
		public V next(){
			if(hasNext()) return (V) nodes[indx++].value;
			throw new NoSuchElementException();
		}
	}
	
	public Iterator<V> values() {
		return new Values();
	}

}
