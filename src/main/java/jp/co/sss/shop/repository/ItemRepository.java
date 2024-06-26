package jp.co.sss.shop.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query("SELECT i FROM Item i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC,i.id DESC")
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
	        @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);
	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);
	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByNameAndDeleteFlag(String name, int notDeleted);
	
	//トップページ
	@Query("SELECT i FROM OrderItem o INNER JOIN Item i on o.item.id=i.id WHERE i.deleteFlag =:deleteFlag GROUP BY i ORDER BY COUNT(i) DESC,i.id ASC")
	public List<Item>findAllByQueryAndDeleteFlag(@Param(value = "deleteFlag")int deleteFlag);
	@Query("SELECT i FROM OrderItem o INNER JOIN Item i on o.item.id=i.id WHERE i.deleteFlag =:deleteFlag GROUP BY i ORDER BY COUNT(i) DESC,i.id ASC")
	public Page<Item>findAllByQueryAndDeleteFlag(@Param(value = "deleteFlag")int deleteFlag,Pageable pageable);
	//新着順表示
	List<Item> findByDeleteFlagOrderByReleaseDateDesc(@Param(value = "deleteFlag")int deleteFlag);
	Page<Item> findByDeleteFlagOrderByReleaseDateDesc(@Param(value = "deleteFlag")int deleteFlag,Pageable pageable);
	//売れ筋順表示かつカテゴリ別表示
	@Query("SELECT i FROM OrderItem o INNER JOIN Item i on o.item.id=i.id WHERE i.category.id =:categoryId and i.deleteFlag =:deleteFlag GROUP BY i  ORDER BY COUNT(i) DESC,i.id ASC")
	public List<Item> findCategoryByQuery(@Param(value="categoryId")Integer categoryId,@Param(value = "deleteFlag")int deleteFlag);
	//新着順表示かつカテゴリ別表示
//	List<Item> findByCategoryOrderByReleaseDateDesc(Category category,@Param(value = "deleteFlag")int deleteFlag);
	@Query("SELECT i FROM Item i WHERE i.category = :category AND i.deleteFlag = :deleteFlag ORDER BY i.releaseDate DESC")
    List<Item> findByCategoryOrderByReleaseDateDesc(@Param("category") Category category, @Param(value="deleteFlag") int deleteFlag);
	
//	Page<Item> findByNameContaining(String name,@Param(value = "deleteFlag")int deleteFlag,Pageable pageable);
	@Query("SELECT i FROM Item i WHERE i.name LIKE %:name% AND i.deleteFlag = :deleteFlag ORDER BY i.releaseDate DESC")
	Page<Item> findByNameContaining(@Param("name") String name, @Param("deleteFlag") int deleteFlag, Pageable pageable);
	
//	List<Item> findByNameContaining(String name);
//
//
//	List<Item> findByArtistId(Integer integer);
//
//	List<Item> findByReleaseDateContaining(Integer releaseDate);
//
//	List<Item> findByArtistName(Artist artist);
//	List<Item> findByNameContaining(String name);
//
//	List<Item> findByArtistId(List<Artist> artist);
//	List<Item> findByReleaseDateContaining(Integer releaseDate);
//
//	List<Item> findByArtistName(Artist artist);
	@Query("SELECT i FROM Item i WHERE i.price = :price AND i.deleteFlag = :deleteFlag ORDER BY i.releaseDate DESC")
	Page<Item> findByPriceLessThanOrderByPrice(Integer price,@Param(value = "deleteFlag")int deleteFlag,Pageable pageable);
	
//	Page<Item> findByPriceBetweenOrderByPrice(int b, int j,Pageable pageable);
	@Query("SELECT i FROM Item i WHERE i.price BETWEEN :b AND :j AND i.deleteFlag = :deleteFlag ORDER BY i.price")
	Page<Item> findByPriceBetweenOrderByPrice(@Param("b") int b, @Param("j") int j, @Param("deleteFlag") int deleteFlag, Pageable pageable);
//	Page<Item> findByPriceGreaterThanOrderByPrice(int b,@Param("deleteFlag") int deleteFlag,Pageable pageable);
	@Query("SELECT i FROM Item i WHERE i.price > :b AND i.deleteFlag = :deleteFlag ORDER BY i.price")
	Page<Item> findByPriceGreaterThanOrderByPrice(@Param("b") int b, @Param("deleteFlag") int deleteFlag, Pageable pageable);




//	List<Item> findByArtistId(@Param("name") String name, @Param("deleteFlag") int deleteFlag);
//	@Query("SELECT i FROM Item i WHERE i.id AND i.deleteFlag = :deleteFlag ORDER BY i.releaseDate DESC")
	@Query("SELECT i FROM Item i WHERE i.artist.id = :id AND i.deleteFlag = :deleteFlag ORDER BY i.releaseDate DESC")
	List<Item> findByArtistId(@Param("id") int id, @Param("deleteFlag") int deleteFlag);



}