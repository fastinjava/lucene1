package com.cococi.lucene1;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class LuceneSecond {

    @Test
    public void testAddIndex() throws Exception {

        IndexWriter indexWriter = new IndexWriter(
                FSDirectory.open(new File("D:\\temp\\index").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
        Document document = new Document();
        /**
         *StringField:构建字符串，不会进行分析（分词），会创建索引，是否进行存储
         name field name
         value String value
         stored Store.YES if the content should also be stored
         * public StringField(String name, String value, Store stored)
         */
        document.add(new StringField("orderNo","1111", Field.Store.YES));
        document.add(new StringField("username","Tony", Field.Store.YES));
        document.add(new LongPoint("price",999l));
        document.add(new StoredField("price",999l));
        document.add(new TextField("filename", "新添加的文档", Field.Store.YES));
        document.add(new TextField("content", "新添加的文档的内容", Field.Store.NO));

        indexWriter.addDocument(document);

        indexWriter.close();
    }

}
