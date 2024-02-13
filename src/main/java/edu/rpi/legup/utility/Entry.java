package edu.rpi.legup.utility;

public class Entry<K, V> {
  private K key;
  private V value;

  /**
   * Entry Constructor creates a key value pair
   *
   * @param key key
   * @param value value
   */
  public Entry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Gets the key
   *
   * @return key
   */
  public K getKey() {
    return key;
  }

  /**
   * Sets the key
   *
   * @param key key
   */
  public void setKey(K key) {
    this.key = key;
  }

  /**
   * Gets the value
   *
   * @return value
   */
  public V getValue() {
    return value;
  }

  /**
   * Set the value
   *
   * @param value value
   */
  public void setValue(V value) {
    this.value = value;
  }
}
