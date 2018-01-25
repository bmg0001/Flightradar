package kr.krl.iata;

import java.util.Date;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;
import java.io.FileWriter;

public class main {
	public static void main(String[] args) throws IOException{
		URL URLSET = new URL("http://api.esnw.kr/air");
		while(true) {
			System.out.println("=================================");
			System.out.println(" 1. �������� �װ��� ��� ���");
			System.out.println(" 2. ��          ��");
			System.out.println("=================================");
			System.out.print(">> ");
			Scanner sc = new Scanner(System.in);
			int menu = sc.nextInt();
			
			switch(menu) {
			case 1:
				StringBuilder SB = new StringBuilder();
				String Data = GET(URLSET);
				//System.out.println("=======================================================================================");
				//System.out.println(String.format("%-74s%-40s%-10s%-7s%-8s%-8s%-8s", "���", "��ü", "�����ý���", "��ü��ȣ", "�����", "������", "IATA"));
				//System.out.println("=======================================================================================");
				SB.append("=======================================================================================\n");
				SB.append(String.format("%-34s%-20s%-6s%-5s%-6s%-6s%-6s\n", "���", "��ü", "�����ý���", "��ü��ȣ", "�����", "������", "IATA"));
				SB.append("=======================================================================================\n");
				JSONArray array = new JSONArray(Data);
				JSONObject obj = (JSONObject)array.get(0);
				String[] Keys = obj.getNames(obj);
				
				for(String tdata : Keys) {
					JSONArray array2 = obj.getJSONArray(tdata);
					for(int i=0; i<array2.length(); i++) {
						try {
							String tmpstring = String.format("%-7s",array2.getString(i));
							//System.out.print(tmpstring);
							SB.append(tmpstring);
						}
						catch(JSONException ex) {
							//System.out.print("       ");
							SB.append("       ");
						}
						
					}
					//System.out.println("");
					SB.append("\n");
				}
				
				Date dt = new Date();
				System.out.println(String.format("== �� %d���� ���������� ���� �Ͽ����ϴ�. ==", Keys.length));
				SB.append(String.format("== �� %d���� ���������� ���� �Ͽ����ϴ�. ==\n", Keys.length));
				SB.append(String.format("Written on %s", dt.toString()));
				
				System.out.println(SB);
				
				System.out.print("���������� ���� �Ͻðڽ��ϱ�?(y/n) > ");
				String chk = sc.next().toLowerCase();
				if(chk.equals("y") == true) {
					String env = System.getProperty("user.dir");
					FileWriter fw = new FileWriter(env+"\\data.txt");
					fw.write(SB.toString());
					fw.close();
					System.out.println(env+"\\data.txt �� ���������� ��� �Ͽ����ϴ�.");
				}
				
				break;
			case 2:
				System.exit(0);
				break;
			}
		}
	}
	
	public static String GET(URL url) throws IOException {
		
		System.out.println("����� �Դϴ�.");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println("�Ϸ�");
		return response.toString();
	}
	public static void clear() {
		for(int i=0; i<25; i++) {
			System.out.println("");
		}
	}
}
