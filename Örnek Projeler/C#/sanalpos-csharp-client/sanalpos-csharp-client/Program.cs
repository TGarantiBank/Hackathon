using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Net;
using System.IO;

namespace sanalpos_csharp_client
{
    class Program
    {
        static void Main(string[] args)
        {
            string hostAddress = "http://localhost:9999/VPServlet";
            string orderId = "123";
            string amount = getFormattedAmount(123.89);
            string hash = "A4BBFB8B305F0E6E899B362A951E4060CDEE5277";

            XmlDocument gvpsRequestXmlDocument = createGVPSRequestXmlDocument(
                                                    "PROVAUT", hash, "PROVAUT", "1.1.1.1",
                                                    "123", "1212", "123", orderId, amount, "949");

            string gvpsRequestXmlDocumentStr = "data=" + gvpsRequestXmlDocument.OuterXml;
            string response = makePostRequest(gvpsRequestXmlDocumentStr, hostAddress);
            Console.Out.WriteLine(response);
            Console.ReadLine();
        }
 
        private static string makePostRequest(string gvpsRequestXmlDocument, string hostAddress)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(hostAddress);
            request.Method = "POST";
            request.ContentType = "application/xml";
            request.ContentLength = gvpsRequestXmlDocument.ToString().Length;

            StreamWriter requestWriter = new StreamWriter(request.GetRequestStream(), System.Text.Encoding.ASCII);
            requestWriter.Write(gvpsRequestXmlDocument);
            requestWriter.Close();

            try
            {
                WebResponse webResponse = request.GetResponse();
                Stream webStream = webResponse.GetResponseStream();
                StreamReader responseReader = new StreamReader(webStream);
                string response = responseReader.ReadToEnd();
                responseReader.Close();

                return response;
            }
            catch (Exception e)
            {
                return e.Message;
            }
        }

        private static string getFormattedAmount(double amount)
        {
            return Decimal.Parse(Convert.ToString(amount)).ToString(".##").Replace(",", ".");
        }

        private static XmlDocument createGVPSRequestXmlDocument(
            string userName, string hash, string userID, string ipAddress,
            string paymentToolNumber, string expDate, string cvc,
            string orderID, string amount, string currency)
        {
            XmlDocument xmlDocument = new XmlDocument();
            XmlElement rootElement = xmlDocument.CreateElement("GVPSRequest");
            xmlDocument.AppendChild(rootElement);

            XmlElement modeElement = xmlDocument.CreateElement("Mode");
            modeElement.InnerText = "PROD";
            rootElement.AppendChild(modeElement);

            XmlElement versionElement = xmlDocument.CreateElement("Version");
            versionElement.InnerText = "v0.01";
            rootElement.AppendChild(versionElement);

            // Terminal elementi ve alt elementleri
            XmlElement terminalElement = xmlDocument.CreateElement("Terminal");
            rootElement.AppendChild(terminalElement);

            XmlElement provUserIdElement = xmlDocument.CreateElement("ProvUserID");
            provUserIdElement.InnerText = "PROVAUT";
            terminalElement.AppendChild(provUserIdElement);

            XmlElement hashDataElement = xmlDocument.CreateElement("HashData");
            hashDataElement.InnerText = hash;
            terminalElement.AppendChild(hashDataElement);

            XmlElement userIDElement = xmlDocument.CreateElement("UserID");
            userIDElement.InnerText = "deneme";
            terminalElement.AppendChild(userIDElement);

            XmlElement idElement = xmlDocument.CreateElement("ID");
            idElement.InnerText = "10000039";
            terminalElement.AppendChild(idElement);

            XmlElement merchantIDElement = xmlDocument.CreateElement("MerchantID");
            merchantIDElement.InnerText = userID;
            terminalElement.AppendChild(merchantIDElement);

            // Customer elementi ve alt elementleri
            XmlElement customerElement = xmlDocument.CreateElement("Customer");
            rootElement.AppendChild(customerElement);

            XmlElement ipAddressElement = xmlDocument.CreateElement("IPAddress");
            ipAddressElement.InnerText = ipAddress;
            customerElement.AppendChild(ipAddressElement);

            XmlElement emailAddressElement = xmlDocument.CreateElement("EmailAddress");
            emailAddressElement.InnerText = "aa@b.com";
            customerElement.AppendChild(emailAddressElement);

            // Card elementi ve alt elementleri
            XmlElement cardElement = xmlDocument.CreateElement("Card");
            rootElement.AppendChild(cardElement);

            XmlElement numberElement = xmlDocument.CreateElement("Number");
            numberElement.InnerText = paymentToolNumber;
            cardElement.AppendChild(numberElement);

            XmlElement expireDateElement = xmlDocument.CreateElement("ExpireDate");
            expireDateElement.InnerText = "1212";
            cardElement.AppendChild(expireDateElement);

            XmlElement cvv2Element = xmlDocument.CreateElement("CVV2");
            cvv2Element.InnerText = cvc;
            cardElement.AppendChild(cvv2Element);


            // Order elementi ve alt elementleri
            XmlElement orderElement = xmlDocument.CreateElement("Order");
            rootElement.AppendChild(orderElement);

            XmlElement orderIdElement = xmlDocument.CreateElement("OrderID");
            orderIdElement.InnerText = orderID;
            orderElement.AppendChild(orderIdElement);

            XmlElement groupIdElement = xmlDocument.CreateElement("GroupID");
            groupIdElement.InnerText = "";
            orderElement.AppendChild(groupIdElement);


            // Transaction elementi ve alt elementleri
            XmlElement transactionElement = xmlDocument.CreateElement("Transaction");
            rootElement.AppendChild(transactionElement);

            XmlElement typeElement = xmlDocument.CreateElement("Type");
            typeElement.InnerText = "sales";
            transactionElement.AppendChild(typeElement);

            XmlElement installmentCntElement = xmlDocument.CreateElement("InstallmentCnt");
            installmentCntElement.InnerText = "";
            transactionElement.AppendChild(installmentCntElement);

            XmlElement amountElement = xmlDocument.CreateElement("Amount");
            amountElement.InnerText = amount;
            transactionElement.AppendChild(amountElement);

            XmlElement currencyCodeElement = xmlDocument.CreateElement("CurrencyCode");
            currencyCodeElement.InnerText = currency;
            transactionElement.AppendChild(currencyCodeElement);

            XmlElement cardholderPresentCodeElement = xmlDocument.CreateElement("CardholderPresentCode");
            cardholderPresentCodeElement.InnerText = "0";
            transactionElement.AppendChild(cardholderPresentCodeElement);

            XmlElement motoIndElement = xmlDocument.CreateElement("MotoInd");
            motoIndElement.InnerText = "N";
            transactionElement.AppendChild(motoIndElement);

            return xmlDocument;
        }
    }
}
