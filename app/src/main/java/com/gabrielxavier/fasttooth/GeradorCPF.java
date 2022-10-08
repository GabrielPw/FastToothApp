package com.gabrielxavier.fasttooth;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeradorCPF {

    ArrayList<String> listaCpf = new ArrayList<>(Arrays.asList("77474394073", "48475231055","42123950025",
            "50795297092","34263863054", "71466625007",
            "64153366064", "09493838056", "80459369083",
            "19690787071", "89354099017", "45808681022",
            "48137624090", "50143981030", "26822389040",
            "24529474070", "39493177084", "80704959011",
            "59437552096", "48247221012", "52882335008",
            "65172721002", "90115395083", "45246481000",
            "14169983068", "23168516066", "47782930022",
            "25763220005", "46564550081", "38160124007",
            "59312516000", "49487905081", "58191844087",
            "02837183075", "10615925022", "84729169018",
            "99017139005", "70242953093", "41368520022",
            "88433565001", "05691707027", "66596390047",
            "45133121067", "99371121092"));

    public String generateCPF(){

        Random random = new Random();

        int indice = random.nextInt(listaCpf.size() - 1);

        String cpfAleatorio = listaCpf.get(indice);
        listaCpf.remove(indice);

        return cpfAleatorio;
    }
}
