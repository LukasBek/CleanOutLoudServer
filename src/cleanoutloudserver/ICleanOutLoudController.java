package cleanoutloudserver;

import Objects.Camp;
import Objects.Message;
import Objects.Quiz;
import Objects.User;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoudController {
    
    @WebMethod String login(int userName, String password);
    
    @WebMethod int getPoints(String token);
    
    @WebMethod Camp getCamp(String campName);
    
    @WebMethod Quiz getQuiz(String quizId);
    
    @WebMethod User getUser(String username);
    
    @WebMethod Message getMessage(String messageId);
    
    
}
