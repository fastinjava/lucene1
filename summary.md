# 流行框架总结

# lucene

## lucene简介

    1、全文检索和搜寻的开源软件库，由Apache软件基金会支持和提供
    2、Lucene提供了一个简单却强大的应用程序接口（API），能够做全文索引和搜寻，在Java开发环境里Lucene是一个成熟的免费开放源代码工具
    3、Lucene并不是现成的搜索引擎产品，但可以用来制作搜索引擎产品
    
## 什么是全文检索

    计算机程序扫描文档中的每一个词，对每一个词建立索引，指明该词在文章中出现的次数和位置，当用户进行查询时，检索程序根据事先建立的索引进行查找，并将查找的结果反馈给用户的检索方式
    简而言之：
        lucene全文检索就是对文档中的全部内容进行分词，然后对所有单词建立倒排索引的过程
        
## Lucene、Solr、Elasticsearch关系

   1、Lucene：底层的API，工具包
   
   2、Solr：基于Lucene开发的企业级的搜索引擎产品
   
   3、Elasticsearch：基于Lucene开发的企业级的搜索引擎产品        
 
## lucene的基本使用

    使用Lucene的API来实现对索引的增（创建索引）、删（删除索引）、改（修改索引）、查（搜索数据） 

### 创建索引

    创建索引的过程
        前言总结：
        
        1、文档Document：数据库中一条具体的记录
        2、字段Field：数据库中的每个字段
        3、目录对象Directory：索引物理存储位置
        4、写出器的配置对象：需要分词器和lucene的版本
        5、indexWriter：操作索引库的核心对象
        
        具体流程：
        1、创建indexWriter对象
            param1：directory索引库存放的实际物理位置
                 Directory directory = FSDirectory.open(new File("D:\\temp\\index").toPath());
            param2：config对象（indexConfig）主要用来配置  配置分析器和lucene版本
                new IndexWriterConfig(new IKAnalyzer())
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        
        2、创建域
        
        Field fileNameField = new TextField("filename", fileName, Field.Store.YES);
          
        3、创建文档对象
        
        Document document = new Document();
        
        4、将上述配置的域对象添加到文档中
        
        document.add(fileNameField);
        
        5、使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
        
        indexWriter.addDocument(document);

### Field总结

   一个document对象可以有多个不同的字段，每一个字段都是一个Field对象
   一个Document中的字段其类型是不确定的，因此Field类就提供了各种不同的子类，来对应这些不同类型的字段
   常见字段总结：
        LongPoint、FloatPoint、StringField、DoublePoint 会索引不会分词，意味着搜索时必须匹配所有内容
        TextField 索引和分词
        StoredField 一定会存储但是不会被索引
        
        如何确定一个字段是否需要存储？
        如果一个字段要显示到最终的结果中，那么一定要存储，否则就不存储
        
        如何确定一个字段是否需要创建索引
        如果要根据这个字段进行搜索，那么这个字段就必须创建索引
        
        如何确定一个字段是否需要分词
        前提是这个字段首先要创建索引。然后如果这个字段的值是不可分割的，那么就不需要分词。例如：ID 
    
    
### Analyzer分析器

    lucene默认提供了标准分词器，但是对中文不友好
    IKAnalyzer： 中文分词器
        扩展词典
        停用词典

### IndexWriterConfig索引写出器配置类        
     1） 设置配置信息：Lucene的版本和分词器类型
     2）设置是否清空索引库中的数据
     
     
### IndexWriter（索引写出器类）

    索引写出工具，作用就是 实现对索引的增（创建索引）、删（删除索引）、改（修改索引）
        indexWriter.addDocument(document);
        
        
