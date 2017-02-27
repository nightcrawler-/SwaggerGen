package com.cafrecode;

import java.io.FileNotFoundException;

public class Main {
    public static String inDir, outDir;
    public static final int TABLE_NAME_LINE = 3;
    public static final int FIRST_COLUMN_LINE = 5;

    public static void main(String[] args) throws FileNotFoundException {
        inDir = "/home/frederick/Dev/RubymineProjects/ti/Samaritan/app/models";
        String rakeOut = "/home/frederick/Desktop/routes.rt";
        // outDir = args[1];

       // String res = new DefinitionsGen(inDir).getDefinitionsString();
        new PathSpecGen(rakeOut).init();
    }


}



