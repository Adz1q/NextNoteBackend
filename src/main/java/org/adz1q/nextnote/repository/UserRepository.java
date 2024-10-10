package org.adz1q.nextnote.repository;

import org.adz1q.nextnote.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
