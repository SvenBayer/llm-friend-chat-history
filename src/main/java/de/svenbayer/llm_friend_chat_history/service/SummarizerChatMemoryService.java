package de.svenbayer.llm_friend_chat_history.service;

import de.svenbayer.llm_friend_chat_history.model.conversation.MessageSummaryRequest;
import de.svenbayer.llm_friend_chat_history.model.conversation.MessageWithType;
import de.svenbayer.llm_friend_chat_history.service.process.OllamaProcessService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class SummarizerChatMemoryService {

    private final ChatClient chatClient;
    private final SystemPromptService systemPromptService;
    private final OllamaProcessService ollamaProcessService;

    public SummarizerChatMemoryService(ChatClient.Builder chatClientBuilder, SystemPromptService systemPromptService, OllamaProcessService ollamaProcessService) {
        this.chatClient = chatClientBuilder.build();
        this.systemPromptService = systemPromptService;
        this.ollamaProcessService = ollamaProcessService;
    }

    public MessageWithType createOverallSummaryMessage(MessageSummaryRequest messageSummaryRequest) {
        Message summaryMessage = MessageWithType.createMessage(messageSummaryRequest.getSummaryMessage());
        Message messageToAdd = MessageWithType.createMessage(messageSummaryRequest.getMessageToAddToSummary());
        Prompt overallSummaryPrompt = systemPromptService.getOverallSummaryPrompt(summaryMessage, messageToAdd);

        String summary = chatClient.prompt()
                .user(overallSummaryPrompt.getContents())
                .call()
                .content();
        System.out.println("Overall-Summary: " + summary);

        ollamaProcessService.stopOllamaContainer();

        return MessageWithType.createMessageWithType(MessageType.SYSTEM, summary);
    }

    public MessageWithType summarizeOldMessage(MessageWithType oldDetailledMessage) {
        Message oldMessage = MessageWithType.createMessage(oldDetailledMessage);
        Prompt summarizationPrompt = systemPromptService.getSummarizationPrompt(oldMessage);

        String summary = chatClient.prompt()
                .user(summarizationPrompt.getContents())
                .call()
                .content();
        System.out.println("Summary: " + summary);

        ollamaProcessService.stopOllamaContainer();

        return MessageWithType.createMessageWithType(oldDetailledMessage.getMessageType(), summary);
    }
}
