package lessons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
/*
 * ������ �������������������� ������, 
 * ����� ���������� �������������
 * 
 * */
public class QAHandler {
	int questionIndex = 0;
	BufferedReader reader;
	
	//��������� ���� � ��������, ��������� ��� ���������, ����� ��� ������ ���������� �����
	public int countQuestions(){
		String addr = "C:\\ML\\news_test.txt";
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
	public void init(){
		//������� ����� ������, ��������� ��� � ��������� ����������,
		//��� ��� ��������� ��� ������ ��� ��������
		String addr = "C:\\ML\\news_test.txt";
		FileInputStream fis;
		try {
			fis = new FileInputStream(addr);
			reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String readQuestion(){
		try {//������ ������ �� ������ ������
			return reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void clearAnswers(){
		//��������� � ��������� ����� ������, ���� ��� ���������
		String addr = "C:\\ML\\answers.txt";
		try{
			FileOutputStream fos =  new FileOutputStream(addr);			
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeAnswer(String answer){
		String addr = "C:\\ML\\answers.txt";
		//��������� ����� ������, ������� ���� ����� � ������� ������, ��������� �����.
		try{
			FileOutputStream fos =  new FileOutputStream(addr, true);
			OutputStreamWriter writer = new OutputStreamWriter(fos);
			
			writer.append(answer + '\n');
			
			writer.close();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
