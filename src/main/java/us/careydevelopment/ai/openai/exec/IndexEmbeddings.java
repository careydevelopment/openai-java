package us.careydevelopment.ai.openai.exec;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.index.CreateIndexParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.support.MilvusServiceClientHelper;

public class IndexEmbeddings {

    private static Logger LOG = LoggerFactory.getLogger(IndexEmbeddings.class);

    private static final IndexType INDEX_TYPE = IndexType.IVF_FLAT;
    private static final String INDEX_PARAM = "{\"nlist\":1024}";

    private static final String COLLECTION_NAME = "amazon_food_reviews";
    private static final String INDEX_NAME = "amazon_food_reviews_index";
    private static final String EMBEDDING_FIELD = "content_embedding";

    public static void main(String[] args) {
        final MilvusServiceClient client = MilvusServiceClientHelper.getClient();

        final R<RpcStatus> response = client.createIndex(
                CreateIndexParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .withFieldName(EMBEDDING_FIELD)
                        .withIndexType(INDEX_TYPE)
                        .withMetricType(MetricType.L2)
                        .withExtraParam(INDEX_PARAM)
                        .withIndexName(INDEX_NAME)
                        .withSyncMode(Boolean.TRUE)
                        .build()
        );

        System.out.println(response);

        client.close();
    }
}
