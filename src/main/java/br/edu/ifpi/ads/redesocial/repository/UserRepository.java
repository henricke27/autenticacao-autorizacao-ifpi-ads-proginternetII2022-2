package br.edu.ifpi.ads.redesocial.repository;

import br.edu.ifpi.ads.redesocial.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
