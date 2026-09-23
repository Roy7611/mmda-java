package cloud.mmda.core.web;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 跳转结果
 *
 * 用于App/H5统一的跳转指示，APP使用(redirectType,redirectParam)
 * H5使用redirectTo相对跳转路径
 */
@AllArgsConstructor
public class RedirectResult {
	public static final String REDIRECT_TYPE = "redirectType";
	public static final String REDIRECT_TO = "redirectTo";
	public static final String REDIRECT_PARAM = "redirectParam";

	//H5跳转
	public static final String REDIRECT_URL = "URL";


	/**
	 * 跳转类型：
	 * 商品详情，资讯详情，商品集合，品牌列表，品类列表，活动页面，H5页面
	 */
	@Getter
	private final String redirectType;

	/**
	 * 跳转参数，多个键值使用:隔开
	 */
	@Getter
	private final String redirectParam;

	/**
	 * 跳转地址，
	 * 统一为 eshop/app/products/1 这种格式
	 * 板块：eshop,society,event,my,
	 *
	 */
	@Getter
	private final String redirectTo;

	/**
	 *
	 * @param redirectType
	 * @param redirectParam
	 * @param redirectTo
	 * @return
	 */
	public RedirectResult valueOf(String redirectType, String redirectParam, String redirectTo){
		return new RedirectResult(redirectType, redirectParam, redirectTo);
	}
}
