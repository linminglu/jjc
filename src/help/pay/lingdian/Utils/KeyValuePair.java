package help.pay.lingdian.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class KeyValuePair<K extends Comparable<K>,V extends Comparable<V>> 
	implements		Comparable<KeyValuePair<K, V>>
{
  private K key;
  private List<V> valueList = new ArrayList(1);

  public KeyValuePair(K key, V[] value) {
    this.key = key;
    if (value != null)
      this.valueList.addAll(Arrays.asList(value));
  }

  public KeyValuePair(K key, V value)
  {
    this.key = key;
    if (value != null)
      this.valueList.add(value);
  }

  public KeyValuePair(K key, Collection<V> value)
  {
    this.key = key;
    if (value != null)
      this.valueList.addAll(value);
  }

  public K getKey()
  {
    return this.key;
  }

  public List<V> getValueList() {
    return this.valueList;
  }

  public Iterator<V> getValueIterator() {
    return this.valueList.iterator();
  }

  public int getValueSize() {
    return this.valueList.size();
  }

  public V getValue(int i) {
    return this.valueList.get(i);
  }

  public V getValue() {
    return this.valueList.size() == 0 ? null : this.valueList.get(0);
  }

  public int size() {
    return this.valueList.size();
  }

  public void setValue(V[] value) {
    this.valueList.clear();
    this.valueList.addAll(Arrays.asList(value));
  }

  public void setValue(V value) {
    this.valueList.clear();
    this.valueList.add(value);
  }

  public void addValue(V value) {
    this.valueList.add(value);
  }

  public void addValue(V[] value) {
    this.valueList.addAll(Arrays.asList(value));
  }

  public int compareTo(KeyValuePair<K, V> o) {
    if (o == null) return 1;
    if (getKey() == null) return -1;
    return getKey().compareTo(o.getKey());
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append(getKey());
    buf.append("=[");
    buf.append(getValueList());
    buf.append("];");
    return buf.toString();
  }

  public String toParameter() {
    StringBuffer buf = new StringBuffer();
    int i = 0;
    for (Object v : getValueList()) {
      if (i != 0) {
        buf.append("&");
      }
      buf.append(getKey()).append("=").append(v);
      i++;
    }
    return buf.toString();
  }
}