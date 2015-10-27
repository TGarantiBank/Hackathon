<?php

$gvps = new SimpleXMLElement('<GVPSRequest/>'); //xml elemanı oluşturuyoruz

$gvps->addChild('Mode', 'PROD');
$gvps->addChild('Version', 'v0.01');
$term = $gvps->addChild('Terminal');
$term->addChild('ProvUserID', 'PROVAUT');
$term->addChild('HashData', 'A4BBFB8B305F0E6E899B362A951E4060CDEE5277');
$term->addChild('UserID', 'deneme');
$term->addChild('ID', '10000039');
$term->addChild('MerchantID', 'PROVAUT');
$cust = $gvps->addChild('Customer');
$cust->addChild('IPAddress', '1.1.1.1');
$cust->addChild('EmailAddress', 'aa@b.com');
$card = $gvps->addChild('Card');
$card->addChild('Number', '123');
$card->addChild('ExpireDate', '1212');
$card->addChild('CVV2', '123');
$order = $gvps->addChild('Order');
$order->addChild('OrderID', '123');
$order->addChild('GroupID');
$trans = $gvps->addChild('Transaction');
$trans->addChild('Type', 'sales');
$trans->addChild('InstallmentCnt');
$trans->addChild('Amount', '10000');
$trans->addChild('CurrencyCode', '949');
$trans->addChild('CardholderPresentCode', '0');
$trans->addChild('MotoInd', 'N');

$url = "http://localhost:9999/VPServlet"; //servis adresi

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url); //servis adresini set ediyoruz
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $gvps->asXML()); //xml datasını post ediyoruz
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$data = curl_exec($ch); //servisten dönen sonuç
curl_close($ch);

$xml = simplexml_load_string($data); //dönen datayı xml formatında alıyoruz

//ekrana bastırıyoruz
echo "<br />User Id: " . $xml->Terminal->UserID;
echo "<br /><br />Id: " . $xml->Terminal->ID;
echo "<br /><br />Email Address: " . $xml->Customer->EmailAddress;
echo "<br /><br />IP Address" . $xml->Customer->IPAddress;







