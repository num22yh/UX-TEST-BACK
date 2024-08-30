package com.example.Hamkkenali2Server.search.service;

import com.example.Hamkkenali2Server.search.entity.CancerInfo;
import com.example.Hamkkenali2Server.search.repository.CancerInfoRepository;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CancerInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CancerInfoService.class);

    @Autowired
    private CancerInfoRepository cancerInfoRepository;

    public CancerInfo getDocumentById(int contentId) {
        return cancerInfoRepository.findByContentId(contentId);
    }

    public List<CancerInfo> searchData(String searchTerm) {
        try {
            KoreanAnalyzer analyzer = new KoreanAnalyzer();
            List<String> tokens = tokenize(searchTerm, analyzer);

            Map<Integer, Integer> documentWeights = new HashMap<>();

            for (String token : tokens) {
                List<CancerInfo> matchingDocs = cancerInfoRepository.searchByKeyword(token);
                for (CancerInfo doc : matchingDocs) {
                    documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 50);
                }
            }

            return documentWeights.entrySet().stream()
                    .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                    .map(entry -> cancerInfoRepository.findByContentId(entry.getKey()))
                    .toList();

        } catch (Exception e) {
            logger.error("Error during search", e);
            return new ArrayList<>();
        }
    }

    private List<String> tokenize(String text, KoreanAnalyzer analyzer) throws IOException {
        List<String> tokens = new ArrayList<>();
        try (StringReader stringReader = new StringReader(text)) {
            TokenStream tokenStream = analyzer.tokenStream("field", stringReader);
            CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                tokens.add(termAttr.toString());
            }
            tokenStream.end();
        }
        return tokens;
    }
}

