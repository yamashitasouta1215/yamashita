package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Artist;


public interface ArtistRepository extends JpaRepository<Artist, Integer>{

	Artist findByNameContaining(String name);

}
