package data_structures;

import java.util.Iterator;

/**
 * A map interface for study and implementation.
 * 
 * @param <K>
 *          Generic type for the key object
 * @param <V>
 *          Generic type for the value object
 */
public interface DictionaryADT<K, V> {

  /**
   * Returns true if the dictionary holds an object bound to the parameter key
   * within its structure, otherwise false. Note: uses .compareTo
   * 
   * @param key
   *          item to locate within the dictionary
   * @return true if present, false otherwise
   */
  public boolean contains(K key);

  /**
   * Inserts the indicated key/value pair into the dictionary. If the item is
   * already in the dictionary, this method aborts the insertion and warns the
   * caller by returning false.
   * 
   * @param key
   *          item to use as key within map
   * @param value
   *          object to associate with the key
   * @return true if item added to dictionary or false if the dictionary is full
   *         or the key is already present within the dictionary
   */
  public boolean add(K key, V value);

  /**
   * Removes the key/value pair from the dictionary.
   * 
   * @param key
   *          identifier for item to delete
   * @return true if deleted, false if not present
   */
  public boolean delete(K key);

  /**
   * Returns the value associated with the parameter key. This operation must
   * complete in O(log n).
   * 
   * @param key
   *          identifier for item to retrieve
   * @return contents of value or null if not found
   */
  public V getValue(K key);

  /**
   * Returns the key associated with the first instance of an object possessing
   * the indicated value. Uses .compareTo. This operation will require O(n).
   * 
   * @param value
   * @return
   */
  public K getKey(V value);

  /**
   * Indicates quantity of entries in the dictionary.
   * 
   * @return number of items in the dictionary
   */
  public int size();

  /**
   * Indicates if the data structure has reached capacity.
   * 
   * @return true if at capacity, or false otherwise
   */
  public boolean isFull();

  /**
   * Indicates the data structure is empty.
   * 
   * @return true if empty, or false otherwise
   */
  public boolean isEmpty();

  /**
   * Returns the dictionary to an empty state.
   */
  public void clear();

  /**
   * Returns an iterator of the keys in the dictionary in ascending order. The
   * iterator supports fail-fast behavior. Because this iterator must sort the
   * entries, it requires O(n log n) complexity.
   * 
   * @throws ConcurrentModificationException
   *           From iterator to indicate fail-fast behavior in the iterator
   * @throws NoSuchElementException
   *           From iterator if .next is called when the iterator does not have
   *           a next element
   * @return an iterator for the keys
   */
  public Iterator<K> keys();

  /**
   * Returns an iterator of the values in the dictionary. The values come out in
   * the same order as the keys. That is, the first value from this iterator is
   * paired with the first value in the key iterator. The iterator supports
   * fail-fast behavior. Because this iterator must sort on the keys before
   * producing results, it requires O(n log n) complexity.
   * 
   * @throws ConcurrentModificationException
   *           From iterator to indicate fail-fast behavior in the iterator
   * @throws NoSuchElementException
   *           From iterator if .next is called when the iterator does not have
   *           a next element
   * @return an iterator for the values
   */
  public Iterator<V> values();
}
