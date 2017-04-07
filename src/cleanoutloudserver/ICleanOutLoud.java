package cleanoutloudserver;

import DBObjects.AnsweredQuizzes;
import DBObjects.Quiz;
import DBObjects.QuizAnswers;
import Objects.Camp;
import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface ICleanOutLoud {
    
    
    @WebMethod Camp getCamp(String campName);
    
    @WebMethod String login(String userName, String password) throws Exception;
    
    @WebMethod Quiz getQuiz();
    
    @WebMethod QuizAnswers getQuizAnswers(String quizName);
    
    @WebMethod AnsweredQuizzes getUsersOfAnsweredQuizzes(String quizName);
    
    
    
    
    
    
    
}