### 核心API

    QueryParser 
    1）QueryParser（单一字段的查询解析器）
    2）MultiFieldQueryParser（多字段的查询解析器）
    Query（查询对象，包含要查询的关键词信息）
    1）通过QueryParser解析关键字，得到查询对象
    2)自定义查询对象（高级查询）
    IndexSearch（索引搜索对象，执行搜索功能）
    IndexSearch可以帮助我们实现：快速搜索、排序、打分等功能。
    IndexSearch需要依赖IndexReader类
    查询后得到的结果，就是打分排序后的前N名结果。N可以通过第2个参数来指定
    TopDocs（查询结果对象）    
    通过IndexSearcher对象，我们可以搜索，获取结果：TopDocs对象
    在TopDocs中，包含两部分信息：   
        totalHits:总记录数
        ScoreDoc[] scoreDocs	： 得分文档对象的数组
    ScoreDoc(得分文档对象)
        int doc:文档id
        float score：文档得分
    TermQuery 词条查询
        // 创建词条查询对象
        Query query = new TermQuery(new Term("title", "谷歌地图"));        
    WildcardQuery（通配符查询）
        Query query = new WildcardQuery(new Term("title", "*歌*"));
            ?代表任意一个字符
            *代表任意多个字符
    FuzzyQuery（模糊查询）        
    NumericRangeQuery 数值范围查询
    BooleanQuery（组合查询）组合查询
    
### 修改索引

    A：Lucene修改功能底层会先删除，再把新的文档添加。 
    B：修改功能会根据Term进行匹配，所有匹配到的都会被删除。这样不好 
    C：因此，一般我们修改时，都会根据一个唯一不重复字段进行匹配修改。例如ID 
    D：但是词条搜索，要求ID必须是字符串。如果不是，这个方法就不能用。 
    如果ID是数值类型，我们不能直接去修改。
    可以先手动删除deleteDocuments(数值范围查询锁定ID)，再添加。
    
    
### 删除索引

    // 根据词条进行删除 
    //		writer.deleteDocuments(new Term("id", "1")); 
    // 根据query对象删除,如果ID是数值类型，那么我们可以用数值范围查询锁定一个具体的ID 
    //		Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true); 
    //		writer.deleteDocuments(query);
   
# spring data jpa

## orm思想和hibernate以及jpa的概述和jpa的基本操作    

### orm思想
    
    ORM: Object Relation Mapping对象关系映射
    主要目的：操作object相当于操作数据库表，解放开发人员对于数据库开发的依赖
    建立两个映射关系
        object和table的映射关系
        object中的属性和table中的字段的映射关系
    
    特点：不再注重sql语句的书写
    实现了ORM思想的框架：mybatis，hibernate   
### hibernate框架介绍
 	Hibernate是一个开放源代码的对象关系映射框架，
 		它对JDBC进行了非常轻量级的对象封装，
 		它将POJO与数据库表建立映射关系，是一个全自动的orm框架
### JPA规范
    jpa规范，实现jpa规范，内部是由接口和抽象类组成           
