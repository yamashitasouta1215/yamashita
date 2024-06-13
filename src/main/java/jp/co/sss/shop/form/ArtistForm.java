package jp.co.sss.shop.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.co.sss.shop.annotation.CategoryCheck;

@CategoryCheck
public class ArtistForm implements Serializable {
	
	/**
	 * アーティストID
	 */
	private Integer id;
	
	/**
	 * アーティスト名
	 */
	@NotBlank
	@Size(min = 1, max = 100, message = "{text.maxsize.message}")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
