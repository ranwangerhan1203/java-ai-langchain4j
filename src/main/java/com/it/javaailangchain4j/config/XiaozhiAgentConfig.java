package com.it.javaailangchain4j.config;

import com.it.javaailangchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class XiaozhiAgentConfig {

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    ChatMemoryProvider chatMemoryProviderXiaozhi() {
        return memoryId -> MessageWindowChatMemory.builder()
            .id(memoryId)
            .maxMessages(20)
            .chatMemoryStore(mongoChatMemoryStore)
            .build();
    }

    @Bean
    ContentRetriever contentRetrieverXiaozhi() {
        List<Document> documents = new ArrayList<>();

        // 服务器绝对路径，和你当前目录完全匹配
        String basePath = "/data/";

        // 加载3个md文件，异常捕获保证不崩溃
        try {
            Document doc1 = FileSystemDocumentLoader.loadDocument(basePath + "医院信息.md");
            documents.add(doc1);
        } catch (Exception e) {
            // 文件不存在时仅打印日志，不影响启动
            System.err.println("文件加载失败: " + basePath + "医院信息.md, 错误: " + e.getMessage());
        }

        try {
            Document doc2 = FileSystemDocumentLoader.loadDocument(basePath + "科室信息.md");
            documents.add(doc2);
        } catch (Exception e) {
            System.err.println("文件加载失败: " + basePath + "科室信息.md, 错误: " + e.getMessage());
        }

        try {
            Document doc3 = FileSystemDocumentLoader.loadDocument(basePath + "神经内科.md");
            documents.add(doc3);
        } catch (Exception e) {
            System.err.println("文件加载失败: " + basePath + "神经内科.md, 错误: " + e.getMessage());
        }

        // 内存向量存储，无文件也能正常启动
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        if (!documents.isEmpty()) {
            EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        }

        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    @Autowired
    private EmbeddingStore embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    ContentRetriever contentRetrieverXiaozhiPincone() {
        return EmbeddingStoreContentRetriever
            .builder()
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .maxResults(1)
            .minScore(0.8)
            .build();
    }
}