package muface.application.domain.repository;

import muface.application.domain.model.Diploma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiplomaRepository {

    List<Diploma> findDiplomasByTitulacionName(String titulacionName);

    Page<Diploma> findDiplomasByTitulacionName(String titulacionName, Pageable pageable);

}
