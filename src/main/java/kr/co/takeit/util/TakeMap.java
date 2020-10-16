package kr.co.takeit.util;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;

public class TakeMap<K,V> extends LinkedHashMap<K,V> {
	private static final long serialVersionUID = 1L;

	public V put(K key, V value) {
        return super.put((K) StringUtils.lowerCase((String) key), value);
    }

}
