package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Artist;


public interface ArtistRepository extends JpaRepository<Artist, Integer>{

	List<Artist> findByNameContaining(String name);

}
