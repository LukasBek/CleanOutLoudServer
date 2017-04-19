
package cleanoutloudserver;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.List;


public class CleanOutLoudKlient {
    
     public static void main(String[] arg) throws Exception {
        URL url = new URL("http://localhost:3769/col?wsdl");
        QName qname = new QName("http://cleanoutloudserver/", "CleanOutLoadImplService");
        Service service = Service.create(url, qname);
        ICleanOutLoud g = service.getPort(ICleanOutLoud.class);
    
        
     }
     
    
}
