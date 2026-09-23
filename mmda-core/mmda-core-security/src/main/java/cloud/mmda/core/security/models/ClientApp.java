package cloud.mmda.core.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 客户端应用
 * 
 * @remarks 客户端应用。定义一个客户端应用程序，可申请密钥，支持多种OAuth授权。
 * 
 * @author mmda codebot 
 * @version 3.0.0 
 * @since 2024-07-17 07:38:57.0
 * 
 */
@Data
@Entity
@Table(name="clientapp",catalog = "mmda_base")
public class ClientApp {
	/**
	 * 应用标识
	 */
    @Id
    @NotBlank
    private String appId;//clientId
	/**
	 * 应用商标
	 */	
    @Column(updatable = false)
    private String appLogo;
	/**
	 * 应用名称
	 */	
    @Column(updatable = false)
    private String appName;//clientName
	/**
	 * 应用标题
	 */	
    @Column(updatable = false)
    private String appTitle;
	/**
	 * 应用网址
	 */	
    @Column(updatable = false)
    private String appUri;
    @Column(updatable = false)
	/**
	 * 月租
	 */
	private int monthlyRent;
	/**
	 * 状态：0;DEV;开发中|1;TESTING;测试中|2;RELEASED;已发布|-1;DEPRECATED;已停用
	 */	
	private short status;


	/**
	 * 最后发布于
	 */
    private Timestamp latestReleasedAt;//clientIdIssuedAt
	/**
	 * 最后发布版本
	 */
    private String latestReleasedVersion;

	/**
	 * 应用密钥
	 */
    private String appSecret;//clientSecret
	/**
	 * 应用密钥过期于
	 */
    private Timestamp appSecretExpiresAt;//clientSecretExpiresAt

    /**
     * 客户端认证方法：none|client_secret_basic|client_secret_post|client_secret_jwt|private_key_jwt|tls_client_auth|self_signed_tls_client_auth
     */
    private String clientAuthenticationMethods;
    /**
     * 授权类型：authorization_code|refresh_token|client_credentials|password|urn:ietf:params:oauth:grant-type:jwt-bearer|urn:ietf:params:oauth:grant-type:device_code|urn:ietf:params:oauth:grant-type:token-exchange
     */
	/**
	 * 授权类型：authorization_code|refresh_token|client_credentials|password|urn:ietf:params:oauth:grant-type:jwt-bearer|urn:ietf:params:oauth:grant-type:device_code|urn:ietf:params:oauth:grant-type:token-exchange
	 */
    private String authorizationGrantTypes;
	/**
	 * 重定向URIs
	 */
    private String redirectUris;
	/**
	 * 登出后重定向URIs
	 */
    private String postLogoutRedirectUris;
	/**
	 * 授权作用域
	 */
    private String scopes;
	/**
	 * 客户端配置
	 */
    private String clientSettings;
	/**
	 * 令牌配置
	 */
    private String tokenSettings;
	/**
	 * 备注
	 */	
    private String description;	

    //不需要子模块来限定作用域，使用scopes
//    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) //mappedBy="app",
//    @JoinColumn(name = "appId", nullable = false)
//    private Collection<ClientAppModule> modules = new ArrayList<>();
	/**
	 * 发布历史
	 */
//	private List<ClientAppRelease> releases;
}
