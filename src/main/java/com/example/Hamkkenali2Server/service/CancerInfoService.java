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

    // 해당 ID의 문서를 조회하는 메서드
    public CancerInfo getDocumentById(int contentId) {
        return cancerInfoRepository.findByContentId(contentId);
    }

    // 검색어를 이용하여 암 정보를 검색하고 관련 문서를 정렬하여 반환하는 메서드
    public List<CancerInfo> searchData(String searchTerm) {
        try {
            // 한국어 형태소 분석기를 사용하여 검색어를 토큰화
            KoreanAnalyzer analyzer = new KoreanAnalyzer();

            List<String> tokens = new ArrayList<>();

            try (StringReader stringReader = new StringReader(searchTerm)) {
                // 토큰 스트림 생성
                TokenStream tokenStream = analyzer.tokenStream("field", stringReader);
                CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();  // 토큰 스트림 초기화

                // 토큰을 추출하여 리스트에 추가하고 로깅
                while (tokenStream.incrementToken()) {
                    String token = termAttr.toString();
                    tokens.add(token);

                    logger.info("Token: {}", token);
                }

                tokenStream.end();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 문서의 가중치를 저장하는 맵
            Map<Integer, Integer> documentWeights = new HashMap<>();

            // 검색어와 일치하는 문서를 조회하여 가중치 부여 (첫 번째 토큰에 대해 가중치를 크게 부여)
            List<CancerInfo> matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(tokens.get(0), tokens.get(0), tokens.get(0));

            for (CancerInfo doc : matchingDocs) {
                documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 100);
            }

            // 두번째 토큰에 대해 가중치 부여 (가중치 50)
            for (String token : tokens) {
                matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(token, token, token);
                for (CancerInfo doc : matchingDocs) {
                    documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 50);
                }
            }

            // 마지막 토큰에 대해 가중치 부여 (가중치는 1)
            for (String token : tokens) {
                matchingDocs = cancerInfoRepository.findByCancerNameOrCategoryOrContentContains(token, token, token);
                for (CancerInfo doc : matchingDocs) {
                    documentWeights.put(doc.getContentId(), documentWeights.getOrDefault(doc.getContentId(), 0) + 1);
                }
            }

            // 가중치를 기준으로 결과를 정렬한 후 반환
            List<CancerInfo> sortedResults = new ArrayList<>();
            documentWeights.entrySet().stream()
                    .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                    .forEach(entry -> sortedResults.add(cancerInfoRepository.findByContentId(entry.getKey())));

            return sortedResults;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // 예외 상황
        }
    }
}
