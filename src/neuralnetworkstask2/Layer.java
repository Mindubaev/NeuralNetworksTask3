/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworkstask2;

import java.util.List;

/**
 *
 * @author Artur
 */
public class Layer {
    
    private List<Perseptron> perseptrons;

    public Layer(List<Perseptron> perseptrons) {
        this.perseptrons = perseptrons;
    }

    public List<Perseptron> getPerseptrons() {
        return perseptrons;
    }

    public void setperseptrons(List<Perseptron> perseptrons) {
        this.perseptrons = perseptrons;
    }
    
}
