package jp.co.sss.shop.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.shop.entity.Artist;


public interface ArtistRepository extends JpaRepository<Artist, Integer>{

	List<Artist> findByNameContaining(String name);

	@Query("SELECT a FROM Artist a WHERE a.deleteFlag =:deleteFlag ORDER BY a.insertDate DESC,a.id DESC")
	Page<Artist> findByDeleteFlagOrderByInsertDateDescIdDescPage(
			@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);
	
	Artist findByIdAndDeleteFlag(Integer id, int deleteFlag);
	
	List<Artist> findByDeleteFlagOrderByInsertDateDescIdDesc(int deleteFlag);
	
	@Query("SELECT i FROM Artist i WHERE i.name LIKE %:name% AND i.deleteFlag = :deleteFlag")
	List<Artist> findByNameContaining(@Param("name") String name, @Param("deleteFlag") int deleteFlag);
}
