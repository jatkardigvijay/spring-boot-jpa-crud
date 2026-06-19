package com.jbd.repository;

import com.jbd.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_returnsUser_whenExists() {
        userRepository.save(new User(0, "john_test", "encodedPass", "ROLE_USER"));

        Optional<User> found = userRepository.findByUsername("john_test");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_test");
        assertThat(found.get().getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void findByUsername_returnsEmpty_whenUserDoesNotExist() {
        Optional<User> found = userRepository.findByUsername("nonexistent_xyz");

        assertThat(found).isEmpty();
    }

    @Test
    void save_persistsUser_withGeneratedId() {
        User user = new User(0, "jane_test", "password", "ROLE_ADMIN");

        User saved = userRepository.save(user);

        assertThat(saved.getUserId()).isGreaterThan(0);
        assertThat(saved.getUsername()).isEqualTo("jane_test");
    }
}