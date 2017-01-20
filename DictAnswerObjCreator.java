package lessons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
//создает из обучающей выборки массив пар <множество слов, класс>
public class DictAnswerObjCreator {
	//проверяет, хранит ли в себе строка целое число
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	//получает множество слов
	public static HashSet<String> getDict(String x){
		HashSet<String> a = new HashSet<String>(100);
		String b = x.toLowerCase();
		b = b.replace("0", "")
				.replace("1", "")
				.replace("2", "")
				.replace("3", "")
				.replace("4", "")
				.replace("5", "")
				.replace("6", "")
				.replace("7", "")
				.replace("8", "")
				.replace("9", "");
		while(b.length()>0){
			int[] index = new int[22];
			index[0] = b.indexOf('"');
			index[1] = b.indexOf('.');
			index[2] = b.indexOf(' ');
			index[3] = b.indexOf('-');
			index[4] = b.indexOf('/');
			index[5] = b.indexOf('(');
			index[6] = b.indexOf(')');
			index[7] = b.indexOf(':');
			index[8] = b.indexOf(';');
			index[9] = b.indexOf('\'');
			index[10] = b.indexOf('«');
			index[11] = b.indexOf('»');
			index[12] = b.indexOf('!');
			index[13] = b.indexOf('?');
			index[14] = b.indexOf('\t');
			index[15] = b.indexOf(',');
			index[16] = b.indexOf('#');
			index[17] = b.indexOf('@');
			index[18] = b.indexOf('—');
			index[19] = b.indexOf('?');
			index[20] = b.indexOf('[');
			index[21] = b.indexOf(']');
			
			int minInd = b.length();
			for(int i=0;i<index.length;i++){
				if((index[i]<minInd)&&(index[i]>-1))
					minInd = index[i];
			}
			
			if(minInd == 0)
				b = b.substring(1);
			else{
				if(isInteger(b.substring(0,minInd))){
					a.add("NUMBER");
				}else{
					if(minInd>3 && !isStopWord(b.substring(0,minInd)))
						a.add(b.substring(0,minInd-1));
				}
				b = b.substring(minInd);	
			}	
			
		}
		return a;
	}
	//считает размер обучающей выборки (построчно)
	public static int countTexts(String addr){
		String  thisLine = null;
		int count = 0;
		try{
			FileInputStream fis =  new FileInputStream(addr);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			while ((thisLine = br.readLine()) != null) {
				count++;
			}       
			br.close();
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	//непосредственно считывает тексты и отдает массив пар <множество слов, тема>
	static DictAnswerObj[] giveObjects(){
		String addr = "C:\\ML\\news_train.txt";
		
		int count = countTexts(addr);		
		DictAnswerObj[] array = new DictAnswerObj[count];
		
		String  thisLine = null;
		int i = 0;
		try{
			FileInputStream fis =  new FileInputStream(addr);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			while ((thisLine = br.readLine()) != null) {
				int index = thisLine.indexOf('\t');
				array[i] = new DictAnswerObj();
				array[i].answer = thisLine.substring(0,index);
				String text = thisLine.substring(index+1);
				array[i].dictionary = getDict(text);
				i++;
			}       
			br.close();
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return array;
	}
	//определяет, находится ли слово в списке стоп-слов
	public static boolean isStopWord(String word){
		String stopWords = " а е и ж м о на не ни об но он мне мои мож она они оно мной много многочисленное многочисленная многочисленные многочисленный мною мой мог могут можно может можхо мор моя моё мочь над нее оба нам нем нами ними мимо немного одной одного менее однажды однако меня нему меньше ней наверху него ниже мало надо один одиннадцать одиннадцатый назад наиболее недавно миллионов недалеко между низко меля нельзя нибудь непрерывно наконец никогда никуда нас наш нет нею неё них мира наша наше наши ничего начала нередко несколько обычно опять около мы ну нх от отовсюду особенно нужно очень отсюда в во вон вниз внизу вокруг вот восемнадцать восемнадцатый восемь восьмой вверх вам вами важное важная важные важный вдали везде ведь вас ваш ваша ваше ваши впрочем весь вдруг вы все второй всем всеми времени время всему всего всегда всех всею всю вся всё всюду г год говорил говорит года году где да ее за из ли же им до по ими под иногда довольно именно долго позже более должно пожалуйста значит иметь больше пока ему имя пор пора потом потому после почему почти посреди ей два две двенадцать двенадцатый двадцать двадцатый двух его дел или без день занят занята занято заняты действительно давно девятнадцать девятнадцатый девять девятый даже алло жизнь далеко близко здесь дальше для лет зато даром первый перед затем зачем лишь десять десятый ею её их бы еще при был про процентов против просто бывает бывь если люди была были было будем будет будете будешь прекрасно буду будь будто будут ещё пятнадцать пятнадцатый друго другое другой другие другая других есть пять быть лучше пятый к ком конечно кому кого когда которой которого которая которые который которых кем каждое каждая каждые каждый кажется как какой какая кто кроме куда кругом с т у я та те уж со то том снова тому совсем того тогда тоже собой тобой собою тобою сначала только уметь тот тою хорошо хотеть хочешь хоть хотя свое свои твой своей своего своих свою твоя твоё раз уже сам там тем чем сама сами теми само рано самом самому самой самого семнадцать семнадцатый самим самими самих саму семь чему раньше сейчас чего сегодня себе тебе сеаой человек разве теперь себя тебя седьмой спасибо слишком так такое такой такие также такая сих тех чаще четвертый через часто шестой шестнадцать шестнадцатый шесть четыре четырнадцать четырнадцатый сколько сказал сказала сказать ту ты три эта эти что это чтоб этом этому этой этого чтобы этот стал туда этим этими рядом тринадцать тринадцатый этих третий тут эту суть чуть тысяч ";
		if(stopWords.indexOf(" "+word+" ")!=-1)
			return true;
		return false;
	}
}

