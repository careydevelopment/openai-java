package us.careydevelopment.ai.openai.exec;

import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.SearchResults;
import io.milvus.grpc.StringArray;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.collection.ReleaseCollectionParam;
import io.milvus.param.dml.SearchParam;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.support.MilvusServiceClientHelper;
import us.careydevelopment.ai.openai.support.SearchTermHelper;

import java.util.ArrayList;
import java.util.List;

public class SemanticSearchInDatabase {

    private static Logger LOG = LoggerFactory.getLogger(SemanticSearchInDatabase.class);

    private static final String COLLECTION_NAME = "amazon_food_reviews";
    private static final String EMBEDDING_FIELD = "content_embedding";
    private static final String CONTENT_FIELD = "content";
    private static final Integer SEARCH_K = 2;
    private static final String SEARCH_PARAM = "{\"nprobe\":10}";

    private static final String SEARCH_TERM = "cats like the weight loss food";

    public static void main(String[] args) {
        final MilvusServiceClient client = MilvusServiceClientHelper.getClient();

        //Milvus handles searches in memory, so we have to load the collection into memory first
        loadCollectionInMemory(client);

        //do the actual search
        final List<String> results = search(client);

        //spit out the results
        results.forEach(r -> {
            System.out.println(r);
        });

        //release the collection
        releaseCollection(client);

        //close the client
        client.close();
    }

    private static void releaseCollection(MilvusServiceClient client) {
        final ReleaseCollectionParam rparam = ReleaseCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build();
        final R<RpcStatus> response = client.releaseCollection(rparam);
        System.out.println(response);
    }

    private static SearchParam getSearchParam() {
        final List<List<Float>> searchEmbeddings = getSearchEmbeddings();

        final List<String> outputFields = List.of(CONTENT_FIELD);

        final SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(outputFields)
                .withTopK(SEARCH_K)
                .withVectors(searchEmbeddings)
                .withVectorFieldName(EMBEDDING_FIELD)
                .withParams(SEARCH_PARAM)
                .build();

        return searchParam;
    }

    private static List<String> search(MilvusServiceClient client) {
        final SearchParam searchParam = getSearchParam();
        final R<SearchResults> respSearch = client.search(searchParam);

        //System.out.println(respSearch);

        final List<String> results = new ArrayList<>();

        final StringArray array = respSearch
                .getData()
                .getResults()
                .getFieldsData(0)
                .getScalars()
                .getStringData();

        for (int j=0; j<array.getDataCount(); j++) {
            final String res = array.getData(j);
            results.add(res);
        }

        return results;
    }

    @NotNull
    private static List<List<Float>> getSearchEmbeddings() {
        final List<Float> searchEmbedding = SearchTermHelper.getEmbeddingForSingleSearchTermAsFloats(SEARCH_TERM);
        final List<List<Float>> searchEmbeddings = new ArrayList<>();
        searchEmbeddings.add(searchEmbedding);

        return searchEmbeddings;
    }

    private static void loadCollectionInMemory(final MilvusServiceClient client) {
        final R<RpcStatus> response = client.loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .build()
        );

        System.out.println(response);
    }
}
