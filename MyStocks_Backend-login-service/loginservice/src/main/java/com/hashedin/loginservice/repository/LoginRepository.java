package com.hashedin.loginservice.repository;

import com.hashedin.loginservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

    default boolean signUp(User user){
       User userCreated = save(user);
       if(userCreated!=null)
           return true;
       return false;
    }

//    public List<User> findByusername(String username);
    public User findByUsername(String username);
}
