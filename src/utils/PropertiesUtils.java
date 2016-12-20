package utils;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertiesUtils {


		/**
		 * @param properties
		 */
		private static void showKeys(Properties properties) {
			Enumeration<?> enu = properties.propertyNames();
			while (enu.hasMoreElements()) {
				Object key = enu.nextElement();
				System.out.println(key);
			}
		}

		/**
		 * @param properties
		 */
		private static void showValues(Properties properties) {
			Enumeration<Object> enu = properties.elements();
			while (enu.hasMoreElements()) {
				Object value = enu.nextElement();
				System.out.println(value);
			}
		}

		/**
		 * @param properties
		 */
		private static void showKeysAndValues(Properties properties) {
			Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("key   :" + key);
				System.out.println("value :" + value);
				System.out.println("---------------");
			}
		}


}
