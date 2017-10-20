package cn.itcast.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Title: Lucene
 * @Description:Lucene的开发测试
 * @Company:www.keyonecn.com
 * @author:fzw
 * @date:2017/10/20 10:39
 * @version:1.0
 */
public class LuceneManager
{
    public static void main(String[] args) throws IOException
    {

    }

    @Test
    public void createDump() throws IOException
    {
        //先制定索引库存放的位置
        String dumpPath = "D:\\temp\\1110";
        Directory directory = FSDirectory.open(new File(dumpPath));
        //放在内存中
        //Directory directory = new RAMDirectory();
        //指定分析器
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, standardAnalyzer);
        //创建indexwirte对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //读取歌词并创建文档对象
        File musicDir = new File("E:\\0\\新建文件夹\\新建文件夹\\学习资料\\2015年传智播客JavaEE就业班视频教程\\传智168期JAVA\\2015年传智播客JavaEE就业班视频教程\\day71-Lucene\\00.参考资料\\歌词\\歌词");
        for (File f : musicDir.listFiles())
        {
            //创建Document对象
            Document document = new Document();
            //判断是否是文件
            //文件名
            Field filed = new TextField("filename", f.getName(), Field.Store.YES);
            //文件内容
            Field fileContent = new TextField("content", FileUtils.readFileToString(f), Field.Store.YES);
            //文件路径默认只存储不建立索引
            Field filePath = new StoredField("path", f.getPath());
            //文件的大小
            Field fieldSize = new LongField("size", FileUtils.sizeOf(f), Field.Store.YES);
            //将域添到Document中
            document.add(filed);
            document.add(fileContent);
            document.add(filePath);
            document.add(fieldSize);
            indexWriter.addDocument(document);
        }
        //关闭索引
        indexWriter.close();
    }

    @Test
    public void queryIndex() throws IOException
    {
        String dumPath="D:\\temp\\1110";
        Directory directory=FSDirectory.open(new File(dumPath));
        IndexReader indexReader= DirectoryReader.open(directory);
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        Query query=new TermQuery(new Term("filename","爱"));
        TopDocs topDocs=indexSearcher.search(query,10);
        System.out.println(topDocs.totalHits);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs)
        {
            Document document=indexSearcher.doc(scoreDoc.doc);
            //从document中取出域的内容
            System.out.println(document.get("filename"));
            System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
        }
    }
}