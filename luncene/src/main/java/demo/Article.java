package demo;

import java.io.IOException;
import java.nio.file.Paths;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private Long id;
    private String title;
    private String content;
    private String author;
    private String url;

    /**
     * 创建文档
     *
     * @return
     */
    public Document toDocument() {
        // Lucene存储的格式（Map装的k,v）
        Document doc = new Document();
        // 向文档中添加一个long类型的属性，建立索引
        doc.add(new LongPoint("id", id));
        // 在文档中存储
        doc.add(new StoredField("id", id));
        // 设置一个文本类型，会对内容进行分词，建立索引，并将内容在文档中存储
        doc.add(new TextField("title", title, Store.YES));
        // 设置一个文本类型，会对内容进行分词，建立索引，存在文档中存储 / No代表不存储
        // Store.No只是不在文档中存储
        doc.add(new TextField("content", content, Store.YES));
        // StringField，不分词，建立索引，文档中存储，因为不分词，所以查询时要输入全内容
        doc.add(new StringField("author", author, Store.YES));
        // 不分词，不建立索引，在文档中存储，
        doc.add(new StoredField("url", url));
        return doc;
    }


    public static void main(String[] args) throws Exception {
        add();
        search("单机程序");
        // delete();
    }

    // 索引文档存放目录
    private static String indexPath = "D:\\data\\testluncene\\index";

    // ik中文分词器 userSmart = true 表示尽可能少的分词
    private static Analyzer analyzer = new IKAnalyzer(true);

    /**
     * 新增
     *
     * @throws IOException
     */
    private static void add() throws IOException {
        Article article = new Article();
        // 即使重复也可以
        article.setId(108L);
        article.setAuthor("张三");
        article.setTitle("学习lucene");
        article.setContent("lucene,单机程序！");
        article.setUrl("http://www.baidu.com");
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        // 写入索引的配置，设置了分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 指定了写入数据目录和配置
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        // 创建一个文档对象
        Document document = article.toDocument();
        // 通过IndexWriter写入
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
     * lucene的update比较特殊，update的代价太高，先删除，然后在插入
     *
     * @throws Exception
     */
    public void update() throws Exception {
        delete();
        add();
    }

    /**
     * 删除
     *
     * @throws Exception
     */
    public static void delete() throws Exception {
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        // Term词条查找，内容必须完全匹配，不分词
        // indexWriter.deleteDocuments(new Term("content", "学好"));
        // 以分词器作为查询条件
        // QueryParser parser = new QueryParser("title", analyzer);
        // Query query = parser.parse("大数据老师");
        // LongPoint是建立索引的 范围查找
        // Query query = LongPoint.newRangeQuery("id", 99L, 120L);
        // 等值查找
        Query query = LongPoint.newExactQuery("id", 108L);
        indexWriter.deleteDocuments(query);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 查询
     *
     * @param queryStr 要查询的词
     * @throws Exception
     */
    public static void search(String queryStr) throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        // 索引查询器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        // 创建一个查询条件解析器 "content"表示从content中查找
        QueryParser parser = new QueryParser("content", analyzer);
        // 对查询条件进行解析
        Query query = parser.parse(queryStr);
        // TermQuery将查询条件当成是一个固定的词
        // Query query = new TermQuery(new Term("url", "http://www.edu360.cn/a10010"));
        // 在【索引库】中进行查找 10 表示查找前10个
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 获取到查找到的文文档ID和得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        // 释放资源
        directoryReader.close();
    }

    /**
     * 可以从多个字段中查找
     *
     * @throws Exception
     */
    public void multiField() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        //多字段数组
        String[] fields = {"title", "content"};
        //多字段的查询转换器
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse("三");
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        directoryReader.close();
    }

    /**
     * 查找全部的数据
     *
     * @throws Exception
     */
    public void matchAll() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        Query query = new MatchAllDocsQuery();
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        directoryReader.close();
    }


    /**
     * 布尔查询，可以组合多个查询条件
     *
     * @throws Exception
     */
    public void booleanQuery() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        Query query1 = new TermQuery(new Term("title", "三"));
        Query query2 = new TermQuery(new Term("content", "a"));
        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);//必须满足
        BooleanClause bc2 = new BooleanClause(query2, BooleanClause.Occur.MUST_NOT);//必须不满足
        BooleanQuery boolQuery = new BooleanQuery.Builder().add(bc1).add(bc2).build();
        System.out.println(boolQuery);
        TopDocs topDocs = indexSearcher.search(boolQuery, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        directoryReader.close();
    }

    /**
     * 非连续范围查找, 相当于in , or
     *
     * @throws Exception
     */
    public void queryParser() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        //创建一个QueryParser对象。参数1：默认搜索域 参数2：分析器对象。
        QueryParser queryParser = new QueryParser("title", new StandardAnalyzer());
        //Query query = queryParser.parse("数据");
        Query query = queryParser.parse("title:学好 OR title:学习");
        System.out.println(query);
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        directoryReader.close();
    }

    /**
     * 连续范围查找, 相当于< , >
     *
     * @throws Exception
     */
    public void rangeQuery() throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        Query query = LongPoint.newRangeQuery("id", 107L, 108L);
        System.out.println(query);
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        forech(scoreDocs, indexSearcher);
        directoryReader.close();
    }

    /**
     * 遍历打印结果
     *
     * @param scoreDocs
     * @param indexSearcher
     * @throws IOException
     */
    public static void forech(ScoreDoc[] scoreDocs, IndexSearcher indexSearcher) throws IOException {
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            Article article = Article.parseArticle(document);
            System.out.println(article);
        }
    }

    /**
     * Document 文档 转 Article 类
     *
     * @param doc
     * @return
     */
    public static Article parseArticle(Document doc) {
        Long id = Long.parseLong(doc.get("id"));
        String title = doc.get("title");
        String content = doc.get("content");
        String author = doc.get("author");
        String url = doc.get("url");
        Article article = new Article(id, title, content, author, url);
        return article;
    }
}
