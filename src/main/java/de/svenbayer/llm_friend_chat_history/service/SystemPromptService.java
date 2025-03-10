package de.svenbayer.llm_friend_chat_history.service;

import de.svenbayer.llm_friend_chat_history.config.ConfigProperties;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemPromptService {

    private final ConfigProperties configProperties;

    @Value("classpath:templates/summarization-prompt.st")
    private Resource summarizationPromptTemplate;

    @Value("classpath:templates/overall-summary-prompt.st")
    private Resource overallSummaryPromptTemplate;

    public SystemPromptService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public Prompt getSummarizationPrompt(Message oldDetailledMessage) {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        params.put("maxLength", configProperties.maxSummarizationLength);
        params.put("speaker", oldDetailledMessage.getMessageType().getValue());
        params.put("detailedConversation", oldDetailledMessage.getText());
        return createPromptFromTemplate(this.summarizationPromptTemplate, params);
    }

    public Prompt getOverallSummaryPrompt(Message existingSummary, Message messageToAddToSummary) {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        params.put("maxLength", configProperties.maxSummarizationLength);
        params.put("speaker", messageToAddToSummary.getMessageType().getValue());
        params.put("existingSummary", existingSummary.getText());
        params.put("detailedConversation", messageToAddToSummary.getText());
        return createPromptFromTemplate(this.overallSummaryPromptTemplate, params);
    }

    private Prompt createPromptFromTemplate(Resource templateResource, Map<String, Object> params) {
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);
        return promptTemplate.create(params);
    }
}
