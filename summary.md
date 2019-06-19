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
 ```$xslt
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
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        
```   

    
# spring data jpa

# spring boot

# git