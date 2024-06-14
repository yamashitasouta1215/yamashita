package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Artist;
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
	public Page<Item>findAllByQueryAndDeleteFlag(@Param(value = "deleteFlag")int deleteFlag,Pageable pageable);

	//新着順表示
	Page<Item> findByOrderByReleaseDateDesc(Pageable pageable);

	//売れ筋順表示かつカテゴリ別表示
	@Query("SELECT i FROM OrderItem o INNER JOIN Item i on o.item.id=i.id WHERE i.category.id =:categoryId AND i.deleteFlag =:deleteFlag GROUP BY i  ORDER BY COUNT(i) DESC,i.id ASC")
	public List<Item> findCategoryByQueryAndDeleteFlag(@Param(value="categoryId")Integer categoryId,@Param(value="deleteFlag")int deleteFlag);


	//新着順表示かつカテゴリ別表示
	List<Item> findByCategoryOrderByReleaseDateDesc(Category category);



	Page<Item> findByNameContaining(String name,Pageable pageable);
	
	

	List<Item> findByNameContaining(String name);

//
//	List<Item> findByArtistId(List<Artist> artist);

	List<Item> findByReleaseDateContaining(Integer releaseDate);

	List<Item> findByArtistName(Artist artist);


	Page<Item> findByPriceLessThanOrderByPrice(Integer price,Pageable pageable);

	Page<Item> findByPriceBetweenOrderByPrice(int i, int j,Pageable pageable);

	Page<Item> findByPriceGreaterThanOrderByPrice(int i,Pageable pageable);


	
}
