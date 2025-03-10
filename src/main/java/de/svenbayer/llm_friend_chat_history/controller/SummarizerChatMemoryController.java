package de.svenbayer.llm_friend_chat_history.controller;

import de.svenbayer.llm_friend_chat_history.model.conversation.MessageSummaryRequest;
import de.svenbayer.llm_friend_chat_history.model.conversation.MessageWithType;
import de.svenbayer.llm_friend_chat_history.service.SummarizerChatMemoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummarizerChatMemoryController {

    private final SummarizerChatMemoryService summarizerChatMemoryService;

    public SummarizerChatMemoryController(SummarizerChatMemoryService summarizerChatMemoryService) {
        this.summarizerChatMemoryService = summarizerChatMemoryService;
    }

    @PostMapping("/createOverallSummaryMessage")
    public MessageWithType createOverallSummaryMessage(@RequestBody MessageSummaryRequest messageSummaryRequest) {
        return summarizerChatMemoryService.createOverallSummaryMessage(messageSummaryRequest);
    }

    @PostMapping("/summarizeOldMessage")
    public MessageWithType summarizeOldMessage(@RequestBody MessageWithType messageFromUser) {
        return summarizerChatMemoryService.summarizeOldMessage(messageFromUser);
    }
}
