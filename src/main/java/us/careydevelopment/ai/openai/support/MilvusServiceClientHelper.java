package us.careydevelopment.ai.openai.support;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import us.careydevelopment.ai.openai.util.AiProperties;

    public class MilvusServiceClientHelper {

        //make sure to set the correct properties in application.properties
        public static MilvusServiceClient getClient() {
            final String host = AiProperties.get("milvus.host");

            //you won't need these properties if you didn't set up Milvus with security
            final String user = AiProperties.get("milvus.user");
            final String password = AiProperties.get("milvus.password");

            final MilvusServiceClient milvusClient = new MilvusServiceClient(
                    ConnectParam.newBuilder()
                            .withHost(host)
                            .withPort(19530)

                            //comment this out if you didn't set up Milvus with security
                            .withAuthorization(user, password)
                            .build()
            );

            return milvusClient;
        }
    }
