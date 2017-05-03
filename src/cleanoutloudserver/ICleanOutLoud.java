package cleanoutloudserver;

import DBObjects.AnsweredQuiz;
import DBObjects.Camp;
import DBObjects.Comment;
import DBObjects.Message;
import DBObjects.Quiz;
import DBObjects.QuizAnswer;
import DBObjects.User;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoud {
    
    @WebMethod List<Camp> getCamps();
    @WebMethod List<Camp> getCampsSortedInWeight();
    @WebMethod void addCamp(String campName, String token) throws CustomErrorMessage;
    @WebMethod void deleteCamp(String campName, String token);
    @WebMethod void setGarbage(String campName, Float weight,  String token) throws CustomErrorMessage;
    
    @WebMethod String login(String userName, String password) throws Exception;

    
    @WebMethod void createUser(String userName, String password, String camp, String userType, String token) throws CustomErrorMessage;
    @WebMethod void setUser(User user, String token) throws CustomErrorMessage;
    @WebMethod User getUser(String userName, String token) throws CustomErrorMessage;

    @WebMethod List<Message> getWallMessages();
    @WebMethod Message getMessage(int messageId) throws CustomErrorMessage;
    @WebMethod void addMessage(String message, String token) throws CustomErrorMessage;
    
    
    @WebMethod void addComment(String comment, Message message, String token) throws CustomErrorMessage;
    @WebMethod List<Comment> getCommentsForMessage(int messageId) throws CustomErrorMessage;

//    @WebMethod void deleteUser(String userName, String token) throws CustomErrorMessage;
//    @WebMethod Comment getComment(int commentId, String token) throws CustomErrorMessage;    
//    @WebMethod List<Quiz> getQuizzes();
//    @WebMethod Quiz getQuiz(String quizName);
//    @WebMethod void addQuiz(String quizName, String textQuestion, String token) throws CustomErrorMessage;
//    @WebMethod List<QuizAnswer> getQuizAnswers(String quizName);
//    @WebMethod Quiz addQuizAnswers(String quizName);
//    @WebMethod List<AnsweredQuiz> getUsersOfAnsweredQuizzes(String quizName);
//    @WebMethod void addUsersOfAnsweredQuizzes(String quizName, String token); 
}
