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
public class loginError extends Exception {
    
    public loginError()
    {
    }
    
    public loginError(String message)
    {
        super(message);
    }
    
    public loginError(Throwable cause)
    {
        super(cause);
    }
    
    public loginError(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public loginError(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
