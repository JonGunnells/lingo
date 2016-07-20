package com.theironyard;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theironyard.entities.Article;
import com.theironyard.entities.Dictionary;
import com.theironyard.entities.User;
import com.theironyard.services.DictionaryRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.h2.tools.Server;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.theironyard.services.ArticleRepository;
import com.theironyard.services.UserRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

@RestController
public class LingoRestController {

    @Autowired
    UserRepository users;

    @Autowired
    ArticleRepository articles;

    @Autowired
    DictionaryRepository dictionaries;

    @PostConstruct
    public void init() throws SQLException {
        Server.createWebServer().start();
    }


    @RequestMapping(path="/", method = RequestMethod.GET)
    public Iterable<User> findUser(){
        return users.findAll();
    }

    @PostConstruct
    public void scrapeAPIResults() throws IOException {
        parseDictionary();               //Brings language dictionary into the DB if it isn't already there.

        String apiURL = "https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=289858bf10514c09b02e561994f4ab45";   // The Technology API url
        String returnedJson = apiRequest(apiURL);                        //setting the returned Json string to a String object for use.

        ObjectMapper mapper = new ObjectMapper();
        JsonNode fullJsonReturn = mapper.readValue(returnedJson, JsonNode.class);    //converts Json String into a Node for use with Jackson

        JsonNode results = fullJsonReturn.get("results");                //Creating a sub-node containing only the "results" portion of the original json file

        for(JsonNode result : results) {                                 // For every Result in the Results Node

            JsonNode title = result.findValue("title");                 // grab the title
            if (articles.findByTitle(title.toString()) != null) {       // use the title to see if it's in the DB, if it's there move on, otherwise add it
                continue;
            } else {
                JsonNode returnedURL = result.findValue("url");          // grab the url
                JsonNode author = result.findValue("byline");            // grab the byline - AKA the author

                String urlClean = returnedURL.toString();                 // The returned url has an extra set of "" that need to be removed before it can be used to get the article contents
                urlClean = urlClean.substring(1, urlClean.length() - 1); // this line drops the extra quotes by dropping the first and last character of the string

                Document doc = Jsoup.connect(urlClean).get();             // This scrapes the provided webpage and puts it in a document Object for use with Jsoup
                String content = doc.select("p").text();                  // this line grabs only the content from the css paragraph tags
                content = content.substring(27, content.length());        // Removes the first few characters because they are consistently gibberish or advertisements

                Article article = new Article(title.toString(), returnedURL.toString(), author.toString(), content.toString());  //creates an Article Object from the information grabbed above

                if (article.getContent().length() > 25000) {                 //Checking to make sure it will fit the confines of a cell in the DB
                    article.setContent(article.getContent().substring(0, 24999));
                    System.out.println("damn thats a big article");
                }
                articles.save(article);                                    //Saving the article to the Repo
            }
        }
        wordReplacement();
    }
    public static String apiRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()){
            return response.body().string();

        }
    }


    public int randomNum(){
        int n = (int) (Math.random() * (dictionaries.count()+1));
        return n;
    }


    public void wordReplacement(){
        for (Article article: articles.findAll()){
            if (article.getSpan1()!= null){             //if spanish has already been injected, move onto the next article
                System.out.println("Its not so null");
                continue;
            }else {                                     //otherwise, get to hotswappin'
                String spanishArticle = "";
                int count = 0;
                while (count <= 6) {
                    int seedValue = randomNum();
                    if (article.getContent().contains(dictionaries.findOne(seedValue).getEnglish())) {   //if the article contains the randomly selected english word from the language dictionary....
                        spanishArticle = article.getContent().replace(dictionaries.findOne(seedValue).getEnglish(), dictionaries.findOne(seedValue).getSpanish()); //replace english seed value with spanish seed value
                        System.out.println("Replaced: " + dictionaries.findOne(seedValue).getEnglish() + " With: " + dictionaries.findOne(seedValue).getSpanish()); //for console testing
                        count++;
                    } else if (count == 6) {
                        article.setSpan1(spanishArticle);  //once the count hits 6 save the spanish changes and save it back to the DB
                        articles.save(article);
                        break;
                    }

                }
            }
        }
    }

    public void parseDictionary() throws FileNotFoundException {

        if(dictionaries.count() == 0) {
            File f = new File("LanguageDB-V0.0.1.csv");
            Scanner scanner = new Scanner(f);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] arrayString = scanner.nextLine().split(",");
                Dictionary dictEntry = new Dictionary(arrayString[0], arrayString[1], arrayString[2]);
                dictionaries.save(dictEntry);
            }

            System.out.println("Language Dictionary has been created");
        } else {
            System.out.println("Language Dictionary already exists");
        }
    }
}
