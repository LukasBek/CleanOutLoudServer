package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Camps;
import DBObjects.Messages;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import DBObjects.Users;
import Objects.Camp;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoud {
    
    
//    @WebMethod ArrayList<String> getCamps();
    
    @WebMethod List<Camps> getCamps();
    
    @WebMethod String login(String userName, String password) throws Exception;
    
    @WebMethod String loginWithBrugerAutMod(String userName, String password) throws Exception;
    
    @WebMethod void createUser(String userName, String password, String camp, String userType, String token) throws CustomErrorMessage;
    
    @WebMethod void editUser(Users user, String token);
    
    @WebMethod List<Messages> getWallMessages();
    
    @WebMethod void addMessage(String message, String token);
    
    @WebMethod void addComment(String comment, Messages message, String token);
    
    @WebMethod ArrayList<Quiz> getQuizzes();
    
    @WebMethod QuizAnswers getQuizAnswers(String quizName);
    
    @WebMethod AnsweredQuizzes getUsersOfAnsweredQuizzes(String quizName);
    
    
    
    
    
    
    
}
