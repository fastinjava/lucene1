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

# spring boot

# git