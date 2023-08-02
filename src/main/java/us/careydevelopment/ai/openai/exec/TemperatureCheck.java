package us.careydevelopment.ai.openai.exec;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import us.careydevelopment.ai.openai.support.ChatCompletionRequestHelper;
import us.careydevelopment.ai.openai.support.OpenAiServiceHelper;

import java.util.ArrayList;
import java.util.List;

public class TemperatureCheck {

    private static final double TEMPERATURE = 1.0d;

    public static void main(String[] args) {
        final OpenAiService service = OpenAiServiceHelper.getOpenAiService();

        final List<ChatMessage> messages = getMessages();
        final ChatCompletionRequest chatCompletionRequest
                = ChatCompletionRequestHelper.getRequest(messages, TEMPERATURE);

        service.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .forEach(System.out::println);
    }

    private static List<ChatMessage> getMessages() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "You are a helpful assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(),
                "Animals are ");

        messages.add(systemMessage);
        messages.add(userMessage);

        return messages;
    }
}
