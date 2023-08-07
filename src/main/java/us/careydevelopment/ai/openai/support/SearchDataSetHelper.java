package us.careydevelopment.ai.openai.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.ai.openai.model.SearchDataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SearchDataSetHelper {

    private static Logger LOG = LoggerFactory.getLogger(EmbeddingsHelper.class);

    public static final SearchDataSet createSearchDataSet(final String inputFile, final String embeddingsFile) throws IOException {
        preCreateCheck(inputFile, embeddingsFile);

        final SearchDataSet dataSet = new SearchDataSet();
        dataSet.setInputFile(inputFile);
        dataSet.setEmbeddingsFile(embeddingsFile);

        return dataSet;
    }

    /**
     * This method validates that the input file exists
     * Then, it creates the embeddings file if it doesn't exist
     *
     * @param inputFile
     * @param embeddingsFile
     * @throws IOException
     */
    private static void preCreateCheck(final String inputFile, final String embeddingsFile) throws IOException {
        final Path inputFilePath = Paths.get(inputFile);
        if (Files.notExists(inputFilePath)) {
            throw new IOException("File " + inputFile + " doesn't exist!");
        }

        LOG.debug("Input file path " + inputFile + " exists");

        final Path embeddingsFilePath = Paths.get(embeddingsFile);
        if (Files.notExists(embeddingsFilePath)) {
            //if we get here, the embeddings haven't been saved yet
            //we'll save them now
            LOG.debug("No embeddings file path, creating one...");

            final List<String> list = new ArrayList<>();

            try (Stream<String> stream = Files.lines(inputFilePath)) {
                stream.forEach(line -> list.add(line));
            }

            final boolean saved = EmbeddingsHelper.persistEmbeddings(list, embeddingsFile);
            if (!saved) throw new IOException("Problem saving embeddings!");
            else LOG.debug("Saved embeddings file path successfully");
        } else {
            LOG.debug("Embeddings file path " + embeddingsFile + " already exists");
        }
    }
}
