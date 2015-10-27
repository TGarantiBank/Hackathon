<?php
$service_url = "http://localhost:8888/cepbankMobile/sendMoneyToPhone.json"; //servisin adresi
$ch = curl_init($service_url); //initialize ediliyor

//data initialization
$data = array("applicationName"=> "CepBank",
    "timestamp"=> "2015-08-24-14.11.02.333",
    "targetPhoneNum"=> "22",
    "msisdnToken"=> "",
    "tranAmount"=> 11,
    "operatingSystemType"=> "IOS",
    "deviceName"=> "iPhone5,2",
    "deviceToken"=> "unique gt id",
    "crackFlag"=> "N",
    "hashKey"=> "for security",
    "userPhoneNum"=> "",
    "deviceId"=> "aaaaaid",
    "pin"=> "****",
    "operatingSystemName"=> "iPhone OS 9.0");                                                                    
$data_string = json_encode($data); //datayı json'a encode ediyoruz                                                                         
                                                                  
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST"); //post isteği                                                                  
curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string); //json datasını gönderiyoruz                                                       
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);                                                                      
curl_setopt($ch, CURLOPT_HTTPHEADER, array( //içerik tipini json olarak belirliyoruz                                                                     
    'Content-Type: application/json',                                                                                
    'Content-Length: ' . strlen($data_string))                                                                       
);  

$result = curl_exec($ch); //servisten dönen sonuç
curl_close($curl);
 
var_dump($result); //ekrana yazdırma