### JPA的操作步骤

    step1、引入POM.XML依赖
    
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.cococi</groupId>
        <artifactId>spring-data-jpa</artifactId>
        <version>1.0-SNAPSHOT</version>
    
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <project.hibernate.version>5.0.7.Final</project.hibernate.version>
        </properties>
    
        <dependencies>
            <!-- junit -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>
    
            <!-- hibernate对jpa的支持包 -->
            <dependency>
                <groupId>org.hibernate</groupId>
                <artifactId>hibernate-entitymanager</artifactId>
                <version>${project.hibernate.version}</version>
            </dependency>
    
            <!-- c3p0 -->
            <dependency>
                <groupId>org.hibernate</groupId>
                <artifactId>hibernate-c3p0</artifactId>
                <version>${project.hibernate.version}</version>
            </dependency>
    
            <!-- log日志 -->
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>1.2.17</version>
            </dependency>
    
            <!-- Mysql and MariaDB -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.6</version>
            </dependency>
        </dependencies>
    
    
    </project>
    
    step2、创建配置文件（位置为classpath目录下的META-INF中persistence.xml）    
    
    <?xml version="1.0" encoding="UTF-8"?>
    <persistence xmlns="http://java.sun.com/xml/ns/persistence"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
        http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
                 version="2.0">
        <!--配置持久化单元
            name：持久化单元名称
            transaction-type：事务类型
                 RESOURCE_LOCAL：本地事务管理
                 JTA：分布式事务管理 -->
        <persistence-unit name="myJpa" transaction-type="RESOURCE_LOCAL">
            <!--配置JPA规范的服务提供商 -->
            <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
            <properties>
                <!-- 数据库驱动 -->
                <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
                <!-- 数据库地址 -->
                <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test" />
                <!-- 数据库用户名 -->
                <property name="javax.persistence.jdbc.user" value="root" />
                <!-- 数据库密码 -->
                <property name="javax.persistence.jdbc.password" value="root" />
    
                <!--jpa提供者的可选配置：我们的JPA规范的提供者为hibernate，所以jpa的核心配置中兼容hibernate的配 -->
                <property name="hibernate.show_sql" value="true" />
                <property name="hibernate.format_sql" value="true" />
                <!--value:create
                          update
                -->
                <property name="hibernate.hbm2ddl.auto" value="update" />
            </properties>
        </persistence-unit>
    </persistence>

    step3、jpa操作流程
    
        1.加载配置文件创建实体管理器工厂
			Persisitence：静态方法（根据持久化单元名称创建实体管理器工厂）
				createEntityMnagerFactory（持久化单元名称）
			作用：创建实体管理器工厂
			
		2.根据实体管理器工厂，创建实体管理器
			EntityManagerFactory ：获取EntityManager对象
			方法：createEntityManager
			* 内部维护的很多的内容
				内部维护了数据库信息，
				维护了缓存信息
				维护了所有的实体管理器对象
				再创建EntityManagerFactory的过程中会根据配置创建数据库表
			* EntityManagerFactory的创建过程比较浪费资源
			特点：线程安全的对象
				多个线程访问同一个EntityManagerFactory不会有线程安全问题
			* 如何解决EntityManagerFactory的创建过程浪费资源（耗时）的问题？
			思路：创建一个公共的EntityManagerFactory的对象
			* 静态代码块的形式创建EntityManagerFactory
			
		3.创建事务对象，开启事务
			EntityManager对象：实体类管理器
				beginTransaction : 创建事务对象
				presist ： 保存
				merge  ： 更新
				remove ： 删除
				find/getRefrence ： 根据id查询
				
			Transaction 对象 ： 事务
				begin：开启事务
				commit：提交事务
				rollback：回滚
		4.增删改查操作
		5.提交事务
		6.释放资源

### 案例步骤

    	i.搭建环境的过程
    		1.创建maven工程导入坐标
    		2.需要配置jpa的核心配置文件
    			*位置：配置到类路径下的一个叫做 META-INF 的文件夹下
    			*命名：persistence.xml
    		3.编写客户的实体类
    		4.配置实体类和表，类中属性和表中字段的映射关系
    		    实体类中的各项配置：
    		        实体类上的注解：
    		            @Entity //声明实体类
    		            @Table(name="cst_customer") //建立实体类和表的映射关系
    		            
    		        实体类中字段上的注解：
    		            @Id//声明当前私有属性为主键
                        //    @GeneratedValue(strategy=GenerationType.SEQUENCE) //序列
                         @GeneratedValue(strategy=GenerationType.IDENTITY) //配置主键的生成策略
                        /*
                        *  TABLE,
                        SEQUENCE,
                        IDENTITY,
                        AUTO;
                        * */
                        @Column(name="cust_id") //指定和表中cust_id字段的映射关系
    		5.保存客户到数据库中
    	ii.完成基本CRUD案例
    		persist ： 保存
    		merge ： 更新
    		remove ： 删除
    		find/getRefrence ： 根据id查询
    		
