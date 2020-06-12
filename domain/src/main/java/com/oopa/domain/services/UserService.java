package com.oopa.domain.services;

import com.oopa.dataAccess.repositories.UserRepository;
import com.oopa.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        var userEntity = this.modelMapper.map(user, com.oopa.dataAccess.model.User.class);
        return this.modelMapper.map(userRepository.save(userEntity), User.class);
    }
    
    public User getUserById(Integer id){
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find " + User.class.getName() + " with id " + id);
        }
        return this.modelMapper.map(user.get(), User.class);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll().stream()
                .map(user -> this.modelMapper.map(user, User.class))
                .collect(Collectors.toList());
    }

    public User deleteUser(Integer id){
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find " + User.class.getName() + " with id " + id);
        }
        userRepository.deleteById(id);
        return this.modelMapper.map(user.get(), User.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = getUserByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public User getUserByEmail(String email) {
        var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find " + User.class.getName() + " with email " + email);
        }
        var user = optionalUser.get();
        return this.modelMapper.map(user, User.class);
    }
}
