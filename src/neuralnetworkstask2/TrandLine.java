/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworkstask2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Artur
 */
public class TrandLine {
    
    public List<double[]> statData=new ArrayList<>();
    public List<Map<Double[],Double[]>> preparedData=new ArrayList<>();
    public double a;
    public double b;

    public TrandLine(File file,int columnNum) throws FileNotFoundException, IOException, Exception {
        this.statData=extarctStatisticsFromFile(file, columnNum);
    }
    
    private List<double[]> extarctStatisticsFromFile(File file,int columnNum) throws FileNotFoundException, IOException{
        FileReader reader=new FileReader(file);
        BufferedReader bufferedReader=new BufferedReader(reader);
        int dayCounter=0;
        double[] point;
        List<double[]> statData=new ArrayList<>();
        while (bufferedReader.ready())
        {
            point=new double[2];
            dayCounter++;
            String[] str=bufferedReader.readLine().replace(",", "").trim().split("\"\"");
            if (columnNum>str.length)
                throw new IOException("неверно указан номер калонки");
            point[0]=dayCounter;
            point[1]=Double.parseDouble(str[columnNum]);
            statData.add(point);
        }
        statData=normalizeData(statData);
        return statData;
    }
    
    private List<double[]> normalizeData(List<double[]> data){
        double max=0;
        double min=data.get(0)[0];
        for (double[] point:data)
        {
            if (max<point[1])
            {
              max=point[1];  
            }
            if (min>point[1])
            {
              min=point[1];  
            }
        }
        for (double[] point:data)
        {
             //point[1]=(point[1]-min)/(max-min); 
            point[1]=(point[1])/(max); 
        }    
        return data;
    }
    
    private double builtTradeLine(List<Double[]> statData) throws Exception{
        double averX=0;
        double averY=0;
        double averXY=0;
        double averPowX=0;
        for (Double[] point:statData)
        {
            averX+=point[0];
            averY+=point[1];
            averXY+=point[0]*point[1];
            averPowX+=Math.pow(point[0], 2);
        }
        int size=statData.size();
        if (averX>0)
            averX=averX/size;
        if (averY>0)
            averY=averY/size;
        if (averXY>0)
            averXY=averXY/size;
        if (averPowX-Math.pow(averX, 2)!=0)
        {
            b=(averXY-averX*averY)/(averPowX-Math.pow(averX, 2));
            a=averY-b*averX;
            return b;
        }
        else
            throw new Exception("некорректные входные данные, невозможно построить линию трендов");
    }
    
    public void prepareStaticticForNeuroStuding(int numOfPrevPoint,int numOfNextPoint) throws Exception{
        Queue<Double> inputs=new ArrayBlockingQueue<Double>(numOfPrevPoint) ;
        Queue<Double> pointsForTrandLine=new ArrayBlockingQueue<>(numOfNextPoint);
        List<Double> outputs;
        List<Map<Double[],Double[]>> result=new ArrayList<>();
        if (statData.size()>numOfNextPoint+numOfPrevPoint-1){
            double k=0;
            for (int i=0;i<statData.size();i++)
            {
                double point=statData.get(i)[1];
                if (i<=numOfPrevPoint-1)
                {
                    inputs.add(point);
                }
                else
                {
                    inputs.poll();
                    inputs.add(point);
                }
                if (i>=numOfNextPoint-1)
                {
                    if (i==numOfNextPoint-1)
                        pointsForTrandLine.add(point);
                    else
                    {
                        pointsForTrandLine.poll();
                        pointsForTrandLine.add(point);
                    }
                    k=builtTradeLine(prepareDataForTrandLine(new ArrayList<Double>(pointsForTrandLine)));      
                }
                else
                    pointsForTrandLine.add(point);
                if (i>=numOfNextPoint+numOfPrevPoint-1)
                {
                    outputs=new ArrayList<Double>();
                    outputs.add(k);
                    Map map=new HashMap<>();
                    map.put(inputs.toArray(new Double[numOfPrevPoint]),outputs.toArray(new Double[1]));
                    result.add(map);
                }
            }
            preparedData=result;
        }
        else
            throw new Exception("некорректные входные данные, невозможно построить линию трендов");
    }
    
    public List<Double[]> getInputList(){
        List<Double[]> inputs=new ArrayList<>();
        for (Map map:preparedData)
            inputs.addAll(map.keySet());
        return inputs;
    }
    
    public List<Double[]> getOutputList(){
        List<Double[]> outputs=new ArrayList<>();
        for (Map map:preparedData)
            outputs.addAll(map.values());
        outputs=normalizeOutputs(outputs);
        return outputs;
    }

//    private List<Double> addLast(List<Double> pointsForTrandLine, double point) {
//        for (int i=pointsForTrandLine.size()-1;i>0;i--)
//            pointsForTrandLine.set(i-1, pointsForTrandLine.get(i));
//        pointsForTrandLine.set(pointsForTrandLine.size()-1, point);
//        return pointsForTrandLine;
//    }
    
    private List<Double[]> prepareDataForTrandLine(List<Double> pointsForTrandLine){
        List<Double[]> preparedData=new ArrayList<>();
        for (int i=0;i<pointsForTrandLine.size();i++)
            preparedData.add(new Double[]{new Double(i),pointsForTrandLine.get(i)});
        return preparedData;
    }

    private List<Double[]> normalizeOutputs(List<Double[]> outputs) {
        double max=outputs.get(0)[0];
        double min=outputs.get(0)[0];
        for (Double[] point:outputs)
        {
            if (max<Math.abs(point[0]))
                max=Math.abs(point[0]);

        }
        for (Double[] point:outputs)
        {
                point[0]=0.5+(point[0]/(2*max));
        }
        return  outputs;
    }
    
}