### 案例演示

    测试方法牵执行
         /**
         * 创建实体管理类工厂，借助Persistence的静态方法获取
         * 		其中传递的参数为持久化单元名称，需要jpa配置文件中指定
         * 	Persistence
         */
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
        //创建实体管理类
        EntityManager em = factory.createEntityManager();
        //获取事务对象
        EntityTransaction tx = em.getTransaction();
        //开启事务
        tx.begin();
        
        执行操作
            添加方法：
            Customer c = new Customer();
            c.setCustName("传智播客");
            em.persist(c);
            
            
            查询方法：find立即加载；getReference延迟加载，获取到的是一个动态代理对象
            em.find(Custeomer.class,1l)
            em.getReference(Customer.class,1L);
            
            删除客户案例
            Customer customer = em.find(Customer.class, 1l);
            em.remove(customer);        
                    
            Customer customer = em.find(Customer.class, 2l);
            customer.setCustAddress("杭州西湖区骆家庄西元一曲");
            em.clear();
            em.merge(customer);
            
         //提交事务
        tx.commit();
        //释放资源
        em.close();
        factory.close();
        
### jpql案例演示


        方法前置
        EntityManager entityManager = JpaUtil.getEntityManager();抽取了公共接口类
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        创建Query对象
            jpsql语法：
                和sql语法及其相似，只不过sql中操作表和字段在jpql中对应的就是实体类和属性
                
                from Customer 查询所有
                from Customer order by custId desc 查询所有按照custId属性降序
                select count (custId) FROM Customer 查询总记录数
                FROM Customer where custName like ? 条件查询
                
        Query query = entityManager.createQuery("from Customer ");
        如果查询到的结果是一个集合调用getResultList
            如果查询到的结果是一个集合调用getResultList，进一步如果想要分页
            query.setFirstResult(2);//limit前?
            query.setMaxResults(2);//limit后?
            query.setParameter(1,"网易%");//条件查询，param1：第n个占位符，param2第n个占位符的值
        如果查询到的结果是一个单行单列getSingleResult
        List resultList = query.getResultList();
        for (Object cust : resultList) {
            System.out.println(cust);
        }
        
        
        
        
        transaction.commit();
        entityManager.close();

### SpringDataJpa orm思想，hibernate，JPA的相关操作
     	案例：客户的基本CRUD
     	i.搭建环境
     		创建工程导入坐标
     		配置spring的配置文件（配置spring Data jpa的整合）
     		
     		
     		
     		
     		
     		
     		编写实体类（Customer），使用jpa注解配置映射关系
     	ii.编写一个符合springDataJpa的dao层接口
     		* 只需要编写dao层接口，不需要编写dao层接口的实现类
     		* dao层接口规范
     			1.需要继承两个接口（JpaRepository，JpaSpecificationExecutor）
     			2.需要提供响应的泛型
     	
     	* 
     		findOne（id） ：根据id查询 
     		    getOne(id): 根据id查询，底层调用getReference延迟加载
     		save(customer):保存或者更新（依据：传递的实体类对象中，是否包含id属性）
     		delete（id） ：根据id删除
     		findAll() : 查询全部        		
### SpringDataJpa案例演示
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations="classpath:application.xml")
    
    public class CustomerDaoTest {
        @Autowired
        private CustomerDao customerDao;//动态代理对象
        方法
        
        customerDao.save(c);
        
        Customer customer = customerDao.findOne(1l);
        
        Customer customer = customerDao.findOne(1l);
        
        Customer customer = customerDao.findOne(1l);
        customer.setCustName("杭州同花顺");
        customerDao.save(customer);
        
        
        customerDao.findAll();
        
        customerDao.count();
        customerDao.exists(1L);
        getOne需要事务注解
        customerDao.getOne(1l);
    }
    
    
