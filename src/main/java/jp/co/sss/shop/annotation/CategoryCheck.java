package jp.co.sss.shop.annotation;
//aa
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jp.co.sss.shop.validator.CategoryValidator;

/**
 * カテゴリ名重複チェックの独自アノテーション定義
 *
 * @author SystemShared
 */

/**
 * アノテーション付与対象
 */

@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE,
		java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD,
		java.lang.annotation.ElementType.PARAMETER })

/**
 * アノテーション情報の維持範囲
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented

/**
 * 入力チェック対象：CategoryValidator
 */
@Constraint(validatedBy = { CategoryValidator.class })
public @interface CategoryCheck {

	/**
	 * 入力チェックNGの場合のメッセージを設定
	 * @return 表示メッセージ
	 */
	String message() default "{categoryListAdmin.duplicate.message}";

	/**
	 * 特定のバリデーショングループを設定(設定なし)
	 * @return バリデーショングループのクラスリスト
	 */
	Class<?>[] groups() default {};

	/**
	 * 検証対象データに対する属性や関連する情報を定義  (処理定義なし)
	 * @return 対象となるオブジェクトのペイロード
	 */
	Class<? extends Payload>[] payload() default {};

	/**
	 * カテゴリ名の取得
	 * @return カテゴリ名
	 */
	String fieldName() default "name";

	/**
	 * カテゴリIDの取得
	 * @return カテゴリID
	 */
	String fieldId() default "id";

}
