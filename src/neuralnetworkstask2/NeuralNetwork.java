/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworkstask2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artur
 */
public class NeuralNetwork {
    
    private Double[] inputs;
    private Double[] outputs;
    private List<Layer> layers;
    
    private static NeuralNetworksBuilder builder =new NeuralNetworksBuilder();

    private NeuralNetwork(Double[] inputs, Double[] outputs, List<Layer> layers) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.layers = layers;
    }
    
    public void study(List<Double[]> data,List<Double[]> rightOutputs, double shift) throws Exception{
      if (data.size()!=rightOutputs.size())
          throw new Exception("different size of data sequence: inputs and outputs");
      for(int j=0;j<1000;j++)
      {
        for (int i=0;i<data.size();i++)
        {
            try {
                startEpoch(data.get(i));
                startBackPropagation(data.get(i),rightOutputs.get(i),shift);              
                } catch (Exception ex) {
                System.err.println(ex.toString());
            }
        }
    }
    }
    
    public void test(List<Double[]> data,List<Double[]> rightOutputs) throws Exception{
      if (data.size()!=rightOutputs.size())
          throw new Exception("different size of data sequence: inputs and outputs");
      int success=0;
      for (int i=0;i<data.size();i++)
      {
          try {
              startEpoch(data.get(i));
              boolean failed=false;
              for (int j=0;j<outputs.length;j++)
              {
                  if (!((outputs[j]>0.5 && rightOutputs.get(i)[j]>0.5) || (outputs[j]==0.5 && rightOutputs.get(i)[j]==0.5) || (outputs[j]<0.5 && rightOutputs.get(i)[j]<0.5)))
                    failed=true;
                  System.out.println("Calculated a="+outputs[j]+" expected a="+rightOutputs.get(i)[j]);
              }
              if (failed==false)
                  success++;
              } catch (Exception ex) {
              System.err.println(ex.toString());
          }
      }
      Double persent=(double)(success)*100/data.size();
      System.out.println("Result:"+persent+"(total: "+data.size()+" passed: "+success+")");
    }
    
    public Double[] startEpoch(Double[] inputs) throws Exception{
        if (this.inputs.length==inputs.length)
        {
            this.inputs=inputs;
            for(int i=0;i<layers.size();i++){
                Layer currentLayer=layers.get(i);
                int currentLayerSize=currentLayer.getPerseptrons().size();
                for (int j=0;j<currentLayerSize;j++){
                    Perseptron currentPerseptron=currentLayer.getPerseptrons().get(j);
                    if (i==0)
                    {
                        int numOfW=currentPerseptron.getW().size();
                        currentPerseptron.setS(0);
                        for (int k=0;k<numOfW;k++)//case for first layer
                        {
                            double w=currentPerseptron.getW().get(k);
                            currentPerseptron.setS(currentPerseptron.getS()+w*this.inputs[k]);
                            System.out.print("");
                        }
                        double f=1/(1+Math.exp(-1*currentPerseptron.getS()));
                        currentPerseptron.setF(f);
                    }
                    else
                    {
                        Layer prevLayer=layers.get(i-1);                   
                        int numOfW=currentPerseptron.getW().size();
                        currentPerseptron.setS(0);//!!!!
                        for (int k=0;k<numOfW;k++)//case for 1++ layer
                        {
                            double w=currentPerseptron.getW().get(k);
                            currentPerseptron.setS(currentPerseptron.getS()+w*prevLayer.getPerseptrons().get(k).getF());
                        }
                        double f=1/(1+Math.exp(-1*currentPerseptron.getS()));
                        currentPerseptron.setF(f);
                        if (i==layers.size()-1)
                        {
                            outputs[j]=f;//!
                        }
                    }
                }
            }
            return outputs;
        }
        else
            throw new Exception("Wrong size of inputs");
    }
    
    public Double[] getInputs() {
    return inputs;
    }

    public Double[] getOutputs() {
        return outputs;
    }
    
    public static NeuralNetworksBuilder getBuilder(){
        return builder;
    }

    private void startBackPropagation(Double[] inputs,Double[] outputs, Double shift) throws Exception{
        if (outputs.length==this.outputs.length)
        {
            for (int i=layers.size()-1;i>=0;i--)
            {
                Layer currentLayer=layers.get(i);
                Layer prevLayer=null;
                if (i<layers.size()-1)
                    prevLayer=layers.get(i+1);
                int layerSize=currentLayer.getPerseptrons().size();
                for (int j=0;j<layerSize;j++)
                {
                    Perseptron currentPerseptron=currentLayer.getPerseptrons().get(j);
                    if (i==layers.size()-1)
                    {
                        currentPerseptron.setErr(outputs[j]-currentPerseptron.getF());
                    }
                    else
                    {
                        double error=0;
                        for (int k=0;k<prevLayer.getPerseptrons().size();k++)//проходимся по персептронам из преведущей сети суммируя их ошибку*нужные веся связанные с нейроном в слое i под номером j
                        {
                            Perseptron perseptron=prevLayer.getPerseptrons().get(k);
                            error=error+perseptron.getErr()*perseptron.getW().get(j);
                        }
                        currentPerseptron.setErr(error);
                    }
                }
            }
            CalculateNewWeights(inputs,shift);
        }
        else
            throw new Exception("Wrong size of outputs");
    }

    private void CalculateNewWeights(Double[] inputs,Double shift) {
        for(int i=0;i<layers.size();i++)
        {
            Layer currLayer=layers.get(i);
            if (i==0)
            {
                for(int j=0;j<currLayer.getPerseptrons().size();j++)
                {
                    Perseptron currentPerseptron=currLayer.getPerseptrons().get(j);
                    for (int k=0;k<currentPerseptron.getW().size();k++)
                    {
                        double dF=currentPerseptron.getF()*(1-currentPerseptron.getF());
                        double newWeight=currentPerseptron.getW().get(k)+currentPerseptron.getErr()*dF*shift*inputs[k];
                        currentPerseptron.getW().set(k, newWeight);
                    }
                }
            }
            else
            {
                Layer prevLayer=layers.get(i-1);
                for(int j=0;j<currLayer.getPerseptrons().size();j++)
                {
                    Perseptron currentPerseptron=currLayer.getPerseptrons().get(j);
                    for (int k=0;k<currentPerseptron.getW().size();k++)
                    {
                        double dF=currentPerseptron.getF()*(1-currentPerseptron.getF());
                        double newWeight=currentPerseptron.getW().get(k)+currentPerseptron.getErr()*dF*shift*prevLayer.getPerseptrons().get(k).getF();
                        currentPerseptron.getW().set(k, newWeight);
                    }
                }
                
            }
        }
    }
    
    public static class NeuralNetworksBuilder{
        
        private int sizeOfInputs=0;
        private int sizeOfOutputs=0;
        private List<Layer> layers=new ArrayList<>();
        private double start=0;
        private double end=0;
        private double defaultWeight=0;
        
        public NeuralNetworksBuilder addLayer(int size){
            List<Perseptron> perseptrons=new ArrayList<>();
            for (int i=1;i<=size;i++)
            {
                Perseptron perseptron=new Perseptron();
                initializePerseptronWithDefaultWeights(layers.size(),defaultWeight,perseptron);
                perseptrons.add(perseptron);
            }
            Layer layer=new Layer(perseptrons);
            layers.add(layer);
            return this;
        }
        
        private void initializePerseptronWithDefaultWeights(int numOfLayer,double defaultWeight,Perseptron perseptron){
            int numOfWeights;
            if (numOfLayer==0)
            {
                numOfWeights=sizeOfInputs;
                for (int i=1;i<=sizeOfInputs;i++)
                    perseptron.getW().add(defaultWeight);
            }
            else
            {
                numOfWeights=layers.get(numOfLayer-1).getPerseptrons().size();
                for (int i=1;i<=numOfWeights;i++)
                    perseptron.getW().add(defaultWeight);
            }
        }
        
        public NeuralNetworksBuilder addLayer(List<Perseptron> perseptrons){
            Layer layer=new Layer(perseptrons);
            layers.add(layer);
            return this;
        }
        
        public NeuralNetworksBuilder setNumderOfInputs(int number){
            sizeOfInputs=number;
            return this;
        }
        
        public NeuralNetworksBuilder setNumderOfOutputs(int number){
            sizeOfOutputs=number;
            return this;
        }

        public double getDefaultWeight() {
            return defaultWeight;
        }

        public NeuralNetworksBuilder setDefaultWeight(double defaultWeight) {
            this.defaultWeight = defaultWeight;
            return this;
        }
        
        public NeuralNetworksBuilder initializeRandomWeight(double start,double end){
            int numOfW=sizeOfInputs;
            for(Layer layer:layers)
            {
                for (Perseptron perseptron:layer.getPerseptrons())
                {
                    perseptron.getW().clear();
                    for (int i=1;i<=numOfW;i++)
                    {
                        perseptron.getW().add((end-start)*Math.random()+start);
                    }
                }
                numOfW=layer.getPerseptrons().size();
            }
            return this;
        }
        
        public NeuralNetwork build(){
            if (layers!=null && sizeOfInputs>0 && sizeOfOutputs>0)
            {
                start=0;
                end=0;
                return new NeuralNetwork(new Double[sizeOfInputs],new Double[sizeOfOutputs], layers);
            }
            else
            {
                sizeOfInputs=0;
                sizeOfOutputs=0;
                start=0;
                end=0;
                layers.clear();
                return null;
            }
        }
    }
        
    
}
