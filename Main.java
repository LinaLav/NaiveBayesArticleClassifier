package lessons;

import java.util.HashSet;
import java.util.Hashtable;

public class Main {
	
	public static void main(String[] args) {
		// массив объектов типа "множество слов - ответ"
		DictAnswerObj[] xy = DictAnswerObjCreator.giveObjects(); 
		
		//общий словарь всех слов в текстах (в форме множества)
		//и множество всех представленных тем
		HashSet<String> dictionary = new HashSet<String>(100000);
		HashSet<String> themes= new HashSet<String>(12);
		
		//заполняем общий словарь
		for(int i = 0; i < xy.length; i++){
			dictionary.addAll(xy[i].dictionary);
			themes.add(xy[i].answer);
		}
		System.out.println(dictionary.size());
		
		System.out.println("Словарь составлен");
		
		//содержит условные вероятности: вероятность увидеть слово w при теме t
		ConditionalProbabilityPool pwt = new ConditionalProbabilityPool();
		
		//считаем все тексты с темой t для любого t
		Counter themeCounter = new Counter();
		for(int i = 0; i < xy.length; i++)
			themeCounter.count(xy[i].answer);
		
		//для каждого слова есть свой счетчик текстов, содержащих это слово, с темой t (для любого t)
		Hashtable<String,Counter> themeWithWordCounter = new Hashtable<String,Counter>();
		
		//считаем все тексты с темой t и словом w для любых t и w
		for(String word : dictionary)
			themeWithWordCounter.put(word, new Counter());
		
		for(int i = 0; i < xy.length; i++)
			for(String word : xy[i].dictionary)
				themeWithWordCounter.get(word).count(xy[i].answer);
		
		//составляем список слов на удаление: туда попадают слова из словаря, встречающиеся очень редко
		HashSet<String> toDelete = new HashSet<String>();
		
		for(String word : dictionary){
			int sum = 0;
			for(String theme: themes)
				sum += themeWithWordCounter.get(word).getAmountOf(theme);
			if(sum<50)
				toDelete.add(word);
		}
		
		//удаляем слова
		for(String word : toDelete){
			dictionary.remove(word);
			themeWithWordCounter.remove(word);
		}
		
		
		System.out.println("тексты посчитаны");
		
		//считаем pwt: p(word|theme) = (кол-во текстов со словом word, принадлежащих теме theme)/(кол-во текстов с темой theme)
		for(String word : dictionary){				
			for(String theme: themes){
				pwt.put(word, 
						theme, 
						(double)themeWithWordCounter.get(word).getAmountOf(theme)
						/
						(double)themeCounter.getAmountOf(theme));
			}
			
		}		

		System.out.println("pwt вычислено");
		
		//ptw содержит вероятность того, что у нас тема theme, если мы увидели слово word
		ConditionalProbabilityPool ptw = new ConditionalProbabilityPool();
		//вычисляем по всем словам и темам
		for(String theme: themes){
			for(String word : dictionary){
				//нижняя часть дроби
				double divider = 0;
				for(String t:themes)						
					divider += ((double)themeCounter.getAmountOf(t)
							/
							(double)xy.length)
							*
							pwt.get(word, t);
				//как следствие ошибок округления, divider бывает равен 0, но этого не происходит, если убрать очень редкие слова
				if(divider == 0)
					System.out.println("ZERO");
				//(верхняя часть дроби)/(нижняя часть дроби)
				ptw.put(theme, word, 
						(
							((double)themeCounter.getAmountOf(theme))
							/
							(double)xy.length
						)
						*
						pwt.get(word, theme)
						/
						divider);
			}
		}
		//читаем неклассифицированные тексты по одному и классифицируем их, записываем ответы в файл
		
		QAHandler handler = new QAHandler();
		handler.init();
		handler.clearAnswers();
		
		String q;
		//считаем неклассифицированные тексты
		int top = handler.countQuestions();
		//обрабатываем:
		for(int k = 0; k < top; k++){
			//получаем множество слов из прочитанного текста
			HashSet<String> text = DictAnswerObjCreator.getDict(handler.readQuestion());
			//убираем все отсутствующие в обучающей выборке слова с помощью пересечения text и dict 
			text.retainAll(dictionary);
			//считаем совместную вероятность каждой темы для текста
			String[] themeArray = themes.toArray(new String[themes.size()]);
			double[] themeProbability = new double[themes.size()];
			for(int i = 0; i < themeArray.length; i++)
				themeProbability[i] = 0;
			for(String word: text){
				for(int i = 0; i < themeArray.length; i++){
					themeProbability[i] += Math.log(ptw.get(themeArray[i], word));
				}
			}
			//находим максимальную вероятность	
			int max = 0;
			
			for(int i = 0; i < themeProbability.length; i++){
				if(themeProbability[i]>themeProbability[max])
					max = i;
			}
			//тема, соответствующая максимальной вероятность = ответ
			handler.writeAnswer(themeArray[max]);
			if(k%10==0)
				System.out.println(k);
		}
	}

}
