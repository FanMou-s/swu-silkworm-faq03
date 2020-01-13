package cn.edu.swu.silkworm.faq;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKTokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEngine {

    private static int RESULT_SIZE = 4;
    private RAMDirectory indexDirectory;
    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;

    public SearchEngine() {
        try {
            this.startup();
            System.out.println("Search Engine startup.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startup() throws IOException {
        // 创建索引存储空间
        this.indexDirectory = new RAMDirectory();

        // 初始化索引创建工具
        this.indexWriter = new IndexWriter(
                this.indexDirectory,
                new IKAnalyzer(),
                true,
                IndexWriter.MaxFieldLength.LIMITED
        );

        // 从excel中读取需要检索的数据
        DataSetReader dataSetReader = new DataSetReader();
        List<Question> questionList = dataSetReader.read("2020.1.xlsx");

        // 将数据写入到索引空间中
        for (Question question : questionList) {
            Document doc = new Document();
            Field type = new Field("type", question.getType(), Field.Store.YES, Field.Index.ANALYZED);
            Field ask = new Field("ask", question.getQuestion(), Field.Store.YES, Field.Index.ANALYZED);
            Field keywords = new Field("keywords", question.getKeywordString(), Field.Store.YES, Field.Index.ANALYZED);
            Field answer = new Field("answer", question.getAnswer(), Field.Store.YES, Field.Index.ANALYZED);
            Field combine = new Field("combine", question.flat(), Field.Store.YES, Field.Index.ANALYZED);

            doc.add(type);
            doc.add(ask);
            doc.add(keywords);
            doc.add(answer);
            doc.add(combine);

            this.indexWriter.addDocument(doc);
        }

        // 提交索引创建结果
        this.indexWriter.commit();
        this.indexWriter.close();

        // 创建索引查询器
        this.indexSearcher = new IndexSearcher(this.indexDirectory, true);
    }

    public List<Question> search(String queryString) throws IOException {
        Document[] docs = this.doSearch(this.indexSearcher, queryString, new String[]{"ask", "keywords"}, RESULT_SIZE);
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < docs.length; i++) {
            questions.add(this.doc2Question(docs[i]));
        }
        return questions;
    }

    public Document[] doSearch(IndexSearcher searcher, String queryString, String[] terms, int resultSize) throws IOException {
        TopScoreDocCollector collector = TopScoreDocCollector.create(resultSize, true);
        Query query = IKQueryParser.parseMultiField(terms, this.tokenize(queryString));
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs(0, resultSize).scoreDocs;
        Document[] docs = new Document[hits.length];

        for (int i = 0; i < hits.length; i++) {
            docs[i] = searcher.doc(hits[i].doc);
        }
        return docs;
    }

    private String tokenize(String query_string) {
        StringBuffer sb = new StringBuffer();
        IKTokenizer tokenizer = new IKTokenizer(new StringReader(query_string), false);
        try {
            while (tokenizer.incrementToken()) {
                TermAttribute termAtt = tokenizer.getAttribute(TermAttribute.class);
                sb.append(termAtt.term()).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private Question doc2Question(Document doc) {
        Question question = new Question();
        question.setType(doc.get("type"));
        question.setQuestion(doc.get("ask"));
        question.setKeywords(Arrays.asList(doc.get("keywords").split(",")));
        question.setAnswer(doc.get("answer"));
        return question;
    }

}
