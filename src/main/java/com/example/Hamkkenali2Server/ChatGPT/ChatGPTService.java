package com.example.Hamkkenali2Server.ChatGPT;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface ChatGPTService {

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}