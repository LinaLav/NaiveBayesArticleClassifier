package lessons;

import java.util.HashSet;
import java.util.Hashtable;

public class Main {
	
	public static void main(String[] args) {
		// ������ �������� ���� "��������� ���� - �����"
		DictAnswerObj[] xy = DictAnswerObjCreator.giveObjects(); 
		
		//����� ������� ���� ���� � ������� (� ����� ���������)
		//� ��������� ���� �������������� ���
		HashSet<String> dictionary = new HashSet<String>(100000);
		HashSet<String> themes= new HashSet<String>(12);
		
		//��������� ����� �������
		for(int i = 0; i < xy.length; i++){
			dictionary.addAll(xy[i].dictionary);
			themes.add(xy[i].answer);
		}
		System.out.println(dictionary.size());
		
		System.out.println("������� ���������");
		
		//�������� �������� �����������: ����������� ������� ����� w ��� ���� t
		ConditionalProbabilityPool pwt = new ConditionalProbabilityPool();
		
		//������� ��� ������ � ����� t ��� ������ t
		Counter themeCounter = new Counter();
		for(int i = 0; i < xy.length; i++)
			themeCounter.count(xy[i].answer);
		
		//��� ������� ����� ���� ���� ������� �������, ���������� ��� �����, � ����� t (��� ������ t)
		Hashtable<String,Counter> themeWithWordCounter = new Hashtable<String,Counter>();
		
		//������� ��� ������ � ����� t � ������ w ��� ����� t � w
		for(String word : dictionary)
			themeWithWordCounter.put(word, new Counter());
		
		for(int i = 0; i < xy.length; i++)
			for(String word : xy[i].dictionary)
				themeWithWordCounter.get(word).count(xy[i].answer);
		
		//���������� ������ ���� �� ��������: ���� �������� ����� �� �������, ������������� ����� �����
		HashSet<String> toDelete = new HashSet<String>();
		
		for(String word : dictionary){
			int sum = 0;
			for(String theme: themes)
				sum += themeWithWordCounter.get(word).getAmountOf(theme);
			if(sum<50)
				toDelete.add(word);
		}
		
		//������� �����
		for(String word : toDelete){
			dictionary.remove(word);
			themeWithWordCounter.remove(word);
		}
		
		
		System.out.println("������ ���������");
		
		//������� pwt: p(word|theme) = (���-�� ������� �� ������ word, ������������� ���� theme)/(���-�� ������� � ����� theme)
		for(String word : dictionary){				
			for(String theme: themes){
				pwt.put(word, 
						theme, 
						(double)themeWithWordCounter.get(word).getAmountOf(theme)
						/
						(double)themeCounter.getAmountOf(theme));
			}
			
		}		

		System.out.println("pwt ���������");
		
		//ptw �������� ����������� ����, ��� � ��� ���� theme, ���� �� ������� ����� word
		ConditionalProbabilityPool ptw = new ConditionalProbabilityPool();
		//��������� �� ���� ������ � �����
		for(String theme: themes){
			for(String word : dictionary){
				//������ ����� �����
				double divider = 0;
				for(String t:themes)						
					divider += ((double)themeCounter.getAmountOf(t)
							/
							(double)xy.length)
							*
							pwt.get(word, t);
				//��� ��������� ������ ����������, divider ������ ����� 0, �� ����� �� ����������, ���� ������ ����� ������ �����
				if(divider == 0)
					System.out.println("ZERO");
				//(������� ����� �����)/(������ ����� �����)
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
		//������ �������������������� ������ �� ������ � �������������� ��, ���������� ������ � ����
		
		QAHandler handler = new QAHandler();
		handler.init();
		handler.clearAnswers();
		
		String q;
		//������� �������������������� ������
		int top = handler.countQuestions();
		//������������:
		for(int k = 0; k < top; k++){
			//�������� ��������� ���� �� ������������ ������
			HashSet<String> text = DictAnswerObjCreator.getDict(handler.readQuestion());
			//������� ��� ������������� � ��������� ������� ����� � ������� ����������� text � dict 
			text.retainAll(dictionary);
			//������� ���������� ����������� ������ ���� ��� ������
			String[] themeArray = themes.toArray(new String[themes.size()]);
			double[] themeProbability = new double[themes.size()];
			for(int i = 0; i < themeArray.length; i++)
				themeProbability[i] = 0;
			for(String word: text){
				for(int i = 0; i < themeArray.length; i++){
					themeProbability[i] += Math.log(ptw.get(themeArray[i], word));
				}
			}
			//������� ������������ �����������	
			int max = 0;
			
			for(int i = 0; i < themeProbability.length; i++){
				if(themeProbability[i]>themeProbability[max])
					max = i;
			}
			//����, ��������������� ������������ ����������� = �����
			handler.writeAnswer(themeArray[max]);
			if(k%10==0)
				System.out.println(k);
		}
	}

}
