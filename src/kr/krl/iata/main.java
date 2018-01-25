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
			System.out.println(" 1. 운항중인 항공기 모두 출력");
			System.out.println(" 2. 종          료");
			System.out.println("=================================");
			System.out.print(">> ");
			Scanner sc = new Scanner(System.in);
			int menu = sc.nextInt();
			
			switch(menu) {
			case 1:
				StringBuilder SB = new StringBuilder();
				String Data = GET(URLSET);
				//System.out.println("=======================================================================================");
				//System.out.println(String.format("%-74s%-40s%-10s%-7s%-8s%-8s%-8s", "편명", "기체", "관제시스템", "기체번호", "출발지", "도착지", "IATA"));
				//System.out.println("=======================================================================================");
				SB.append("=======================================================================================\n");
				SB.append(String.format("%-34s%-20s%-6s%-5s%-6s%-6s%-6s\n", "편명", "기체", "관제시스템", "기체번호", "출발지", "도착지", "IATA"));
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
				System.out.println(String.format("== 총 %d대의 운행정보를 수신 하였습니다. ==", Keys.length));
				SB.append(String.format("== 총 %d대의 운행정보를 수신 하였습니다. ==\n", Keys.length));
				SB.append(String.format("Written on %s", dt.toString()));
				
				System.out.println(SB);
				
				System.out.print("운항정보를 저장 하시겠습니까?(y/n) > ");
				String chk = sc.next().toLowerCase();
				if(chk.equals("y") == true) {
					String env = System.getProperty("user.dir");
					FileWriter fw = new FileWriter(env+"\\data.txt");
					fw.write(SB.toString());
					fw.close();
					System.out.println(env+"\\data.txt 에 운항정보를 기록 하였습니다.");
				}
				
				break;
			case 2:
				System.exit(0);
				break;
			}
		}
	}
	
	public static String GET(URL url) throws IOException {
		
		System.out.println("통신중 입니다.");
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
		System.out.println("완료");
		return response.toString();
	}
	public static void clear() {
		for(int i=0; i<25; i++) {
			System.out.println("");
		}
	}
}
