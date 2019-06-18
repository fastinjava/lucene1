package com.cococi.lucene1;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;

public class LuceneFirst {
    @Test
    public void createIndex () throws Exception {
//        第一步：创建一个java工程，并导入jar包。
//        第二步：创建一个indexwriter对象。
//        1）指定索引库的存放位置Directory对象
        Directory directory = FSDirectory.open(new File("D:\\temp\\index").toPath());
//        2）指定一个IndexWriterConfig对象。
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
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
}
