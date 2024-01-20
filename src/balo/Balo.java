package balo;

class DoVat {
    String TenDV;
    float TL, GT, DG;
    int PA;

    public DoVat(String tenDV, float tl, float gt) {
        TenDV = tenDV;
        TL = tl;
        GT = gt;
        DG = GT / TL;
        PA = 0;
    }
     public String getTenDV() {
        return TenDV;
    }

    public float getTL() {
        return TL;
    }

    public float getGT() {
        return GT;
    }

    public int getPA() {
        return PA;
    }
}

public class Balo {
    private DoVat[] dsdv;
    private float maxWeight;

    public Balo(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setDoVat(DoVat[] dsdv) {
        this.dsdv = dsdv;
    }


    private void swap(DoVat[] array, int i, int j) {
        DoVat temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public void SortByWeight() {
        for (int i = 0; i < dsdv.length - 1; i++) {
            for (int j = 0; j < dsdv.length - 1 - i; j++) {
                if (dsdv[j].TL <= dsdv[j + 1].TL) {
                } else {
                    swap(dsdv, j, j + 1);
                }
            }
        }
    }
    public void SortByValue() {
        for (int i = 0; i < dsdv.length - 1; i++) {
            for (int j = 0; j < dsdv.length - 1 - i; j++) {
                if (dsdv[j].GT >= dsdv[j + 1].GT) {
                } else {
                    swap(dsdv, j, j + 1);
                }
            }
        }
    }
    public void SortByPrice() {
        for (int i = 0; i < dsdv.length - 1; i++) {
            for (int j = 0; j < dsdv.length - 1 - i; j++) {
                if (dsdv[j].DG >= dsdv[j + 1].DG) {
                } else {
                    swap(dsdv, j, j + 1);
                }
            }
        }
    }

    public void greedy() {
        for (DoVat dv : dsdv) {
            if (maxWeight <= 0) {
                break;
            }
            int quantity = (int) (maxWeight / dv.TL);
            if (quantity > 0) {
                dv.PA = quantity;
                maxWeight -= quantity * dv.TL;
            }
        }
    }

    public void greedyByMaxOnce() {
        for (DoVat dv : dsdv) {
            if (maxWeight <= 0) {
                break;
            }

            if (dv.TL <= maxWeight) {
                dv.PA = 1;
                maxWeight -= dv.TL;
            }
        }
    }

    float getTL() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setWeight(float maxWeight) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    float getMaxWeight() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
