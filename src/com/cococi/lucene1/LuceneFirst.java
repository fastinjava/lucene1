package com.cococi.lucene1;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class LuceneFirst {
    @Test
    public void createIndex () throws Exception {
//        第一步：创建一个java工程，并导入jar包。
//        第二步：创建一个indexwriter对象。
//        1）指定索引库的存放位置Directory对象
        Directory directory = FSDirectory.open(new File("D:\\temp\\index").toPath());
//        2）指定一个IndexWriterConfig对象。
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
//        第二步：创建document对象。
        File dir = new File("D:\\temp\\searchsource");
        for (File file : dir.listFiles()) {
            String fileName = file.getName();
            String filePath = file.getPath();
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            long fileSize = FileUtils.sizeOf(file);
//        第三步：创建field对象，将field添加到document对象中。
            //创建文件名域
            //第一个参数：域的名称
            //第二个参数：域的内容
            //第三个参数：是否存储
            Field fileNameField = new TextField("filename", fileName, Field.Store.YES);
            //文件内容域
            Field fileContentField = new TextField("content", fileContent, Field.Store.YES);
            //文件路径域（不分析、不索引、只存储）
            Field filePathField = new TextField("path", filePath, Field.Store.YES);
            //文件大小域
            Field fileSizeField = new TextField("size", fileSize + "", Field.Store.YES);

            //创建document对象
            Document document = new Document();
            document.add(fileNameField);
            document.add(fileContentField);
            document.add(filePathField);
            document.add(fileSizeField);
            //创建索引，并写入索引库
//        第四步：使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
            indexWriter.addDocument(document);

        }

//        第五步：关闭IndexWriter对象。

        indexWriter.close();


    }

    @Test
    public void searchIndex() throws Exception {
//        第一步：创建一个Directory对象，也就是索引库存放的位置。
        Directory directory = FSDirectory.open(new File("D:\\temp\\index").toPath());
//        第二步：创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(directory);

//        第三步：创建一个indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//        第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
        Query query = new TermQuery(new Term("filename", "apache"));
//        第五步：执行查询。
        TopDocs topDocs = indexSearcher.search(query, 10);
//        第六步：返回查询结果。遍历查询结果并输出。
        System.out.println("查询结果的总条数："+ topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc属性就是document对象的id
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("filename"));
            //System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("-------------------------");
        }

//        第七步：关闭IndexReader对象
        indexReader.close();
    }
    @Test
    public void analyzer() throws Exception {
        //创建一个Analyzer，StandardAnalyzer子类实现
        Analyzer analyzer = new IKAnalyzer();
        //调用分析器对象的tokenStream方法获得tokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "Eclipse的编辑功能非常强大,掌握了Eclipse快捷键功能,能够大大提高开发效率。");
        //向tokenStream中设置一个引用，相当于一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //调用tokenStream的reset方法，重置指针
        tokenStream.reset();
        //while循环调用显示结果
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }

        tokenStream.close();
    }

}
