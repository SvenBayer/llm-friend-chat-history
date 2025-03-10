package de.svenbayer.llm_friend_chat_history.model.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageSummaryRequest {
    @NotNull
    private MessageWithType summaryMessage;

    @NotNull
    private MessageWithType messageToAddToSummary;
}