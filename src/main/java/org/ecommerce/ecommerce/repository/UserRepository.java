package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
}
