package com.eteration.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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


public class MainActivity extends Activity {
    //response datalarini tutacak olan string degiskenleri
    private String responseXML = "";
    private String responseJson = "";
    //cesitli UI elemanlari
    private ProgressDialog pd;
    private TextView tv;
    private Button cepBank, sanalPos;
    //servislerin calisacagi IP adresini girmeniz gerekmektedir. 10.0.2.2 adresi, Android emulatoru tarafindan erisilen lokal makinenin adresidir
    private static String hostIP = "http://10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        sanalPos = (Button) findViewById(R.id.button2);
        cepBank = (Button) findViewById(R.id.button1);
        pd = new ProgressDialog(MainActivity.this);

        //XML datasini donduren sanalPos butonu
        sanalPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask myTask = new BackgroundTask() {
                    @Override
                    protected void onPreExecute() {
                        pd.setMessage(getResources().getString(R.string.loading));
                        pd.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        String orderId = "123";
                        //transaction amount degerindeki nokta isaretlerinin kaldirilmasi
                        String amount = getFormattedAmount(123.89);
                        String hash = "A4BBFB8B305F0E6E899B362A951E4060CDEE5277";

                        String requestXML = generateXML("PROVAUT", hash, "PROVAUT", "1.1.1.1",
                                "123", "1212", "123", orderId, amount, "949");

                        //request olarak gonderilen xml console ekranina yazdiriliyor
                        System.out.println(requestXML);
                        String hostAddress = hostIP + ":9999/VPServlet";

                        try {
                            //POST ?stegi atilarak cevap donuyor
                            //Donen ddata XML formatindadir
                            responseXML = createPostRequest(requestXML, hostAddress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        pd.hide();
                        //Cevap konsola yazdiriliyor
                        System.out.println("XML output from Server .... \n");
                        System.out.println(responseXML);
                        //Response datasi Android ekranina yazdiriliyor
                        tv.setText("XML output from Server .... \n" + responseXML);
                    }
                };
                myTask.execute();
            }
        });

        //JSON datasini donduren cepBank butonu
        cepBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask myTask = new BackgroundTask() {
                    @Override
                    protected void onPreExecute() {
                        pd.setMessage(getResources().getString(R.string.loading));
                        pd.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            // CepBank servisleri Http POST metodu ile cagrilmaktadir.
                            // HttpPost nesnesi servis cagirma gorevini yerine getiren nesnedir.
                            // Bu nesneye servis URL'inin
                            // parametre olarak verilmesi gerekmektedir.
                            HttpPost postRequest = new HttpPost(
                                    hostIP + ":8888/cepbankMobile/getunitinfo.json");

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

                            // JSON olarak olusturulan input datasinin stringe cevrilmesi
                            StringEntity input = new StringEntity(requestData.toString());
                            // input datasinin tipinin json oldugunun belirtilmesi
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

                            while ((output = br.readLine()) != null) {
                                responseJson = responseJson + output;
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        pd.hide();

                        try {


                            System.out.println("JSON output from Server .... \n");
                            // String icerisindeki response datasinin JSON nesnesine cevrilmesi
                            // ve
                            // iterate edilerek icerisindeki elemanlarin console ekranina
                            // bastirilmasi

                            // Donen string JSON objesine cevriliyor
                            JSONObject responseData = new JSONObject(responseJson);
                            // JSON objesinden unitList adindaki JSONArray cekiliyor
                            JSONArray unitList = responseData.getJSONArray("unitList");

                            // Cekilen JSONArray icerisinde donuluyor
                            for (int i = 0; i < unitList.length(); i++) {
                                // Array icerisindeki her objeden gerekli alanlar konsola bastiriliyor
                                JSONObject o = unitList.getJSONObject(i);
                                System.out.println(o.get("code") + " " + o.get("name"));
                            }

                            //Response datasi Android ekranina yazdiriliyor
                            tv.setText("JSON output from Server .... \n" + responseJson);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                };
                myTask.execute();
            }
        });


    }


    /**
     * amount alani icerisindeki nokta isaretini kaldiran metod.
     *
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

    //Network islemleri icin kullan?lacak thread sinifi
    public abstract class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

    }


}
