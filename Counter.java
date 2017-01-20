package lessons;

import java.util.Hashtable;
import java.util.Map;

/*
 * класс, производящий подсчет неких строк (обозначающих названия тем или слова)
 * */
public class Counter {
	private Map<String,Integer> data;
	
	public Counter(){
		data = new Hashtable<String, Integer>();
	}
	
	public void count(String key){
		if(data.containsKey(key)){
			Integer toPut = data.get(key).intValue() + 1;
			data.put(key, toPut);
		}else{
			data.put(key, new Integer(1));
		}		
	}
	
	public int getAmountOf(String key){
		Integer retrieved = data.get(key);
		int amount = 0;
		if(retrieved != null)
			amount = retrieved.intValue();
		return amount;
	}
	
	public String[] getKeysArray(){
		String[] arr = new String[data.keySet().size()];
		
		int i = 0;
		for(String key: data.keySet()){
			arr[i] = key;
			i++;
		}
		
		return arr;
	}
	
}
