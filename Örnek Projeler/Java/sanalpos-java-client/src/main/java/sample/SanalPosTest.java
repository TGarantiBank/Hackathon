package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * SanalPos servislerinin Java kodu ile cagrilma orneginin yer aldigi siniftir. 
 * Kodu calistirabilmek icin SoapUI uzerindeki sanalpos servislerini Start etmis olmaniz gerekmektedir. 
 * @author ETERATION
 *
 */
public class SanalPosTest {
	/**
	 * Sanal Pos servislerinin adresi bu degiskende tutulur. Bu metodu calistirmadan once SoapUI 
	 * uzerinden sanalpos servislerini calistirmaniz gerekmektedir. 
	 * Eger servisleri start ettiginiz port 9999 dan farkli ise asagidaki URL i degistiriniz.
	 */
	private static String hostAddress = "http://localhost:9999/VPServlet";

	public static void main(String[] args) throws Exception {

		//servise bir XML datasinin POST edilmesi gerekmektedir. POST islemi
		//data=XML formatinda yapilmalidir.
		//Asagida verilen degerler tamamen ornek amaclidir. 
		//Sizin senaryonuza gore degiskenlerin degerlerini degistirmeniz gerekmektedir.
		
		String orderId = "123";
		//transaction amount degerindeki nokta isaretlerinin kaldirilmasi
		String amount = getFormattedAmount(123.89);
		String hash = "A4BBFB8B305F0E6E899B362A951E4060CDEE5277";

		String requestXML = generateXML("PROVAUT", hash, "PROVAUT", "1.1.1.1",
				"123", "1212", "123", orderId, amount, "949");
		
		//request olarak gonderilen xml console ekranina yazdiriliyor		
		System.out.println(requestXML);
		String responseXML = createPostRequest(requestXML, hostAddress);
		
		//response olarak gelen xml console ekranina yazdiriliyor
		System.out.println(responseXML);
//	SERVISE GONDERILER REQUEST XML		
//		<GVPSRequest>
//		<Mode>PROD</Mode>
//		<Version>v0.01</Version>
//		<Terminal>
//			<ProvUserID>PROVAUT</ProvUserID>
//			<HashData>A4BBFB8B305F0E6E899B362A951E4060CDEE5277</HashData>
//			<UserID>deneme</UserID>
//			<ID>10000039</ID>
//			<MerchantID>PROVAUT</MerchantID>
//		</Terminal>
//		<Customer>
//			<IPAddress>1.1.1.1</IPAddress>
//			<EmailAddress>aa@b.com</EmailAddress>
//		</Customer>
//		<Card>
//			<Number>123</Number>
//			<ExpireDate>1212</ExpireDate>
//			<CVV2>123</CVV2>
//		</Card>
//		<Order>
//			<OrderID>123</OrderID>
//			<GroupID />
//		</Order>
//		<Transaction>
//			<Type>sales</Type>
//			<InstallmentCnt />
//			<Amount>10000</Amount>
//			<CurrencyCode>949</CurrencyCode>
//			<CardholderPresentCode>0</CardholderPresentCode>
//			<MotoInd>N</MotoInd>
//		</Transaction>
//	</GVPSRequest>

// SERVISTEN DONEN RESPONSE XML
//		<GVPSResponse>
//		<Mode>PROD</Mode>
//		<Terminal>
//			<ProvUserID>PROVAUT</ProvUserID>
//			<UserID>deneme</UserID>
//			<ID>10000039</ID>
//			<MerchantID>PROVAUT</MerchantID>
//		</Terminal>
//		<Customer>
//			<IPAddress>1.1.1.1</IPAddress>
//			<EmailAddress>aa@b.com</EmailAddress>
//		</Customer>
//		<Order>
//			<OrderID>123</OrderID>
//			<GroupID></GroupID>
//		</Order>
//		<Transaction>
//			<Response>
//				<Source>HOST</Source>
//				<Code>00</Code>
//				<ReasonCode>00</ReasonCode>
//				<Message>Approved</Message>
//				<ErrorMsg></ErrorMsg>
//				<SysErrMsg></SysErrMsg>
//			</Response>
//			<RetrefNum>311710676028</RetrefNum>
//			<AuthCode>245093</AuthCode>
//			<BatchNum>000008</BatchNum>
//			<SequenceNum>000051</SequenceNum>
//			<ProvDate>20130427 10:24:26</ProvDate>
//			<CardNumberMasked>554960******3012</CardNumberMasked>
//			<CardHolderName>PE*** PEL** TON***</CardHolderName>
//			<CardType>BONUS</CardType>
//			<HashData>8B4BCA794C748337183EA62D75F901549541C95C</HashData>
//			<HostMsgList></HostMsgList>
//			<RewardInqResult>
//				<RewardList></RewardList>
//				<ChequeList></ChequeList>
//			</RewardInqResult>
//		</Transaction>
//	</GVPSResponse>

	}

/**
 * amount alani icerisindeki nokta isaretini kaldiran metod. 
 * @param amount
 * @return
 */
	public static String getFormattedAmount(double amount) {
		DecimalFormat df = new DecimalFormat(".00");
		String formattedAmount = df.format(amount).replaceAll(",", "")
				.replaceAll("\\.", "");
		return formattedAmount;
	}

	
	private static String createPostRequest(String request, String hostAddress)
			throws Exception {

		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				System.out.println("Warning: URL Host: " + urlHostName
						+ " vs. " + session.getPeerHost());
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		URL garanti = new URL(hostAddress);
		
		//servis url ine connection acilmasi. request datasi stream olarak aktariliyor.
		URLConnection connection = garanti.openConnection();
		connection.setDoOutput(true);

		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream());
		out.write("data=" + request);
		out.flush();
		out.close();
		
