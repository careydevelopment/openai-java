package us.careydevelopment.ai.openai.support;

import com.theokanning.openai.service.OpenAiService;
import us.careydevelopment.ai.openai.util.AiProperties;

public class OpenAiServiceHelper {

    public static OpenAiService getOpenAiService() {
        //make sure you create an application.properties file in src/main/resources
        //and then assign your OpenAI key to the openai.token property
        final String token = AiProperties.get("openai.token");

        final OpenAiService service = new OpenAiService(token);

        return service;
    }
}
