## spring boot 整合 shiro

### 1.添加shiro依赖
在pom.xml中添加shiro依赖
```
<!--spring boot 整合shiro依赖-->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>${shiro.version}</version>
</dependency>

<!--shiro依赖-->
<dependency>
     <groupId>org.apache.shiro</groupId>
     <artifactId>shiro-all</artifactId>
     <version>${shiro.version}</version>
</dependency>
```
### 2.身份认证-用户登陆
#### 2.1 用户实体
创建用户实体SysUser
```
    @Id
    @GeneratedValue
    private Integer uid;
    @Column(unique =true)
    private String username;//帐号
    private String name;//名称（昵称或者真实姓名，不同系统不同定义）
    private String password; //密码;
    private String salt;//加密密码的盐
    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色
```
#### 2.2 拦截资源文件过滤器
创建shiro配置类：ShiroConfig  
添加shiroFilterFactoryBean()方法  
添加拦截链接注意顺序，存在‘**’一定要放在最后，不然配置会失效
```
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        //shiroFilterFactoryBean对象
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 配置shiro安全管理器 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 指定要求登录时的链接
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权时跳转的界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/templates/**", "anon");
        filterChainDefinitionMap.put("/login/validate", "anon");
        // 配置退出过滤器,具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        filterChainDefinitionMap.put("/**", "authc");
        logger.debug("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }
```
#### 2.3 添加身份认证realm
在ShiroConfig中添加shiroRealm()
```
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }
```
实现身份证realm：ShiroRealm，继承AuthorizingRealm，  
重写doGetAuthorizationInfo()方法，主要用于访问权限验证（此节暂不实现）  
重写doGetAuthenticationInfo()方法，主要用于登陆验证
```
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        logger.info("登录验证");
        //获取用户的输入的账号.
        String username = (String)authenticationToken.getPrincipal();
        System.out.println(authenticationToken.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser sysUser = sysUserService.findByUsername(username);
        if(sysUser == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                sysUser, //用户名
                sysUser.getPassword(), //密码
                ByteSource.Util.bytes(sysUser.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }
```
#### 2.4 shiro安全管理器
在ShiroConfig中添加securityManager()方法
```
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }
```
#### 2.5 密码加密
在ShiroConfig中添加凭证匹配器hashedCredentialsMatcher()  
此处只需指定密码加密方式，密码校验交给Shiro的SimpleAuthenticationInfo处理
```
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }
```
### 3.身份认证-权限控制
#### 3.1 角色、权限实体
角色实体
```
    @Id@GeneratedValue
    private Integer id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String description; // 角色描述,UI界面显示使用
    private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
    private List<SysUser> sysUserList;// 一个角色对应多个用户
```
权限实体
```
    @Id@GeneratedValue
    private Integer id;//主键.
    private String name;//名称.
    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String icon; //图标
    private Integer sort; //排序
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Boolean available = Boolean.FALSE;
    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
```
#### 3.2 添加链接需要的权限配置
在ShiroConfig的shiroFilterFactoryBean()方法中添加链接权限配置信息
```
filterChainDefinitionMap.put("/user/list", "authc,perms[user:list]");
```
shiro内置过滤器
//TODO所有内置过滤器说明

|标识|说明|实现类|
|----|----|----|
|anno|允许匿名使用|org.apache.shiro.web.filter.authc.AnonymousFilter|

