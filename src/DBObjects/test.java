/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBObjects;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Nicki
 */
public class test {
    
     public static void main(String[] arg) {
         
         EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
         EntityManager emq = emf.createEntityManager();
//         Query allUsersq = emq.createNativeQuery("select * from Users", Users.class);
         
         
Query user = emq.createNativeQuery("SELECT * FROM Users WHERE userName='" + "nick" + "' AND password='" + "nicki12345" +"';", Users.class);
                
List<Users> userss = user.getResultList();

try {
    System.out.println(userss.get(0).getUserName());
    
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Bruger findes ikke...");
}
         

//         Query allCampssq = emq.createNativeQuery("select * from Camps", Camps.class);
//         List<Camps> allCamps = allCampssq.getResultList();
//         
//
//         List<Users> allUsers = allUsersq.getResultList();
//         for(Users u: allUsers){
//             System.out.println("user: "+u.getUserName());
//         }
//         
//         for(Camps c: allCamps) {
//             System.out.println(c.getCampName());
//         }
         
//         Camps c = new Camps();
//         c.setCampName("hulabula3");
//             persist(c);
//         
//             Users user1 = new Users("nicki2");
//             user1.setPassword("tre");
//             user1.setUserType("manager");
//             user1.setToken("testToken");
//             user1.setCamp(c);
//             
//             persist(user1);
    
             
    
//         System.out.println("user" + u.getUserType());    
    
     }

    public static void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CleanOutLoudServerPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    

    
    
    
}
