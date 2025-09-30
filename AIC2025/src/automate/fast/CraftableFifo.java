package automate.fast;

import aic2025.user.Craftable;

public class CraftableFifo {
    public Craftable[] data = {
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null
    };

    public int[] count = new int[Craftable.values().length];
    public int size = 0;

    public void add(Craftable craftable) {
        data[size] = craftable;
        count[craftable.ordinal()]++;
        size++;
    }

    public void pop(){
        count[data[0].ordinal()]--;
        size--;

        for(int i = 0; i < size; i++){
            data[i] = data[i+1];
        }
    }
    public void popAt(int indice){
        count[data[indice].ordinal()]--;
        size--;

        for(int i = indice; i < size; i++){
            data[i] = data[i+1];
        }
    }
}
