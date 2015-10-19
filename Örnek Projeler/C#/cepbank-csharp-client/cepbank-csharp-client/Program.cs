using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;
using Newtonsoft.Json.Linq;

namespace cepbank_csharp_client
{
    class Program
    {
        static void Main(string[] args)
        {
            const string URL = "http://localhost:8888/cepbankMobile/getunitinfo.json";

            JObject data = new JObject();
            data.Add("unitType", "C");
            data.Add("latitude", 36.599104891017426);
            data.Add("longitude", 30.561593821958457);
            data.Add("distance", 2);
            string response = makePostRequest(URL, data.ToString());
            Console.Out.WriteLine(response);
            Console.ReadLine();
        }

        private static string makePostRequest(string url, string data)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = "POST";
            request.ContentType = "application/json";
            request.ContentLength = data.ToString().Length;

            StreamWriter requestWriter = new StreamWriter(request.GetRequestStream(), System.Text.Encoding.ASCII);
            requestWriter.Write(data);
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
    }
}
