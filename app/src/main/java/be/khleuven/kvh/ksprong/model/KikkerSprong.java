package be.khleuven.kvh.ksprong.model;


public class KikkerSprong {

    private User user;



    public KikkerSprong(){

    }

    public String checkIn(User user){


        return user.getName() + user.getSurname();

    }

}
