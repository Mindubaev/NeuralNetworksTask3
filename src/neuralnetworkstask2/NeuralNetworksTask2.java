/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworkstask2;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artur
 */
public class NeuralNetworksTask2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            Perseptron perseptron11=new Perseptron(0, 0, 0, Arrays.asList(Double.valueOf(0.13),Double.valueOf(-0.42)));
//            Perseptron perseptron12=new Perseptron(0, 0, 0, Arrays.asList(Double.valueOf(-0.34),Double.valueOf(0.38)));
//            List<Perseptron> layer1=Arrays.asList(perseptron11,perseptron12);
//            Perseptron perseptron21=new Perseptron(0, 0, 0, Arrays.asList(Double.valueOf(0.25),Double.valueOf(-0.2)));
//            Perseptron perseptron22=new Perseptron(0, 0, 0, Arrays.asList(Double.valueOf(0.07),Double.valueOf(0.32)));
//            List<Perseptron> layer2=Arrays.asList(perseptron21,perseptron22);
//            Perseptron perseptron31=new Perseptron(0, 0, 0, Arrays.asList(Double.valueOf(-0.41),Double.valueOf(0.12)));
//            List<Perseptron> layer3=Arrays.asList(perseptron31);
//            List<double[]> inputs=Arrays.asList(new double[]{1,0});
//            List<double[]> outputs=Arrays.asList(new double[]{1});
//            NeuralNetwork neuralNetwork=NeuralNetwork.getBuilder().setNumderOfInputs(2).addLayer(layer1).addLayer(layer2).addLayer(layer3).setNumderOfOutputs(1).build();
//            neuralNetwork.study(inputs, outputs, 0.1);
//            System.out.println("");
            File file=new File("src/testData.txt");
            TrandLine trandLine=new TrandLine(file, 1);
            trandLine.prepareStaticticForNeuroStuding(10, 3);
            List<Double[]> inputs=trandLine.getInputList();
            List<Double[]> outputs=trandLine.getOutputList();
            NeuralNetwork neuralNetwork=NeuralNetwork.getBuilder().setNumderOfInputs(10).addLayer(10).addLayer(10).addLayer(1).setNumderOfOutputs(1).initializeRandomWeight(-0.5, 0.5).build();
            List<Double[]> inputsForStude=inputs.subList(0, Integer.parseInt(String.valueOf(Math.round(inputs.size()*0.8))));
              List<Double[]> outputsForStude=outputs.subList(0, Integer.parseInt(String.valueOf(Math.round(outputs.size()*0.8))));
              List<Double[]> inputsForTest=inputs.subList(Integer.parseInt(String.valueOf(Math.round(inputs.size()*0.8))), inputs.size());
              List<Double[]> outputsForTest=outputs.subList(Integer.parseInt(String.valueOf(Math.round(outputs.size()*0.8))), outputs.size());
            neuralNetwork.study(inputs, outputs, 0.01);
            neuralNetwork.test(inputsForTest, outputsForTest);
        } catch (Exception ex) {
            Logger.getLogger(NeuralNetworksTask2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
