package us.careydevelopment.ai.openai.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.model.SearchDataSet;
import us.careydevelopment.ai.openai.model.SearchResult;
import us.careydevelopment.ai.openai.util.Calculations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemanticSearchHelper {

    private static Logger LOG = LoggerFactory.getLogger(SemanticSearchHelper.class);

    private static final int DEFAULT_MAX_RESULTS = 5;

    private final SearchDataSet dataSet;
    private final List<String> inputs;
    private final List<List<Double>> dataSetEmbeddings;

    private SemanticSearchHelper(final String inputFile, final String embeddingsFile) throws IOException {
        this.dataSet = SearchDataSetHelper.createSearchDataSet(inputFile, embeddingsFile);
        this.inputs = dataSet.getInputAsList();
        this.dataSetEmbeddings = EmbeddingsHelper.loadFromFile(dataSet.getEmbeddingsFile());
    }

    public static SemanticSearchHelper getSemanticSearchHelper(final String inputFile, final String embeddingsFile) throws IOException {
        return new SemanticSearchHelper(inputFile, embeddingsFile);
    }

    public List<SearchResult> search(final String searchTerm, final int maxResults) {
        //first, get the embedding for a single search term - this returns a list of 1
        final List<Double> searchEmbedding = SearchTermHelper.getEmbeddingForSingleSearchTerm(searchTerm);

        //instantiate an array where we'll store the results
        final List<SearchResult> results = new ArrayList<>();

        //step through all the content we want to search for
        for (int i=0; i<dataSetEmbeddings.size(); i++) {
            //get the cosine similarity between the search term and this part of the content
            final double score = Calculations.cosineSimilarity(dataSetEmbeddings.get(i),
                    searchEmbedding);

            //instantiate a SearchResult object that includes
            //the content and the score
            final SearchResult searchResult = new SearchResult();
            searchResult.setScore(score);
            searchResult.setText(inputs.get(i));

            results.add(searchResult);
        }

        //sort the results - this will be in DESCENDING order
        Collections.sort(results);

        //return the maximum number of results we're looking for
        return results.subList(0, maxResults);
    }

    public List<SearchResult> search(final String searchTerm) {
        return search(searchTerm, DEFAULT_MAX_RESULTS);
    }
}
