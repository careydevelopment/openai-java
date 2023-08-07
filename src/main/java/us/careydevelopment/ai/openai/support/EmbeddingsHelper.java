package us.careydevelopment.ai.openai.support;

import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmbeddingsHelper {

    private static Logger LOG = LoggerFactory.getLogger(EmbeddingsHelper.class);

    private static final String DEFAULT_MODEL = "text-embedding-ada-002";

    private static EmbeddingRequest getEmbeddingRequest(final List<String> inputText) {
        final EmbeddingRequest request = EmbeddingRequest
                .builder()
                .model(DEFAULT_MODEL)
                .input(inputText)
                .build();

        return request;
    }

    public static List<Embedding> getEmbeddings(final List<String> inputText) {
        final OpenAiService service = OpenAiServiceHelper.getOpenAiService();

        final EmbeddingRequest request = getEmbeddingRequest(inputText);

        final List<Embedding> embeddings = service
                .createEmbeddings(request)
                .getData();

        return embeddings;
    }

    public static List<Embedding> getEmbeddingsFromFile(final String filePath) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(filePath))) {
            final List<String> lines = stream.collect(Collectors.toList());
            return getEmbeddings(lines);
        }
    }

    public static boolean persistEmbeddingsFromFile(final String inputPath, final String outputPath) {
        try {
            final List<Embedding> embeddings = getEmbeddingsFromFile(inputPath);
            saveFile(embeddings, outputPath);
        } catch (Exception e) {
            LOG.error("Problem saving embeddings!", e);
            return false;
        }

        return true;
    }

    public static boolean persistEmbeddings(final List<String> inputText, final String pathStr) {
        try {
            final List<Embedding> embeddings = getEmbeddings(inputText);
            saveFile(embeddings, pathStr);
        } catch (Exception e) {
            LOG.error("Problem saving embeddings!", e);
            return false;
        }

        return true;
    }

    private static void saveFile(final List<Embedding> embeddings, final String pathStr) throws IOException {
        final List<String> output = getEmbeddingsAsStringList(embeddings);
        final Path path = Paths.get(pathStr);

        Files.write(path, output);
    }

    static List<String> getEmbeddingsAsStringList(final List<Embedding> embeddings) {
        final List<String> list = embeddings
                .stream()
                .map(e -> e.getEmbedding())
                .map(ld -> ld.stream()
                        .map(dd -> Double.toString(dd))
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());

        return list;
    }

    public static List<List<Double>> loadFromFile(final String pathStr) throws IOException {
        final List<List<Double>> matrix = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(pathStr))) {
            matrix.addAll(stream
                    .map(line -> Arrays.asList(line.split(",")))
                    .map(list -> list.stream().map(s -> Double.parseDouble(s)).collect(Collectors.toList()))
                    .collect(Collectors.toList()));
        }

        return matrix;
    }

    public static List<List<Float>> loadFromFileAsFloats(final String pathStr) throws IOException {
        final List<List<Float>> matrix = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(pathStr))) {
            matrix.addAll(stream
                    .map(line -> Arrays.asList(line.split(",")))
                    .map(list -> list.stream().map(s -> Float.parseFloat(s)).collect(Collectors.toList()))
                    .collect(Collectors.toList()));
        }

        return matrix;
    }
}