		//sunucudan gelen cevap okunuyor. 
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		
		StringBuilder responseXMLBuilder = new StringBuilder();

		String line;
		//response datasi string icerisine aktariliyor.
		while ((line = in.readLine()) != null)
			responseXMLBuilder.append(line);
		responseXMLBuilder.append("\n");
		in.close();

		return responseXMLBuilder.toString();

	}
	/**
	 * Sunucuya gonderilecek XML datasinin olusturuldugu metod.
	 * xml icerisindeki degerleri parametre olarak alir.
	 * javax.xml.parsers   paketindeki siniflari kullanarak xml olusturur
	 * 
	 * @param userName
	 * @param hash
	 * @param userID
	 * @param ipAddress
	 * @param paymentToolNumber
	 * @param expDate
	 * @param cvc
	 * @param orderID
	 * @param amount
	 * @param currency
	 * @return
	 */
	private static String generateXML(String userName, String hash,
			String userID, String ipAddress, String paymentToolNumber,
			String expDate, String cvc, String orderID, String amount,
			String currency) {

		try {
			// Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			// Create blank DOM Document
			Document doc = docBuilder.newDocument();

			Element root = doc.createElement("GVPSRequest");
			doc.appendChild(root);

			Element Mode = doc.createElement("Mode");
			Mode.appendChild(doc.createTextNode("PROD"));
			root.appendChild(Mode);

			Element Version = doc.createElement("Version");
			Version.appendChild(doc.createTextNode("v0.01"));
			root.appendChild(Version);

			Element Terminal = doc.createElement("Terminal");
			root.appendChild(Terminal);

			Element ProvUserID = doc.createElement("ProvUserID");
			// ProvUserID.appendChild(doc.createTextNode(userName));
			ProvUserID.appendChild(doc.createTextNode("PROVAUT"));
			Terminal.appendChild(ProvUserID);

			Element HashData_ = doc.createElement("HashData");
			HashData_.appendChild(doc.createTextNode(hash));
			Terminal.appendChild(HashData_);

			Element UserID = doc.createElement("UserID");
			UserID.appendChild(doc.createTextNode("deneme"));
			Terminal.appendChild(UserID);

			Element ID = doc.createElement("ID");
			ID.appendChild(doc.createTextNode("10000039"));
			Terminal.appendChild(ID);

			Element MerchantID = doc.createElement("MerchantID");
			MerchantID.appendChild(doc.createTextNode(userID));
			Terminal.appendChild(MerchantID);

			Element Customer = doc.createElement("Customer");
			root.appendChild(Customer);

			Element IPAddress = doc.createElement("IPAddress");
			IPAddress.appendChild(doc.createTextNode(ipAddress));
			Customer.appendChild(IPAddress);

			Element EmailAddress = doc.createElement("EmailAddress");
			EmailAddress.appendChild(doc.createTextNode("aa@b.com"));
			Customer.appendChild(EmailAddress);

			Element Card = doc.createElement("Card");
			root.appendChild(Card);

			Element Number = doc.createElement("Number");
			Number.appendChild(doc.createTextNode(paymentToolNumber));
			Card.appendChild(Number);

			Element ExpireDate = doc.createElement("ExpireDate");
			ExpireDate.appendChild(doc.createTextNode("1212"));
			Card.appendChild(ExpireDate);

			Element CVV2 = doc.createElement("CVV2");
			CVV2.appendChild(doc.createTextNode(cvc));
			Card.appendChild(CVV2);

			Element Order = doc.createElement("Order");
			root.appendChild(Order);

			Element OrderID = doc.createElement("OrderID");
			OrderID.appendChild(doc.createTextNode(orderID));
			Order.appendChild(OrderID);

			Element GroupID = doc.createElement("GroupID");
			GroupID.appendChild(doc.createTextNode(""));
			Order.appendChild(GroupID);

			/*
			 * Element Description=doc.createElement("Description");
			 * Description.appendChild(doc.createTextNode(""));
			 * Order.appendChild(Description);
			 */

			Element Transaction = doc.createElement("Transaction");
			root.appendChild(Transaction);

			Element Type = doc.createElement("Type");
			Type.appendChild(doc.createTextNode("sales"));
			Transaction.appendChild(Type);

			Element InstallmentCnt = doc.createElement("InstallmentCnt");
			InstallmentCnt.appendChild(doc.createTextNode(""));
			Transaction.appendChild(InstallmentCnt);

			Element Amount = doc.createElement("Amount");
			Amount.appendChild(doc.createTextNode(amount));
			Transaction.appendChild(Amount);

			Element CurrencyCode = doc.createElement("CurrencyCode");
			CurrencyCode.appendChild(doc.createTextNode(currency));
			Transaction.appendChild(CurrencyCode);

			Element CardholderPresentCode = doc
					.createElement("CardholderPresentCode");
			CardholderPresentCode.appendChild(doc.createTextNode("0"));
			Transaction.appendChild(CardholderPresentCode);

			Element MotoInd = doc.createElement("MotoInd");
			MotoInd.appendChild(doc.createTextNode("N"));
			Transaction.appendChild(MotoInd);

			// Convert dom to String
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			aTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			aTransformer
					.transform(new DOMSource(doc), new StreamResult(buffer));
			return buffer.toString();

		} catch (Exception e) {
			return null;
		}

	}

}
