/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sem2.iwi.bayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jjroman
 */
public class NBScores {

    private int positives = 0;
    private int negatives = 0;
    HashMap<NBOutputValue, AtomicInteger> positiveByOutputValue = new HashMap<>();
    HashMap<NBOutputValue, AtomicInteger> negativeByOutputValue = new HashMap<>();

    void addPositive(NBOutputValue nbov) {
        positives++;
        if (positiveByOutputValue.containsKey(nbov)) {
            positiveByOutputValue.get(nbov).incrementAndGet();
        } else {
            positiveByOutputValue.put(nbov, new AtomicInteger(1));
            negativeByOutputValue.put(nbov, new AtomicInteger(0));
        }
    }

    void addNegative(NBOutputValue nbov) {
        negatives++;
        if (negativeByOutputValue.containsKey(nbov)) {
            negativeByOutputValue.get(nbov).incrementAndGet();
        } else {
            negativeByOutputValue.put(nbov, new AtomicInteger(1));
            positiveByOutputValue.put(nbov, new AtomicInteger(0));
        }
    }

    public double getOverallScore() {
        return positives * 1.0 / (negatives + positives * 1.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Overall score: ")
                .append(Math.round(getOverallScore() * 100.0))
                .append("%\n");
        for (Map.Entry<NBOutputValue, AtomicInteger> me : positiveByOutputValue.entrySet()) {
            sb.append("Category ")
                    .append(me.getKey().getName())
                    .append("(")
                    .append(me.getKey().getKey())
                    .append(")")
                    .append(" score is ")
                    .append(((double) (me.getValue().get())) / ((double) (negativeByOutputValue.get(me.getKey()).get() + me.getValue().get())))
                    .append("\n");

        }
        return sb.toString();
    }
}
