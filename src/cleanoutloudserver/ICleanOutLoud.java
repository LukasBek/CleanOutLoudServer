package cleanoutloudserver;

import Objects.Camp;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoud {
    
    @WebMethod String login(int userName, String password);
    
    int getPoints(String token);
    
    Camp getCamp(String campName);
    
    
    
    
    
}
