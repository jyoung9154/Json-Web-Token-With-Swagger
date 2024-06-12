package toy.project.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.project.jwt.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /* 사용자 ID로 확인 */
    Optional<User> findByUserId(String userId);

    /* 사용자 ID 존재여부 확인 */
    boolean existsByUserId(String userId);

    /* 사용자 이름으로 조회 */
    Optional<User> findByName(String name);
}
