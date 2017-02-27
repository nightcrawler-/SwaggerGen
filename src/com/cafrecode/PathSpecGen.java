package com.cafrecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by frederick on 2/27/17.
 * Read through rake routes output, generate standard pathand verb definitions for swagger.
 * <p>
 * **********************example*******************************8
 * /investors:
 * get:
 * summary: Investors
 * description: |
 * The Investors endpoint returns a list of all registered investors. Requires authorizations.
 * tags:
 * - Investors
 * responses:
 * 200:
 * description: An array of investors
 * schema:
 * type: array
 * items:
 * $ref: '#/definitions/Investor'
 * default:
 * description: Unexpected error
 * schema:
 * $ref: '#/definitions/Error'
 * <p>
 * Have individual methods for get(list), post, (:id -->) get, put, patch, delete. sample above is get (list)
 * <p>
 * api_investors GET    /api/investors(.:format)                                api/investors#index {:format=>:json}
 * POST   /api/investors(.:format)                                api/investors#create {:format=>:json}
 * api_investor  GET    /api/investors/:id(.:format)                            api/investors#show {:format=>:json}
 * PATCH  /api/investors/:id(.:format)                            api/investors#update {:format=>:json}
 * PUT    /api/investors/:id(.:format)                            api/investors#update {:format=>:json}
 * DELETE /api/investors/:id(.:format)                            api/investors#destroy {:format=>:json}
 */
public class PathSpecGen {
    public static final int FIRSTLINE = 1;
    private String rakeResultFilePath = "";

    private HashMap<String, List<String>> pathVerbs = new HashMap<>();

    public PathSpecGen(String rakeResultFilePath) {
        this.rakeResultFilePath = rakeResultFilePath;
    }

    public void init() throws FileNotFoundException {
        if (isValidRakeRoutesOutFile(new File(rakeResultFilePath)))
            getURIVerbPairs(new File(rakeResultFilePath));
        else
            System.out.println("invalid file");

        unloadPathVerbs(pathVerbs);
    }

    //validate valid rake routes output
    //list paths with corresponding verbs
    //get verbs for each path

    private String getURIVerbPairs(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        int index = 0;
        while (scan.hasNext()) {
            String currentLine = scan.nextLine().replaceAll("\\s+", " ").trim();
            index++;
            if (index > 1) {
                String[] split = currentLine.split(" ");
                //len > 2, verb on 0, else on 1
                String verb = split.length > 4 ? split[1] : split[0];
                String uri = (split.length > 4 ? split[2] : split[1]).replace(":id", "{id}").replace("(.:format)", "").replace("/api", "");

                if (pathVerbs.get(uri) == null) {
                    pathVerbs.put(uri, new ArrayList<>());
                }
                pathVerbs.get(uri).add(verb);
            }
        }
        return "";
    }

    private void unloadPathVerbs(HashMap<String, List<String>> pathVerbs) {

        Iterator<Map.Entry<String, List<String>>> iterator = pathVerbs.entrySet().iterator();
        for (String key : pathVerbs.keySet()) {
            List<String> verbs = pathVerbs.get(key);
            System.out.println(key);
            for (String verb : verbs) {
                System.out.println("     " + verb);
            }
        }

    }

    private boolean isValidRakeRoutesOutFile(File file) throws FileNotFoundException {
        //first line should contain: Prefix Verb   URI Pattern   Controller#Action
        Scanner scan = new Scanner(file);
        return scan.nextLine().contains("URI Pattern");
    }
    //get from pre-defined template files
    //replace marked sections with relevant content
    //emphasis on standard paths i.e. those directly mapped to resources.
    //filter down to specialized paths (might have to eb done manually?)
    //14228344652
    private void getPathSpec(VERB verb, String uri) {

    }

    private

    enum VERB {GET, POST, PUT, PATCH, DELETE}
}
