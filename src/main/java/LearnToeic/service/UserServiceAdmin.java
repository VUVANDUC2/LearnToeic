package LearnToeic.service;

import LearnToeic.model.UserAdmin;
import LearnToeic.repository.UserRepositoryAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
@Service
public class UserServiceAdmin {

    @Autowired
    private UserRepositoryAdmin userRepository;
    // Get all users
    public List<UserAdmin> findAllUsers() {
        return userRepository.findAll();
    }
    // Get user by ID
    public Optional<UserAdmin> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    //Paginate users
    public Page<UserAdmin> getPaginateUsers(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(searchTerm !=null && !searchTerm.trim().isEmpty()) {
            return userRepository.findByFullNameContainingIgnoreCase(searchTerm, pageable);
        }

        return userRepository.findAll(pageable);
    }

    public int getDistinctUserCount(){
        return userRepository.countDistinctUsers();
    }
}