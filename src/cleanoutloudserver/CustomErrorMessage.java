/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanoutloudserver;

/**
 *
 * @author Nicki
 */
public class CustomErrorMessage extends Exception {
    
    public CustomErrorMessage()
    {
    }
    
    public CustomErrorMessage(String message)
    {
        super(message);
    }
    
    public CustomErrorMessage(Throwable cause)
    {
        super(cause);
    }
    
    public CustomErrorMessage(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public CustomErrorMessage(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
