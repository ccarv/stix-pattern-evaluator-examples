package design.unstructured.examples.spe.example.kafkafeed;

/**
 * IndicatorEvaluation
 */
public class IndicatorEvaluation extends Indicator {

    private final String data;

    public IndicatorEvaluation(Indicator indicator, String... data) {
        super.setName(indicator.getName());
        super.setDescription(indicator.getDescription());

        StringBuilder builder = new StringBuilder();

        for (String s : data) {
            builder.append(s).append("; ");
        }

        this.data = builder.substring(0, builder.length() - 2);
    }

    public String getData() {
        return data;
    }
}
