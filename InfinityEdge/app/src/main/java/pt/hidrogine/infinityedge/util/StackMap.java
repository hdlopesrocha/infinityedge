package pt.hidrogine.infinityedge.util;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


public class StackMap<K, V> {


    private TreeMap<K, LinkedList<V>> treeMap = new TreeMap<K, LinkedList<V>>();

    public StackMap() {

    }

    public void push(K k, V v) {
        LinkedList<V> s = treeMap.get(k);
        if (s == null) {
            s = new LinkedList<V>();
            treeMap.put(k, s);
        }
        s.addLast(v);
    }

    public V pop(K k) {
        LinkedList<V> s = treeMap.get(k);
        V v = s.removeFirst();
        if (s.size() <= 0)
            treeMap.remove(k);
        return v;
    }

    public V pop() {

        Map.Entry<K, LinkedList<V>> entry = treeMap.firstEntry();
        LinkedList<V> s = entry.getValue();
        V v = s.removeFirst();
        if (s.isEmpty())
            treeMap.remove(entry.getKey());
        return v;
    }

    public V remove() {

        Map.Entry<K, LinkedList<V>> entry = treeMap.lastEntry();
        LinkedList<V> s = entry.getValue();
        V v = s.removeFirst();
        if (s.isEmpty())
            treeMap.remove(entry.getKey());
        return v;
    }

    public int size() {
        return treeMap.size();
    }
}
