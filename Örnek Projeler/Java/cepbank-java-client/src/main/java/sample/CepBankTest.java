package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 * CepBank servislerinin Java kodu ile cagrilma orneginin yer aldigi siniftir. 
 * Kodu calistirabilmek icin SoapUI uzerindeki cepbank servislerini Start etmis olmaniz gerekmektedir. 
 * @author ETERATION
 *
 */
public class CepBankTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			// CepBank servisleri Http POST metodu ile cagrilmaktadir.
			// HttpPost nesnesi servis cagirma gorevini yerine getiren nesnedir.
			// Bu nesneye servis URL'inin
			// parametre olarak verilmesi gerekmektedir.
			HttpPost postRequest = new HttpPost(
					"http://localhost:8888/cepbankMobile/getunitinfo.json");

			// Servise gonderilecek JSON datasinin olusturulmasi
			JSONObject requestData = new JSONObject();
			requestData.put("unitType", "C");
			requestData.put("latitude", 36.599104891017426);
			requestData.put("longitude", 30.561593821958457);
			requestData.put("distance", 2);

			// olusacak json datasi ornegi asagida verilmistir

			// {
			// "unitType": "C",
			// "latitude": 36.599104891017426,
			// "longitude": 30.561593821958457,
			// "distance": 2
			// }

			// input datasinin tipinin json oldugunun belirtilmesi
			StringEntity input = new StringEntity(requestData.toJSONString());
			input.setContentType("application/json");

			// postRequest nesnesi ile input nesnesinin iliskilendirilmesi
			postRequest.setEntity(input);

			// request datasinin sunucuya gonderilmesi ve cevabin alinmasi
			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			// sunucudan donen cevabin alinmasi ve String bir degiskenine
			// atanmasi
			// donen cevabin bir ornegi bu metodun altinda verilmistir
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			String responseStr = "";
			while ((output = br.readLine()) != null) {
				responseStr = responseStr + output;
			}

			System.out.println("Output from Server .... \n");
			JSONParser parser = new JSONParser();
			// String icerisindeki response datasinin JSON nesnesine cevrilmesi
			// ve
			// iterate edilerek icerisindeki elemanlarin console ekranina
			// bastirilmasi
			JSONObject responseData = (JSONObject) parser.parse(responseStr);
			JSONArray unitList = (JSONArray) responseData.get("unitList");
			Iterator<JSONObject> iterator = unitList.iterator();
			while (iterator.hasNext()) {
				JSONObject o = iterator.next();
				System.out.println(o.get("code") + " " + o.get("name"));
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		// SERVISTEN DONEN CEVABIN ORNEGI

		// "unitList": [
		// {
		// "code": "01317CRS001",
		// "name": "KEMER BAGLI SUBE         ",
		// "address": "ATATURK CADDESI NO:18/A 07980                     ",
		// "city": "ANTALYA",
		// "town": "KEMER",
		// "model": "NCR BNAY       ",
		// "hasEnv": "H",
		// "hasCoin": "E",
		// "hasCashPayment": "E",
		// "hasUsd": "H",
		// "hasEur": "H",
		// "hasGbp": "H",
		// "latitude": "36.599308",
		// "longitude": "30.561323",
		// "openTime": 0,
		// "closeTime": 0,
		// "hasUsdCashPayment": "E",
		// "unitType": "atm",
		// "distance": 0.03
		// },
		// {
		// "code": 1317,
		// "name": "KEMER BAGLI SUBE",
		// "type": "T",
		// "address1": "Ataturk Caddesi No: 18 / A 07980",
		// "address2": "",
		// "address3": "",
		// "zipCode": 7980,
		// "areaCode": 242,
		// "telephone": "8143398",
		// "fax": "8143506",
		// "cityCode": "007",
		// "townCode": "00014",
		// "cityName": "ANTALYA        ",
		// "townName": "KEMER",
		// "latitude": "36.600335337588",
		// "longitude": "30.560574531555",
		// "openAfternoon": "H",
		// "afternoonHours": "(12:30-13:30)",
		// "unitType": "branch",
		// "distance": 0.16
		// },
		// {
		// "code": "00904CRS056",
		// "name": "KEMER MARINA             ",
		// "address": "KEMER YAT LIMANI LIMAN CADDESI                    ",
		// "city": "ANTALYA",
		// "town": "KEMER",
		// "model": "NCR BNAY       ",
		// "hasEnv": "H",
		// "hasCoin": "E",
		// "hasCashPayment": "E",
		// "hasUsd": "E",
		// "hasEur": "E",
		// "hasGbp": "H",
		// "latitude": "36.599775",
		// "longitude": "30.563361",
		// "openTime": 0,
		// "closeTime": 0,
		// "hasUsdCashPayment": "N",
		// "unitType": "atm",
		// "distance": 0.17
		// },
		// {
		// "code": "00904CRS072",
		// "name": "KEMER OTTIMO             ",
		// "address": "OTTIMO AVM KEMER                                  ",
		// "city": "ANTALYA",
		// "town": "KEMER",
		// "model": "NCR BNAY       ",
		// "hasEnv": "H",
		// "hasCoin": "E",
		// "hasCashPayment": "E",
		// "hasUsd": "E",
		// "hasEur": "E",
		// "hasGbp": "H",
		// "latitude": "36.605408",
		// "longitude": "30.558983",
		// "openTime": 0,
		// "closeTime": 0,
		// "hasUsdCashPayment": "N",
		// "unitType": "atm",
		// "distance": 0.74
		// },
		// {
		// "code": "00352CRS002",
		// "name": "KEMER SUBE-1             ",
		// "address": "YENI MAHALLE DORTYOL BULVARI NO:26 0798           ",
		// "city": "ANTALYA",
		// "town": "KEMER",
		// "model": "NCR BNAY       ",
		// "hasEnv": "H",
		// "hasCoin": "E",
		// "hasCashPayment": "E",
		// "hasUsd": "E",
		// "hasEur": "E",
		// "hasGbp": "H",
		// "latitude": "36.600782",
		// "longitude": "30.552861",
		// "openTime": 0,
		// "closeTime": 0,
		// "hasUsdCashPayment": "N",
		// "unitType": "atm",
		// "distance": 0.8
		// },
		// {
		// "code": "00352CRS004",
		// "name": "KEMER SUBE-2             ",
		// "address": "YENI MAHALLE DORTYOL BUL.                         ",
		// "city": "ANTALYA",
		// "town": "KEMER",
		// "model": "NCR BNAY       ",
		// "hasEnv": "H",
		// "hasCoin": "E",
		// "hasCashPayment": "E",
		// "hasUsd": "H",
		// "hasEur": "H",
		// "hasGbp": "H",
		// "latitude": "36.600782",
		// "longitude": "30.552861",
		// "openTime": 0,
		// "closeTime": 0,
		// "hasUsdCashPayment": "N",
		// "unitType": "atm",
		// "distance": 0.8
		// },
		// {
		// "code": 352,
		// "name": "KEMER",
		// "type": "T",
		// "address1": "Yeni Mahalle Dortyol Bulvari No: 26 07980",
		// "address2": "",
		// "address3": "",
		// "zipCode": 7980,
		// "areaCode": 242,
		// "telephone": "8135350",
		// "fax": "8145564",
		// "cityCode": "007",
		// "townCode": "00014",
		// "cityName": "ANTALYA        ",
		// "townName": "KEMER",
		// "latitude": "36.600301",
		// "longitude": "30.551068",
		// "openAfternoon": "E",
		// "afternoonHours": "(12:30-13:30)",
		// "unitType": "branch",
		// "distance": 0.95
		// }
		// ],
		// "returnMessage": "OK",
		// "returnCode": 0
		// }

	}

}
