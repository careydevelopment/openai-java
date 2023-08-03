package us.careydevelopment.ai.openai.support;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class ChatCompletionRequestHelper {

    private static final String MODEL = "gpt-3.5-turbo";

    public static ChatCompletionRequest getRequest(final List<ChatMessage> messages) {
        return getRequest(messages, 0d);
    }

    public static ChatCompletionRequest getRequest(final List<ChatMessage> messages, final Double temperature) {
        final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(MODEL)
                .messages(messages)
                .temperature(temperature)
                .build();

        return chatCompletionRequest;
    }
}
