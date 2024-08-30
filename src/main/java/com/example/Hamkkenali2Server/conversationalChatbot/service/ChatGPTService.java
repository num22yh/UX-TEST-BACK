package com.example.Hamkkenali2Server.conversationalChatbot.service;

import com.example.Hamkkenali2Server.conversationalChatbot.dto.ChatCompletionDto;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface ChatGPTService {

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}