//
//  ViewController.m
//  GarantiBank Hackhaton
//
//  Created by KEREM BAYDOGAN on 20/10/15.
//  Copyright © 2015 ETERATION. All rights reserved.
//

#import "ViewController.h"
#import "XMLReader.h"
@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // CEP BANK ORNEK SERVIS CAGRISI
    [self executeCepBankExample];
    
    // SANAL POS ORNEK SERVIS CAGRISI
    [self executeVirtualPosExample];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}





- (void) executeCepBankExample {
    
    // CepBank servisleri Http POST metodu ile cagrilmaktadir.
    // Http POST requesti ile beraber gonderecegimiz parametreler asagidaki gibi hazirlanmalidir.
    
    //parametre stringini hazirla, data tipine encode et, daha sonra kullanmak uzere data buyuklugunu not et
    NSString *parameters = [NSString stringWithFormat:@"unitType=C&latitude=36.599104891017426&longitude=30.561593821958457&distance=2"];
    NSData *postData = [parameters dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postData length]];
    
    // NSMutableURLRequest nesnesi servis cagirma gorevinde kullanilan cagri nesnedir.
    // Bu nesneye servis URL'inin ve parametrelerin set edilmesi gerekmektedir.
    
    //requesti olustur, adresi guncelle, http request tipini post olarak guncelle, datanin buyuklugunu ve kendisi guncelle
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:@"http://192.168.2.6:8888/cepbankMobile/getunitinfo.json"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];
    
    
    // NSMutableURLRequest nesnesi bir NSURLSession icinde servise gonderilir ve completionHandler da response handle edilir.
    
    //requestin yasayabilecegi bir oturum olustur, bu oturum icersinde requesti gonder ve tamamlanma asamasinda yanit ile ne yapacagina karar ver.
    NSURLSession *session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    [[session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        //response'u decode et, console a yazdir.
        NSString *responseAsString = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
        NSLog(@"responseAsString : %@", responseAsString);
        
        //response daha kullanisli bir format olan dictionary'e cevir, bunun icin json serialization kullan.
        NSDictionary *responseAsDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:NULL];
        //collection methodlarini kullanarak responsun detaylarina eris, console a yazdir.
        NSArray* unitList = [responseAsDictionary objectForKey:@"unitList"];
        NSDictionary *unit = [unitList objectAtIndex:2];
        NSString *address = [unit objectForKey:@"address"];
        NSLog(@"responseAsDictionary.unitList[2].address : %@", address);
        
    }] resume];
    
}

- (void) executeVirtualPosExample {

    
    // Sanal Pos servisleri Http POST metodu ile cagrilmaktadir.
    // Http POST requesti ile beraber gonderecegimiz parametreler asagidaki gibi hazirlanmalidir.
    
    //parametre xml stringini hazirla, daha sonra kullanmak uzere uzunlugunu not et
    NSString *message = [self xmlParameterStringForRequest];
    NSString *messageLength = [NSString stringWithFormat:@"%lu",(unsigned long)[message length]];
    
    // NSMutableURLRequest nesnesi servis cagirma gorevinde kullanilan cagri nesnedir.
    // Bu nesneye servis URL'inin ve parametrelerin set edilmesi gerekmektedir.
    
    //requesti olustur, adresi guncelle, http request tipini post olarak guncelle, datanin buyuklugunu ve kendisi guncelle, content tipini xml olarak guncelle
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init] ;
    [request setURL:[NSURL URLWithString:@"http://192.168.2.6:9999/VPServlet"]];
    [request setHTTPMethod:@"POST"];
    [request addValue:messageLength forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:[message dataUsingEncoding:NSUTF8StringEncoding]];
    [request addValue:@"application/xml" forHTTPHeaderField: @"Content-Type"];
    
    
    //requestin yasayabilecegi bir oturum olustur, bu oturum icersinde requesti gonder ve tamamlanma asamasinda yanit ile ne yapacagina karar ver.
    NSURLSession *session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    [[session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        //response'u decode et, console a yazdir.
        NSString *responseAsString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"responseAsString : %@", responseAsString);
        
        //response daha kullanisli bir format olan dictionary'e cevir, bunun icin json serialization kullan.
        NSError *xmlReaderError = nil;
        NSDictionary *responseAsDictionary = [XMLReader dictionaryForXMLData:data options:XMLReaderOptionsProcessNamespaces error:&xmlReaderError];

        //collection methodlarini kullanarak responsun detaylarina eris, console a yazdir.
        NSDictionary* gvpsResponse = [responseAsDictionary objectForKey:@"GVPSResponse"];
        NSDictionary* customer = [gvpsResponse objectForKey:@"Customer"];
        NSDictionary* emailAddress = [customer objectForKey:@"EmailAddress"];
        NSLog(@"responseAsDictionary.GVPSResponse.customer.emailAddress : %@", [emailAddress description]);
        
    }] resume];
    

}




- (NSString*) xmlParameterStringForRequest {
    
    NSMutableString* message = [[NSMutableString alloc] init];
    
    [message appendString:@"data=<GVPSRequest>"];
    [message appendString:@"<Mode>PROD</Mode><Version>v0.01</Version>"];
    
    [message appendString:@"<Terminal>"];
    [message appendString:@"<ProvUserID>PROVAUT</ProvUserID>"];
    [message appendString:@"<HashData>A4BBFB8B305F0E6E899B362A951E4060CDEE5277</HashData>"];
    [message appendString:@"<UserID>deneme</UserID><ID>10000039</ID><MerchantID>PROVAUT</MerchantID>"];
    [message appendString:@"</Terminal>"];
    
    [message appendString:@"<Customer><IPAddress>1.1.1.1</IPAddress> <EmailAddress>aa@b.com</EmailAddress> </Customer>"];
    [message appendString:@"<Card><Number>123</Number> <ExpireDate>1212</ExpireDate> <CVV2>123</CVV2> </Card>"];
    [message appendString:@"<Order><OrderID>123</OrderID> <GroupID /> </Order> "];
    
    [message appendString:@"<Transaction>"];
    [message appendString:@"<Type>sales</Type><InstallmentCnt/><Amount>10000</Amount><CurrencyCode>949</CurrencyCode>"];
    [message appendString:@"<CardholderPresentCode>0</CardholderPresentCode><MotoInd>N</MotoInd>"];
    [message appendString:@"</Transaction>"];
    
    [message appendString:@"</GVPSRequest>"];
    
    return message;
    
}







@end
