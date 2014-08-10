package be.khleuven.kvh.ksprong.model;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class KikkerSprong {

    private User user;



    public KikkerSprong(){

    }

    public String checkIn(User user){


        return user.getName() + user.getSurname();

    }

}
