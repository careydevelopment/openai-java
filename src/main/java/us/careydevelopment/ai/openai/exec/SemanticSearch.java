package us.careydevelopment.ai.openai.exec;

import com.theokanning.openai.embedding.Embedding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.model.SearchResult;
import us.careydevelopment.ai.openai.support.EmbeddingsHelper;
import us.careydevelopment.ai.openai.support.SemanticSearchHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SemanticSearch {

    private static Logger LOG = LoggerFactory.getLogger(SemanticSearch.class);

    private static final String INPUT_FILE = "./amazon-food-reviews.txt";

    private static final String EMBEDDINGS_FILE = "./amazon-food-reviews-embeddings.txt";

    private static final String SEARCH_TERM = "good taffy";
    //private static final String SEARCH_TERM = "great spicy sauce";
    //private static final String SEARCH_TERM = "cats like the weight loss food";

    public static void main(String[] args) {
        try {
            //first instantiate the SemanticSearchHelper class
            //this will create the embeddings file if it doesn't exist already
            final SemanticSearchHelper semanticSearchHelper = SemanticSearchHelper.getSemanticSearchHelper(INPUT_FILE, EMBEDDINGS_FILE);

            //then we'll perform the actual search
            final List<SearchResult> results = semanticSearchHelper.search(SEARCH_TERM);

            //finally spit out the results
            results.forEach(result -> System.out.println(result.getText() + " " + result.getScore()));
        } catch (Exception e) {
            LOG.error("Problem with semantic search!", e);
        }
    }

    private static List<String> indexContent() throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(INPUT_FILE))) {
            final List<String> lines = stream.collect(Collectors.toList());
            return lines;
        }
    }
}
