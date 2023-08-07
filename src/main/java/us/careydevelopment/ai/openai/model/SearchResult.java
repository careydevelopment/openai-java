package us.careydevelopment.ai.openai.model;

public class SearchResult implements Comparable<SearchResult> {

    private String text;
    private Double score;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(final SearchResult o) {
        return Double.compare(o.getScore(), score);
    }
}
