package org.EncryptSL.blockhunt.Serializables;

import java.util.Map;

public class M {
	public static Object g(Map<String, Object> map, String key, Object def) {
		return (map.getOrDefault(key, def));
	}
}