### 复杂查询
	i.借助接口中的定义好的方法完成查询
		findOne(id):根据id查询
	ii.jpql的查询方式
		jpql ： jpa query language  （jpq查询语言）
		特点：语法或关键字和sql语句类似
			查询的是类和类中的属性
			
		* 需要将JPQL语句配置到接口方法上
			1.特有的查询：需要在dao接口上配置方法
			2.在新添加的方法上，使用注解的形式配置jpql查询语句
			3.注解 ： @Query
        
         //@Query 使用jpql的方式查询。
            @Query(value="from Customer")
            public List<Customer> findAllCustomer();
        
        
            //@Query 使用jpql的方式查询。?1代表参数的占位符，其中1对应方法中的参数索引
            @Query(value = "from Customer where custName = ?1")
            public Customer findCustomer(String custName);
        
            /**
             * field = ?n
             * field字段的值从方法参数列表的第n个位置取值
             * @param name
             * @param id
             * @return
             */
            @Query(value = "from Customer where custName = ?1 and custId = ?2")
            public Customer findCustomerByCustNameAndCustId(String name,long id);
        
            /**
             * @Query:代表查询，而我们现在是更新操作
             * @Modifying 更新操作
             * @param id
             * @param name
             * update cst_customer set cust_name=? where cust_id=?
             *
             * update Customer set custName = ?2 where custId = ?1
             */
            @Query(value = "update Customer set custName = ?2 where custId = ?1")
            @Modifying
            public void updateByCustNameAndCustId(long id,String name);

        
        
	iii.sql语句的查询
			1.特有的查询：需要在dao接口上配置方法
			2.在新添加的方法上，使用注解的形式配置sql查询语句
			3.注解 ： @Query
				value ：jpql语句 | sql语句
				nativeQuery ：false（使用jpql查询） | true（使用本地查询：sql查询）
					是否使用本地查询
			
            @Query(value="select * from cst_customer",nativeQuery = true)
            public List<Object[]> findAllCustomerBySql();
        
            @Query(value="select * from cst_customer WHERE cust_name LIKE ?1",nativeQuery = true)
            public List<Object[]> findAllCustomerBySql(String name);		
            
            
	iiii.方法名称规则查询 
	
	      以findBy开头，框架本身为我们实现了方法
	      
	      public Customer findByCustName(String custName);
          public List<Customer> findByCustNameLike(String custName);
	      public Customer findByCustNameLikeAndCustIndustry(String custName,String custIndustry);

### Specifications动态查询
    //查询单个对象
    T findOne(Specification<T> var1);
    //查询列表
    List<T> findAll(Specification<T> var1);
    //查询列表，带分页
     Pageable pageable = new PageRequest(0,5);
    Page<T> findAll(Specification<T> var1, Pageable var2);
    //查询列表，带排序
    
    Sort sort = new Sort(Sort.Direction.DESC,"custId");
    List<T> findAll(Specification<T> var1, Sort var2);
    //统计查询
    long count(Specification<T> var1);
    Specification：查询条件
### Specifications动态查询案例	            
     /**自定义查询条件对象
     * 1、实现接口Specification，这里使用了匿名内部类
     * 2、实现toPredicate方法
     */
    Specification<Customer> spec = new Specification<Customer>() {
        /**
         *
         * @param root 获取需要查询的属性
         * @param criteriaQuery
         * @param criteriaBuilder 构造查询条件
                    
                criteriaBuilder将多个条件组合起来
                    criteriaBuilder.and(predicate, predicate1);
                     Predicate predicate = criteriaBuilder.like(custName.as(String.class), "杭州%");模糊查询
                Predicate equal(Expression<?> var1, Expression<?> var2);

                Predicate equal(Expression<?> var1, Object var2);

                Predicate notEqual(Expression<?> var1, Expression<?> var2);

                Predicate notEqual(Expression<?> var1, Object var2);
         * @return
         */
        public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Path<Object> custName = root.get("custName");
            Predicate predicate = criteriaBuilder.equal(custName, "慕课网成员");
            return predicate;
        }
    };
     Customer customer = customerDao.findOne(spec);
### springdatajpa 多表查询

