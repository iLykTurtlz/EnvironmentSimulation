package utils;

public class PredatorVision extends VisionField {

    public PredatorVision(int x, int y, int range, int orientation) {
        super(x, y, range, orientation);
        calculateField();
    }

    protected void calculateField() {
        field = new int[(range+1)*(range+1)][2];    //(range+1)^2 coordinates
        int k=0;
        switch (orientation)    {
            case 0:
                for (int i=0; i<=range; i++)    {
                    for(int j=0; j<=i; j++) {
                        if (j > 0)  {
                            field[k][0] = x + j;
                            field[k++][1] = y + i;
                            field[k][0] = x - j;
                            field[k++][1] = y + i;
                        } else {
                            field[k][0] = x;
                            field[k++][1] = y + i;
                        }
                        field[k++][1] = y + i;
                    }
                }
                break;
            case 1:



    }







    }




}