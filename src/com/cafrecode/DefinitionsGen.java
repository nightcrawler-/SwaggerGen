package com.cafrecode;

import org.jibx.schema.codegen.extend.DefaultNameConverter;
import org.jibx.schema.codegen.extend.NameConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by frederick on 2/27/17.
 * creates swagger definitions from ruby models
 */
public class DefinitionsGen {

    public static final int TABLE_NAME_LINE = 3;
    public static final int FIRST_COLUMN_LINE = 5;

    private String modelsFolder;

    public DefinitionsGen(String modelsFolder) {
        this.modelsFolder = modelsFolder;
    }

    public String getDefinitionsString() throws FileNotFoundException {
        File[] files = getInputFiles(modelsFolder);
        String result = "";
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isCompleteRubyModelFile(file)) {
                String yaml = generateYamlDefinition(file);
                result += yaml + "\n";
                System.out.println(yaml);
            }
        }
        return result;
    }

    private File[] getInputFiles(String inputDir) {
        File file = new File(inputDir);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    private boolean isCompleteRubyModelFile(File file) throws FileNotFoundException {
        if (file.getName().endsWith(".rb")) {
            Scanner scanner = new Scanner(file);
            if (scanner.nextLine().contains("# == Schema Information")) {
                scanner.close();
                return true;
            }
            scanner.close();
        }
        System.out.println("invalid model file");
        return false;
    }

    private String generateYamlDefinition(File file) throws FileNotFoundException {
        String data = "";
        Scanner scanner = new Scanner(file);
        int currentLineIndex = 0;

        while (scanner.hasNext()) {
            // System.out.println(data);
            currentLineIndex++; //increment index, so you know where title and first column are
            String currentLine = scanner.nextLine();

            if (currentLine.startsWith("#") && currentLine.split(" ").length > 2 && !currentLine.contains("index_")) {

                if (currentLineIndex == TABLE_NAME_LINE) {
                    String tableName = currentLine.split(" ")[3]; //# Table name: staff
                    tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
                    tableName = getTitle(tableName);
                    data += tableName + ":\n  type: object\n  properties:\n";
                }

                if (currentLineIndex >= FIRST_COLUMN_LINE) {
                    //#  id                        :integer          not null, primary key
                    String properLine = currentLine.replaceAll("\\s+", " ").trim();
                    String title = properLine.split(" ")[1] + ":";
                    String type = properLine.split(" ")[2].replace(":", "");
                    data += "    ";
                    data += title + "\n";
                    type = type.contains("decimal") ? "number" : type;
                    data += type.contains("date") ? getValidDateTimeType(type) : "      type: " + getDataType(type) + "\n";

                }
            }

        }
        return data;

    }

    private String getTitle(String title) {
        NameConverter nameTools = new DefaultNameConverter();
        return nameTools.depluralize(title);
    }

    private String getValidDateTimeType(String format) {
        format = format.equals("datetime") ? "date-time" : format;
        String res = "      type: string\n" +
                "      format: " + format + "\n";
        return res;
    }

    /**
     * Returns string for default datatype incase any strange ones popup. data and all ahve been handled by pther methods
     *
     * @param incomingType
     * @return
     */
    private String getDataType(String incomingType) {
        if (incomingType.equals("string") || incomingType.equals("integer") || incomingType.equals("number")) {
            return incomingType;
        }
        return "string";
    }
}
/*
  Investor:
    type: object
    properties:
      name:
        type: string
        description: Full anme of investor
        */