#### SpringDataJpa one2many多表的实体类配置

    one2many
    
        one
            使用@OneToMany注解
                mappedBy：many一方实体类中配置的one一方的属性值
                    private Customer customer;
                    
                cascade：级联级别
                    ALL,全部级联
                    PERSIST,保存级联
                    MERGE,更新级联
                    REMOVE,删除级联
                    REFRESH,
                    DETACH;
                fetch：是否采用延迟加载
                    FetchType.EAGER:立即加载
                    FetchType.LAZY：延迟加载
            @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
            private Set<Linkman> linkMans = new HashSet<Linkman>();

        many一方：
                使用@ManyToOne注解
                    targetEntity：对方的字节码
                使用@JoinColumn注解
                    name:从表（多的一方）的外键，
                    referencedColumnName主表的主键   
                @ManyToOne(targetEntity = Customer.class)
                @JoinColumn(name = "lkm_cust_id",referencedColumnName = "cust_id")
                private Customer customer;
#### SpringDataJpa many2many多表的实体类配置
    
    many2many
        
        many一方：
                使用@ManyToMany注解
                    targetEntity：对方类型
                    cascade：加载机制
                @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
                
                使用@JoinTable注解
                    name：中间表名称
                    joinColumns：
                        @JoinColumn：
                            name：当前对象在中间表中的外键
                            referencedColumnName：当前对象在当前表中的主键
                    inverseJoinColumns：
                        @JoinColumn：
                            name：对方对象在中间表中的外键
                            referencedColumnName：对方对象在其表中的主键
                    
                @JoinTable(name = "sys_user_role",
                        //joinColumns,当前对象在中间表中的外键
                        joinColumns = {@JoinColumn(name = "sys_user_id",referencedColumnName = "user_id")},
                        //inverseJoinColumns，对方对象在中间表的外键
                        inverseJoinColumns = {@JoinColumn(name = "sys_role_id",referencedColumnName = "role_id")}
                )
                
                private Set<Role> roles = new HashSet<Role>();
        many另一方：
                //多对多关系映射
                @ManyToMany(mappedBy="roles")
                private Set<User> users = new HashSet<User>();
    


    
# spring boot

## spring boot简介
    
     1.1、原有spring的优缺点
        优点：J2EE轻量级、依赖注入和面向切面编程EJB
        缺点：配置繁杂、依赖难以管理
     1.2、spring boot概述
        特点：
            为基于Spring的开发提供更快的入门体验
            开箱即用，没有代码生成，也无需XML配置。同时也可以修改默认值来满足特定的需求
            提供了一些大型项目中常见的非功能性特性，如嵌入式服务器、安全、指标，健康检测、外部配置等
            SpringBoot不是对Spring功能上的增强，而是提供了一种快速使用Spring的方式       
     1.3、SpringBoot的核心功能
        起步依赖：其本身继承了一个父工程，该工程中默认定义了许多默认配置
        自动配置
## SpringBoot快速入门
    
    步骤：
        1、创建一个普通maven jar工程
        2、添加pom.xml依赖
                <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>2.0.1.RELEASE</version>
                </parent>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                    </dependency>
                </dependencies>
        3、编写springboot启动类
            @SpringBootApplication
            Application
        4、编写Controller类
            
## SpringBoot快速入门原理分析


    @SpringBootApplication：表明该类是要给springboot启动类
    SpringApplication.run(MySpringBootApplication.class) ：运行该springboot启动类
    
    
## 使用idea开发工具快速构建一个spring boot项目
## SpringBoot原理分析

    在pom.xml文件
    分析spring-boot-starter-parent
        parent节点已经定义好了一部分坐标、依赖管理、插件管理，SpringBoot工程继承spring-boot-starter-parent后已经具备版本锁定等配置了，起步依赖的作用就是进行依赖的管理
    分析spring-boot-starter-web
        web开发要使用的spring-web、spring-webmvc等坐标进行了“打包”
        
    自动配置原理分析：
        @SpringBootApplication注解
            相当于
                @SpringBootConfiguration：等同与@Configuration，既标注该类是Spring的一个配置类    
                @EnableAutoConfiguration：SpringBoot自动配置功能开启
                    @Import(AutoConfigurationImportSelector.class)：引入别的配置文件