### 4.ajax控制
Shiro没有对ajax作处理，ajax请求要自己添加filter处理  
创建ShiroPermsFilter，继承PermissionsAuthorizationFilter  
重写onAccessDenied()方法，ShiroRealm的doGetAuthoriaxtionInfo()方法验证失败会调用此方法  
根据请求中的 X-Requested-With:XMLHttpRequest 来区分ajax请求、普通请求
```
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestedWith = httpServletRequest.getHeader("X-Requested-With");
        if (StringUtils.isNotEmpty(requestedWith) && StringUtils.equals(requestedWith, "XMLHttpRequest")) {
            logger.info("无访问权限-ajax请求");
            //如果是ajax返回指定格式数据
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            Gson gson = new Gson();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", "-1");
            resultMap.put("msg", "无访问权限");
            httpServletResponse.getWriter().write(gson.toJson(resultMap));
        }
        else{
            logger.info("无访问权限-普通请求");
            //如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/403");
        }
        return false;
    }
```
### 5.缓存功能:ehcache缓存管理器
Shiro引入ehcache缓存，减少查询数据库次数  
#### 5.1 添加ehcache缓存依赖
```
<dependency>
   <groupId>org.hibernate</groupId>
   <artifactId>hibernate-ehcache</artifactId>
</dependency>
        
<!--shiro添加ehcache缓存 -->
<dependency>
   <groupId>org.apache.shiro</groupId>
   <artifactId>shiro-ehcache</artifactId>
   <version>1.2.6</version>
</dependency>
```
#### 5.2 在ShiroConfig中添加ehCacheManager()方法  
PS:shiro中启用ehcache与hiberate启用ehcache会产生冲突，新版
ehcache中不允许jvm存在两个同名ehcache,所以新增shiro ehcache时应
先判断是否存在
```
    @Bean
    public EhCacheManager ehCacheManager() {
        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
        if(cacheManager == null){
            logger.info("shiro ehcache不存在，执行初始化");
            try {
                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:ehcache-shiro.xml"));
            } catch (IOException e) {
                throw new RuntimeException("initialize cacheManager failed");
            }
        }
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }
```
#### 5.3 添加对应的ehcaceh配置文件：ehcache-shiro.xml
```
<ehcache>
    <diskStore path="java.io.tmpdir"/>
    <defaultCache
            maxElementsInMemory="10000"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            maxElementsOnDisk="10000000"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">
    </defaultCache>
    <!-- 设定缓存的默认数据过期策略 -->
    <cache name="shiro"
           maxElementsInMemory="10000"
           timeToIdleSeconds="120"
           timeToLiveSeconds="120"
           maxElementsOnDisk="10000000"
           diskExpiryThreadIntervalSeconds="120"
           memoryStoreEvictionPolicy="LRU">
    </cache>
</ehcache>
```
#### 5.4 注入ehcache管理器
在ShiroConfig的securityManager()方法中注入ehcache缓存管理器
```
securityManager.setCacheManager(ehCacheManager());
```
### 6.登陆次数限制功能
限制用户登陆次数，超过一定错误次数之后锁定帐号一段时间  
登陆次数限制功能依赖于缓存，用户尝试登陆次数存放于缓存中，锁定帐号时长由缓存失效时间决定
#### 6.1 在缓存配置文件中添加passwordRetryCache
passwordRetryCache缓存用于存放用户尝试登陆的次数信息，登陆失败计数会一直累加，登陆成功则清空对于缓存  
timeToIdleSecond来保证锁定时间(帐号锁定之后的最后一次尝试间隔timeToIdleSecond秒之后自动清除)
```
    <!-- 登录记录缓存 锁定2分钟 -->
    <cache name="passwordRetryCache"
           maxEntriesLocalHeap="10000"
           eternal="false"
           timeToIdleSeconds="120"
           timeToLiveSeconds="0"
           overflowToDisk="false"
           statistics="false">
    </cache>
```
#### 6.2 实现登陆次数限制凭证器
创建RetryLimitHashedCredentialsMatcher， 继承HashedCredentialsMatcher  
在回调方法doCredentialsMatch()方法中进行身份认证
```
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取登录用户名
        String username = (String) token.getPrincipal();
        //从ehcache中获取密码输错次数
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            //第一次
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        //retryCount.incrementAndGet()自增：count + 1
        if (retryCount.incrementAndGet() >= loginRetryLimit) {
            //超过次数锁定
            throw new ExcessiveAttemptsException(username + " 在一段时间内尝试登录超过" + loginRetryLimit + "次");
        }
        //否则走判断密码逻辑
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            // clear retry count  清楚ehcache中的count次数缓存
            passwordRetryCache.remove(username);
        }
        return matches;
    }
```
#### 6.3 使用登陆次数限制凭证器
在ShiroConfig的hashedCredentialsMatcher()方法中改用登陆次数限制凭证器
```
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager());
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }
```
### 7.rememberMe功能
#### 7.1 rememberMeCookie配置
在ShiroConfig中添加remeberMeCookie()方法，设置cookie过期时间
```
    @Bean
    public SimpleCookie remeberMeCookie(){
        logger.debug("记住我，设置cookie过期时间！");
        //cookie名称;对应前端的checkbox的name = rememberMe
        SimpleCookie scookie=new SimpleCookie("rememberMe");
        //记住我cookie生效时间1小时 ,单位秒  [1小时]
        scookie.setMaxAge(3600);
        return scookie;
    }
```
#### 7.2 rememberMe管理器
在ShiroConfig中添加rememberMeManager()方法，配置rememberMe cookie管理器
```
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        logger.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }
```
#### 7.3 注入rememberMe管理器
在ShiroConfig的securityManager()方法中注入rememberMe cookie管理器
```
securityManager.setRememberMeManager(rememberMeManager());
```
#### 7.4 使用rememberMe功能


实例完整代码地址：https://github.com/crimps/springskill/tree/master/shiroskill