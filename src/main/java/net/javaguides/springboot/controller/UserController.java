package net.javaguides.springboot.controller;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.repository.UserRepository;

@RestController
@CrossOrigin(origins ="*", exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;



	@GetMapping
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable (value = "id") long userId) {
		return this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return this.userRepository.save(user);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody Auth auth) {
		User user = this.userRepository.findUsersByUsername(auth.getUsername());
		if(Objects.equals(user.getPassword(), auth.getPassword())){
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		 else{
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable ("id") long userId) {
		 User existingUser = this.userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		 existingUser.setUsername(user.getUsername());
		 existingUser.setPassword(user.getPassword());
		 existingUser.setPlace(user.getPlace());
		 existingUser.setRole(user.getRole());
		 existingUser.setNumber(user.getNumber());
		 return this.userRepository.save(existingUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable ("id") long userId){
		 User existingUser = this.userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		 this.userRepository.delete(existingUser);
		 return ResponseEntity.ok().build();
	}
}
