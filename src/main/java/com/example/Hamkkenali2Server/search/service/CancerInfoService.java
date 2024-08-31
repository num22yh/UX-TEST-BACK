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
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class CancerInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CancerInfoService.class);

    @Autowired
    private CancerInfoRepository cancerInfoRepository;

    public CancerInfo getDocumentById(int contentId) {
        return cancerInfoRepository.findByContentId(contentId);
    }

    public Page<CancerInfo> searchData(String searchTerm, int page, int size) {
        try {
            // 토큰화
            List<String> tokens = tokenize(searchTerm, initializeAnalyzer());

            // 모든 검색 결과를 병합
            Set<CancerInfo> resultSet = new HashSet<>();
            for (String token : tokens) {
                Page<CancerInfo> pageResults = cancerInfoRepository.searchByKeyword(token, PageRequest.of(0, Integer.MAX_VALUE));
                resultSet.addAll(pageResults.getContent());
            }

            List<CancerInfo> results = new ArrayList<>(resultSet);

            results.sort((c1, c2) -> {
                int score1 = calculateRelevanceScore(c1, tokens);
                int score2 = calculateRelevanceScore(c2, tokens);
                return Integer.compare(score2, score1);
            });

            // 페이징 처리
            int start = Math.min(page * size, results.size());
            int end = Math.min(start + size, results.size());
            if (start >= results.size()) {
                return Page.empty(PageRequest.of(page, size));
            }
            return new PageImpl<>(results.subList(start, end), PageRequest.of(page, size), results.size());

        } catch (IOException e) {
            logger.error("Failed to load custom dictionary or tokenize search term", e);
            return Page.empty();
        } catch (Exception e) {
            logger.error("Error during search", e);
            return Page.empty();
        }
    }

    private KoreanAnalyzer initializeAnalyzer() throws IOException {
        String dictPath = "src/main/resources/custom_dictionary.txt";
        try (Reader reader = new BufferedReader(new FileReader(dictPath))) {
            UserDictionary userDictionary = UserDictionary.open(reader);
            Set<POS.Tag> stopTags = EnumSet.of(
                    POS.Tag.J, POS.Tag.MAG, POS.Tag.MAJ, POS.Tag.E,
                    POS.Tag.IC, POS.Tag.SF, POS.Tag.SP, POS.Tag.SC
            );
            return new KoreanAnalyzer(userDictionary, KoreanTokenizer.DecompoundMode.NONE, stopTags, false);
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
