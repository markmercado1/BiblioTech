package upeu.bibliotech_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.bibliotech_v2.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long>  {
}
