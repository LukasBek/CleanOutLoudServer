package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Camp;
import DBObjects.Messages;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import DBObjects.Users;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoud {
    
    
//    @WebMethod ArrayList<String> getCamps();
    
    @WebMethod List<Camp> getCamps();
    @WebMethod void addCamp(String campName, String token);
    
    @WebMethod String login(String userName, String password) throws Exception;
//    @WebMethod String loginWithBrugerAutMod(String userName, String password) throws Exception;
    
    @WebMethod void createUser(String userName, String password, String camp, String userType, String token) throws CustomErrorMessage;
    @WebMethod Users getUser(String userName, String token) throws CustomErrorMessage;
    @WebMethod void setUser(Users user, String token) throws CustomErrorMessage;
    
    @WebMethod List<Messages> getWallMessages();
    @WebMethod Messages getMessage(int messageId) throws CustomErrorMessage;
    @WebMethod void addMessage(String message, String token) throws CustomErrorMessage;
    
    @WebMethod void addComment(String comment, Messages message, String token) throws CustomErrorMessage;
    
    @WebMethod List<Quiz> getQuizzes();
    @WebMethod Quiz getQuiz(String quizName);
    @WebMethod void addQuiz(String quizName, String textQuestion, String token) throws CustomErrorMessage;
    @WebMethod List<QuizAnswers> getQuizAnswers(String quizName);
    @WebMethod Quiz addQuizAnswers(String quizName);
    @WebMethod List<AnsweredQuizzes> getUsersOfAnsweredQuizzes(String quizName);
    @WebMethod void addUsersOfAnsweredQuizzes(String quizName, String token);
    
    
    
}
