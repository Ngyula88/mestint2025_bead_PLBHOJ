package com.amoba;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmobaAI {
    private BasicNetwork network;
    private static final String MODEL_FILE = "amoba_ai.eg";

    public AmobaAI() {
        if (new File(MODEL_FILE).exists()) {
            network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(MODEL_FILE));
        } else {
            network = new BasicNetwork();
            network.addLayer(new BasicLayer(null, true, 9));
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 12));
            network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 9));
            network.getStructure().finalizeStructure();
            network.reset();
        }
    }

    public int chooseMove(int[] board) {
        BasicMLData input = new BasicMLData(normalize(board));
        double[] output = network.compute(input).getData();

        int best = -1;
        double bestScore = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0 && output[i] > bestScore) {
                best = i;
                bestScore = output[i];
            }
        }

        if (best == -1) {
            List<Integer> free = new ArrayList<>();
            for (int i = 0; i < 9; i++) if (board[i] == 0) free.add(i);
            return free.get(new Random().nextInt(free.size()));
        }
        return best;
    }

    public void train(List<int[]> history) {
        if (history.isEmpty()) return;

        List<double[]> inputs = new ArrayList<>();
        List<double[]> outputs = new ArrayList<>();

        for (int[] state : history) {
            double[] in = normalize(state);
            double[] out = new double[9];
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) out[i] = 0.5;
                else out[i] = 0.1;
            }
            inputs.add(in);
            outputs.add(out);
        }

        BasicMLDataSet trainingSet = new BasicMLDataSet(inputs.toArray(new double[0][]), outputs.toArray(new double[0][]));
        MLTrain trainer = new Backpropagation(network, trainingSet);

        for (int epoch = 0; epoch < 100; epoch++) {
            trainer.iteration();
            if (trainer.getError() < 0.01) break;
        }

        EncogDirectoryPersistence.saveObject(new File(MODEL_FILE), network);
    }

    private double[] normalize(int[] board) {
        double[] norm = new double[9];
        for (int i = 0; i < 9; i++) {
            norm[i] = (board[i] + 1) / 2.0;
        }
        return norm;
    }
}
