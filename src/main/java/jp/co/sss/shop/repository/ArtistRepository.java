package jp.co.sss.shop.repository;

<<<<<<< HEAD
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> branch 'master' of https://yamashitasouta1215@github.com/yamashitasouta1215/yamashita.git
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.shop.entity.Artist;


public interface ArtistRepository extends JpaRepository<Artist, Integer>{

	Artist findByNameContaining(String name);

	@Query("SELECT a FROM Artist a WHERE a.deleteFlag =:deleteFlag ORDER BY a.insertDate DESC,a.id DESC")
	Page<Artist> findByDeleteFlagOrderByInsertDateDescIdDescPage(
			@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);
	
	Artist findByIdAndDeleteFlag(Integer id, int deleteFlag);
}