## spring boot配置文件

    有两种配置文件类型
        application.properties
        application.yml
        
        
    配置文件可以配置一些spring boot的相关属性
        比如配置tomcat的端口号
            server.port=8888
        修改工程路径
            默认为/
            server.servlet.context-path=demo                        
        还有一些比较常见的配置信息，可以参看官网文档
        
        如何读取配置文件中的信息
            在controller类中可以使用@value
            
            例如application.propertier中配置了：
                person.name=zhangsan
                person.age=12
                
                
            在controller中如何去呢？
                @Value("${person.name}")
                private String name;
                @Value("${person.age}")
                private int age;

## spring boot整合其他技术

### spring boot整合mybatis

        
    添加依赖    
        <!--mybatis起步依赖-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>               
        <!-- MySQL连接驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>    
    
        在application.properties中添加数据量的连接信息
        
        #DB Configuration:
        spring.datasource.driverClassName=com.mysql.jdbc.Driver
        spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8
        spring.datasource.username=root
        spring.datasource.password=root
        
        
        数据库中创建表
        
        创建实体Bean
        
        编写Mapper接口
        
        @Mapper
        public interface UserMapper {
        	public List<User> queryUserList();
        }     
        
       编写Mapper接口的配置文件
       
       在src\main\resources\mapper路径下加入UserMapper.xml配置文件"
       
       <?xml version="1.0" encoding="utf-8" ?>
       <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
       <mapper namespace="com.itheima.mapper.UserMapper">
           <select id="queryUserList" resultType="user">
               select * from user
           </select>
       </mapper>
       
       application.properties中添加如下mybatis信息
       
       #spring集成Mybatis环境
       #pojo别名扫描包
       mybatis.type-aliases-package=com.itheima.domain
       #加载Mybatis映射文件
       mybatis.mapper-locations=classpath:mapper/*Mapper.xml
       
       

### spring boot整合junit


    添加依赖
    
    添加Spring Data JPA的起步依赖
    <!-- springBoot JPA的起步依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    
    添加数据库驱动依赖
    <!-- MySQL连接驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    在application.properties中配置数据库和jpa的相关属性
    #DB Configuration:
    spring.datasource.driverClassName=com.mysql.jdbc.Driver
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8
    spring.datasource.username=root
    spring.datasource.password=root
    ​
    #JPA Configuration:
    spring.jpa.database=MySQL
    spring.jpa.show-sql=true
    spring.jpa.generate-ddl=true
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.hibernate.naming_strategy=org.hibernate.cfg.ImprovedNamingStrategy
    
    
    创建实体配置实体
        使用jpa相关注解
        
    UserRepository接口编写继承JpaRepository       
      
### spring整合redis

    添加依赖
    
        <!-- 配置使用redis启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        
    配置redis的连接信息
    #Redis
    spring.redis.host=127.0.0.1
    spring.redis.port=6379        
     
     
    使用redisTemplate进行测试
    
### spring整合security


    添加依赖
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>       
     
     此时访问任何一个url都会被拦截到   
     
     如果未登录则会进入默认login页面
        默认用户未user
        默认user密码：启动类加载时控制台会显示一个密码：617899c8-8770-4ed7-9776-eac3a1e792f5
        
      
# git
    关于参考文档即可，重点掌握如何使用，尤其时结合idea
## git历史

## git与svn

## git工作流程

## git的安装

## 使用git管理文件版本

    创建版本库
    
    添加文件
        添加文件的过程
        工作区和暂存区
    修改文件
        提交修改
        查看修改历史
        差异比较
        还原修改
    删除文件
    忽略文件语法规范
    
    结合远程仓库进行使用
    
    分支管理
    
    结合idea的使用


