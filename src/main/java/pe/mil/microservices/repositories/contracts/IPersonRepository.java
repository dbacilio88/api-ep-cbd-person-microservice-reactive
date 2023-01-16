package pe.mil.microservices.repositories.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.mil.microservices.repositories.entities.PersonEntity;

@Repository
public interface IPersonRepository extends JpaRepository<PersonEntity, Long> {
    boolean existsByPersonId(Long personId);


}
