# JAVAEE课设

## 为什么使用SpringBoot

+ 为所有 Spring 开发提供一个更快更广泛的入门体验。

+ 零配置。无冗余代码生成和XML 强制配置，遵循“约定大于配置” 。（只有在偏离习惯配置的时候才需要增加配置。典型的就是java的标注）

+ 集成了大量常用的第三方库的配置， Spring Boot 应用为这些第三方库提供了几乎可以零配置的开箱即用的能力。

+ 提供一系列大型项目常用的非功能性特征，如嵌入式服务器、安全性、度量、运行状况检查、外部化配置等。

+ Spring Boot 不是Spring 的替代者，Spring 框架是通过 IOC 机制来管理 Bean 的。Spring Boot 依赖 Spring 框架来管理对象的依赖。Spring Boot 并不是Spring
  的精简版本，而是为使用 Spring 做好各种产品级准备

## 工作流程

&emsp;&emsp;用户向服务器发送请求，请求会到DispatcherServlet，DispatcherServlet
对请求URL进行解析，得到请求资源标识符（URI），然后根据该URI，调用HandlerMapping获得该Handler配置的所有相关的对象（包括一个Handler处理器对象、多个HandlerInterceptor拦截器对象），最后以HandlerExecutionChain对象的形式返回.

&emsp;&emsp;DispatcherServlet 根据获得的Handler，选择一个合适的HandlerAdapter。提取Request中的模型数据，填充Handler入参，开始执行Handler（Controller)。
在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作,(如HttpMessageConveter 将JSON转成一个对象;数据转换;数据格式化)

&emsp;&emsp;Handler执行完成后，向DispatcherServlet
返回一个ModelAndView对象；根据返回的ModelAndView，选择一个适合的ViewResolver返回给DispatcherServlet；ViewResolver
结合Model和View，来渲染视图，最后将渲染结果返回给客户端。

## 注解相关

### @AutoWired

+ 当 Spring遇到一个在 setter 方法中使用的 @Autowired 注释，它会在方法中视图执行 byType (搜索整个配置文件中类名为这个的)自动连接。

### @ResponseBody

+ @ResponseBody是作用在方法上的，@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中，一般在异步获取数据时使用【也就是AJAX】。 注意：在使用
  @RequestMapping后，返回值通常解析为跳转路径，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。 比如异步获取 json 数据，加上
  @ResponseBody 后，会直接返回 json 数据。@RequestBody 将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象。
+ 一般作用在Controller的方法上

### @PostMapping

+ 是@RequestMapping(method = RequestMethod.POST)的快捷方式
+ 几个重要的参数
    + headers与Content-Type需一致。 使用例子 headers = "content-type=text/*（具体请求中的媒体类型信息）
    + @RequestParam则是指定参数的类型
    + produces是指定返回的数据类型。consumes是该接口只接受指定的数据类型

### @Service,@Controller,@Mapper,@Repository

+ @Service与@Controller属于Spring框架的注解，注解在类上，需要被扫描才能创建实例化对象，并将对象放到Spring容器中。
+ @Mapper注解是Mybatis框架的注解，使用这个注解我们的接口就可以被Mybatis框架加载，然后动态代理生成实体类，然后就将实体类放到了Spring容器中。
+ @Repository（实现dao访问） 用于标注数据访问层，也可以说用于标注数据访问组件，即DAO组件

### @ResultType(myBatis标签)

+ resultType是sql映射文件中定义返回值类型

### @Results

+ MyBatis中使用@Results注解来映射查询结果集到实体类属性。

+

@Results的基本用法。当数据库字段名与实体类对应的属性名不一致时，可以使用@Results映射来将其对应起来。column为数据库字段名，porperty为实体类属性名，jdbcType为数据库字段数据类型，id为是否为主键。@One代表查询返回的数据只能是一条否则会报错。@Many则是对应一对多查询

### @Param

+ 作用:使得mapper.xml中的参数与后台的参数对应上

+ 与@RequestParam的区别。
    + @Param是myBatis的标签主要用于Dao层。@RequestParam则是用于Controller层

### @Order

+ 定义组件加载顺序

## 跨域

### 为什么会产生跨域

+ 请求协议http,https的不同
+ 域domain的不同
+ 端口port的不同

### 如何解决跨域

+ 配置一个过滤器，其中的几个重要参数做说明
 + Access-Control-Allow-Origin 就是允许指定的域名跨域。这里采用了直接把接受到的域名作为指定的域名
 + Access-Control-Allow-Credentials:true 是否允许后续请求携带认证信息（cookies）,该值只能是true,否则不返回，表示XHR是否接收cookies和发送cookies

## 拦截器

### 为什么要使用拦截器

+ 主要做的是一个权限的认证

### 如何去使用拦截器

#### 实现HandlerInterceptor接口的方法



```mermaid
graph TB
  start[开始] --> input[获取当前Handler和role]
  input --> D{是否这个handler为HandlerMethod<br>且其身上的注解不为NoAuthority}
  D --yes-->E{是否这个handler为异常类}
  E --No-->return
  E --yes --> F[根据其身上的注解和role进行比对] 
  F --> G{如果这个role为空}
  G --yes-->返回权限校验失败
  G --No -->进行权限验证 
  
 ```


#### 实现WebMvcConfigurer接口并添加拦截器

&emsp;&emsp;WebMvcConfigurer配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter。基于java-based方式的spring mvc配置，需要创建一个配置类并实现WebMvcConfigurer 接口；

