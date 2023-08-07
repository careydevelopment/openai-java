package us.careydevelopment.ai.openai.support;

import com.theokanning.openai.embedding.Embedding;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTermHelper {

    /**
     * This method makes it easy to get Embeddings for a single search term
     * @param searchTerm
     * @return
     */
    public static List<Double> getEmbeddingForSingleSearchTerm(final String searchTerm) {
        final List<String> searchTermAsList = Arrays.asList(searchTerm);
        final List<Embedding> embeddings = EmbeddingsHelper.getEmbeddings(searchTermAsList);

        //should only get 1 result for a single search term
        return embeddings
                .get(0)
                .getEmbedding()
                .stream()
                .collect(Collectors.toList());
    }

    public static List<Float> getEmbeddingForSingleSearchTermAsFloats(final String searchTerm) {
        final List<String> searchTermAsList = Arrays.asList(searchTerm);
        final List<Embedding> embeddings = EmbeddingsHelper.getEmbeddings(searchTermAsList);

        //should only get 1 result for a single search term
        return embeddings
                .get(0)
                .getEmbedding()
                .stream()
                .map(d -> d.floatValue())
                .collect(Collectors.toList());
    }
}
