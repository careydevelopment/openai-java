package us.careydevelopment.ai.openai.exec;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.MutationResult;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.dml.InsertParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.support.EmbeddingsHelper;
import us.careydevelopment.ai.openai.support.MilvusServiceClientHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PersistEmbeddings {

    private static Logger LOG = LoggerFactory.getLogger(PersistEmbeddings.class);

    private static final String INPUT_FILE = "./amazon-food-reviews.txt";
    private static final String EMBEDDINGS_FILE = "./amazon-food-reviews-embeddings.txt";

    private static final String COLLECTION_NAME = "amazon_food_reviews";
    private static final String CONTENT_FIELD = "content";
    private static final String EMBEDDING_FIELD = "content_embedding";
    private static final String ID_FIELD = "review_id";


    public static void main(String[] args) {
        try {
            final MilvusServiceClient client = MilvusServiceClientHelper.getClient();
            //dropCollection(client, COLLECTION_NAME);

            createCollection(client);

            final List<String> lines = loadLinesFromFile();
            final List<List<Float>> embeddings = populateEmbeddings();

            final List<InsertParam.Field> fields = new ArrayList<>();
            fields.add(new InsertParam.Field(CONTENT_FIELD, lines));
            fields.add(new InsertParam.Field(EMBEDDING_FIELD, embeddings));

            final InsertParam insertParam = InsertParam.newBuilder()
                    .withCollectionName(COLLECTION_NAME)
                    .withFields(fields)
                    .build();

            final R<MutationResult> result = client.insert(insertParam);
            System.out.println(result);

            client.close();
        } catch (Exception e) {
            LOG.error("Problem persisting embeddings!", e);
        }
    }

    private static List<String> loadLinesFromFile() throws IOException {
        final List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Path.of(INPUT_FILE))) {
            stream.forEach(line -> list.add(line));
        }

        return list;
    }

    private static List<List<Float>> populateEmbeddings() throws IOException {
        final List<List<Float>> embeddingsFromFile = EmbeddingsHelper.loadFromFileAsFloats(EMBEDDINGS_FILE);
        return embeddingsFromFile;
    }

    private static void createCollection(final MilvusServiceClient client) {
        final FieldType primaryIdType = FieldType.newBuilder()
                .withName(ID_FIELD)
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(true)
                .build();

        final FieldType contentType = FieldType.newBuilder()
                .withName(CONTENT_FIELD)
                .withDataType(DataType.VarChar)
                .withMaxLength(2048)
                .build();

        final FieldType embeddingsType = FieldType.newBuilder()
                .withName(EMBEDDING_FIELD)
                .withDataType(DataType.FloatVector)
                .withDimension(1536)
                .build();

        final CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withDescription("Amazon Reviews")
                .withShardsNum(2)
                .addFieldType(primaryIdType)
                .addFieldType(contentType)
                .addFieldType(embeddingsType)
                .withEnableDynamicField(true)
                .build();

        client.createCollection(createCollectionReq);
    }

    private static void dropCollection(final MilvusServiceClient client, final String collectionName) {
        final R<RpcStatus> status = client.dropCollection(
            DropCollectionParam.newBuilder()
                    .withCollectionName(collectionName)
                    .build()
        );

        System.out.println(status);
    }
}
