package com.example.Hamkkenali2Server.service;

import com.example.Hamkkenali2Server.entity.CancerInfo;
import com.example.Hamkkenali2Server.entity.CancerInfoRepository;
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

            List<String> tokens = new ArrayList<>();

            try (StringReader stringReader = new StringReader(searchTerm)) {

                TokenStream tokenStream = analyzer.tokenStream("field", stringReader);
                CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();


                while (tokenStream.incrementToken()) {
                    String token = termAttr.toString();
                    tokens.add(token);

                    logger.info("Token: {}", token);
                }

                tokenStream.end();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Map<Integer, Integer> documentWeights = new HashMap<>();

            List<CancerInfo> matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(tokens.get(0), tokens.get(0), tokens.get(0));

            for (CancerInfo doc : matchingDocs) {
                documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 100);
            }


            for (String token : tokens) {
                matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(token, token, token);
                for (CancerInfo doc : matchingDocs) {
                    documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 50);
                }
            }

            for (String token : tokens) {
                matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(token, token, token);
                for (CancerInfo doc : matchingDocs) {
                    documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 1);
                }
            }

            List<CancerInfo> sortedResults = new ArrayList<>();
            documentWeights.entrySet().stream()
                    .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                    .forEach(entry -> sortedResults.add(cancerInfoRepository.findByContentId(entry.getKey())));

            return sortedResults;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
