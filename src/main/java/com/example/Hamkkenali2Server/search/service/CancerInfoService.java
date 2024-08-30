package com.example.Hamkkenali2Server.search.service;

import com.example.Hamkkenali2Server.search.entity.CancerInfo;
import com.example.Hamkkenali2Server.search.repository.CancerInfoRepository;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.ko.POS;
import org.apache.lucene.analysis.ko.dict.UserDictionary;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

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
        List<String> tokens;
        try {

            String dictPath = "src/main/resources/custom_dictionary.txt";
            UserDictionary userDictionary;
            try (Reader reader = new BufferedReader(new FileReader(dictPath))) {
                userDictionary = UserDictionary.open(reader);
            }

            Set<POS.Tag> stopTags = EnumSet.of(
                    POS.Tag.J,     // 조사
                    POS.Tag.MAG,   // 일반 부사
                    POS.Tag.MAJ,   // 접속 부사
                    POS.Tag.E,     // 어미
                    POS.Tag.IC,    // 감탄사
                    POS.Tag.SF,    // 마침표, 물음표 등
                    POS.Tag.SP,    // 공백
                    POS.Tag.SC     // 구분자
            );

            KoreanAnalyzer analyzer = new KoreanAnalyzer(
                    userDictionary,
                    KoreanTokenizer.DecompoundMode.NONE,
                    stopTags,
                    false
            );

            tokens = tokenize(searchTerm, analyzer);

            Set<CancerInfo> results = new HashSet<>();
            for (String token : tokens) {
                List<CancerInfo> matchingDocs = cancerInfoRepository.searchByKeyword(token);
                results.addAll(matchingDocs);
            }

            List<CancerInfo> finalResultList = new ArrayList<>(results);

            finalResultList.sort((c1, c2) -> {
                int score1 = calculateRelevanceScore(c1, tokens);
                int score2 = calculateRelevanceScore(c2, tokens);
                return Integer.compare(score2, score1);
            });

            return finalResultList;

        } catch (IOException e) {
            logger.error("Failed to load custom dictionary or tokenize search term", e);
            return new ArrayList<>();
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
        System.out.println(tokens);
        return tokens;
    }

    // 문서의 관련성 점수 계산하기
    private int calculateRelevanceScore(CancerInfo info, List<String> tokens) {
        int score = 0;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            int tokenWeight = 1;

            // 먼저 입력한 단어일수록 가중치를 크게 부여
            if (i == 0) {
                tokenWeight = 20;
            } else if (i == 1) {
                tokenWeight = 10;
            }

            if (info.getCancerName() != null && info.getCancerName().contains(token)) {
                score += tokenWeight * 3; // CancerName과 매칭되면 기본 가중치 3배
            }
            if (info.getCategory() != null && info.getCategory().contains(token)) {
                score += tokenWeight * 2; // Category와 매칭되면 기본 가중치 2배
            }
            if (info.getContent() != null && info.getContent().contains(token)) {
                score += tokenWeight * 1; // Content와 매칭되면 기본 가중치 그대로
            }
        }

        return score;
    }

}
