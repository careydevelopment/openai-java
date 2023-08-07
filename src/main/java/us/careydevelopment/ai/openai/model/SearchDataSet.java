package us.careydevelopment.ai.openai.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SearchDataSet {

    private String inputFile;
    private String embeddingsFile;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getEmbeddingsFile() {
        return embeddingsFile;
    }

    public void setEmbeddingsFile(String embeddingsFile) {
        this.embeddingsFile = embeddingsFile;
    }

    public List<String> getInputAsList() throws IOException {
        final List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {
            stream.forEach(line -> list.add(line));
        }

        return list;
    }
}
