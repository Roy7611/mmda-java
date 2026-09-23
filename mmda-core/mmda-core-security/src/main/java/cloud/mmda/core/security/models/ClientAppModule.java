package cloud.mmda.core.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
/**
 * 客户端应用模块
 *
 * @remarks 客户端应用模块
 *
 * @author mmda codebot
 * @version 3.0.0
 * @since 2024-07-17 07:38:57.0
 *
 */
@Data
@Entity
@IdClass(ClientAppModule.Key.class)
@Table(name="clientappmodule",catalog = "mmda_base")
public class ClientAppModule {
    @Id
	@NotBlank
	@Size(min=1,max=36)
    private String appId;
    @Id
	@NotBlank
	@Size(min=1,max=15)
    private String moduleCode;

	/**
	 * 必要的
	 */
    private boolean indispensable;
	/**
	 * 月租金
	 */
    private int monthlyRent;
	/**
	 * 起租月数
	 */
    private int minMonths;
	/**
	 * 用户数限制
	 */
    private Integer maxUserNum;
	/**
	 * 数据量限制
	 */
    private Long maxDataNum;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "appId", nullable = false)
//    private ClientApp app;

	/**
	 * 主键类
	 */
    public static class Key implements Serializable {
        private String appId;
        private String moduleCode;
    }
}
