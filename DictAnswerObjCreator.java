package lessons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
//������� �� ��������� ������� ������ ��� <��������� ����, �����>
public class DictAnswerObjCreator {
	//���������, ������ �� � ���� ������ ����� �����
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
	//�������� ��������� ����
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
			index[10] = b.indexOf('�');
			index[11] = b.indexOf('�');
			index[12] = b.indexOf('!');
			index[13] = b.indexOf('?');
			index[14] = b.indexOf('\t');
			index[15] = b.indexOf(',');
			index[16] = b.indexOf('#');
			index[17] = b.indexOf('@');
			index[18] = b.indexOf('�');
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
	//������� ������ ��������� ������� (���������)
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
	//��������������� ��������� ������ � ������ ������ ��� <��������� ����, ����>
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
	//����������, ��������� �� ����� � ������ ����-����
	public static boolean isStopWord(String word){
		String stopWords = " � � � � � � �� �� �� �� �� �� ��� ��� ��� ��� ��� ��� ���� ����� �������������� �������������� �������������� �������������� ���� ��� ��� ����� ����� ����� ����� ��� ��� �� ���� ��� ��� ��� ��� ��� ���� ���� ���� ������� ����� ������ ����� ������� ������ ���� ���� ������ ��� ������� ���� ���� ���� ���� ���� ����������� ������������ ����� �������� ������� ��������� �������� ����� ����� ���� ������ ������ ���������� ������� ������� ������ ��� ��� ��� ��� �� ��� ���� ���� ���� ���� ������ ������ ������� ��������� ������ ����� ����� �� �� �� �� �������� �������� ����� ����� ������ � �� ��� ���� ����� ������ ��� ������������ ������������� ������ ������� ����� ��� ���� ������ ������ ������ ������ ����� ����� ���� ��� ��� ���� ���� ���� ������� ���� ����� �� ��� ������ ���� ����� ������� ����� ����� ����� ������ ���� ���� ��� ��� �� ����� � ��� ������� ������� ���� ���� ��� �� �� �� �� �� �� �� �� �� ��� ��� ������ �������� ������ ����� ����� ����� ������ ���������� ������ ����� ������ ���� ��� ��� ��� ���� ����� ������ ����� ������ ����� ������� �� ��� ��� ���������� ����������� �������� ��������� ���� ��� ��� ��� ��� ���� ����� ������ ������ ������ ������������� ����� ������������ ������������� ������ ������� ���� ���� ����� ������ ������ ����� ������ ��� ��� ���� ����� ������ ����� ����� ����� ���� ������ ������� �� � �� �� ��� ��� ��� ��� ��������� ������ ������ ������ ���� ���� ���� ���� ���� ���� ����� ����� ������ ������ ��������� ���� ���� ����� ����� ��� ���������� ����������� ����� ������ ������ ������ ������ ������ ���� ���� ���� ����� ����� � ��� ������� ���� ���� ����� ������� �������� ������� ������� ������� ������� ��� ������ ������ ������ ������ ������� ��� ����� ����� ��� ����� ���� ������ � � � � �� �� �� �� �� ��� ����� ���� ������ ���� ����� ���� ����� ����� ����� ����� ������� ������ ����� ��� ��� ������ ������ ������ ���� ���� ���� ���� ���� ����� ������ ����� ���� ���� ��� ��� ��� ��� ��� ��� ��� ���� ���� ���� ���� ���� ����� ������ ����� ������ ���������� ����������� ����� ������ ����� ���� ���� ���� ������ ������ ���� ������� ���� ���� ����� ������� ����� ������ ���� ���� ������� ������� ������� ��� ����� ����� ����� ����� ����� ��� ��� ���� ��������� ����� ����� ������ ����������� ������������ ����� ������ ������������ ������������� ������� ������ ������� ������� �� �� ��� ��� ��� ��� ��� ���� ���� ����� ���� ����� ����� ���� ���� ���� ���� ����� ����� ���������� ����������� ���� ������ ��� ��� ���� ���� ����� ";
		if(stopWords.indexOf(" "+word+" ")!=-1)
			return true;
		return false;
	}
}

