package lessons;

import java.util.Hashtable;
import java.util.Map;

//содержит значения условных вероятностей для события и условия, то есть представляет собой P(event|condition)
public class ConditionalProbabilityPool {
	private Map<String,Map<String, Double>> data;
	
	ConditionalProbabilityPool(){
		data = new Hashtable<String,Map<String, Double>>();		
	}
	//кладет значение условной вероятности в себя
	public void put(String event, String condition, double probability){
		
		if(data.containsKey(event)){
			data.get(event).put(condition, probability);
		}else{
			data.put(event, new Hashtable<String, Double>());
			data.get(event).put(condition, probability);
		}		
		
	}
	//отдает значение условной вероятности
	public double get(String event, String condition){
		if(data.containsKey(event)){
			if(data.get(event).containsKey(condition))
				return data.get(event).get(condition);
		}
		
		return 0;
	}
	
}
