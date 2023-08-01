package us.careydevelopment.ai.openai.support;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class ChatCompletionRequestHelper {

    private static final String MODEL = "gpt-3.5-turbo-0613";

    public static ChatCompletionRequest getRequest(final List<ChatMessage> messages) {
        final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(MODEL)
                .messages(messages)
                .build();

        return chatCompletionRequest;
    }
}
