package com.example;

public class JokeJava {

    /**
     * SOURCE FOR JOKES: http://www.techrepublic.com/article/the-geekiest-tech-jokes-on-the-internet/
     */

    String arr[] = {
            "There are 10 types of people in the world: those who understand binary, and those who don't.",
            "How many programmers does it take to change a light bulb? \n" +
                    "None. It's a hardware problem.",
            " A SEO couple had twins. For the first time they were happy with duplicate content.",
            " Why is it that programmers always confuse Halloween with Christmas? \n" +
                    "Because 31 OCT = 25 DEC",
            "Why do they call it hyper text? \n" +
                    "Too much JAVA.",
            " Why was the JavaScript developer sad? \n" +
                    "Because he didn't Node how to Express himself",
            "In order to understand recursion you must first understand recursion.",
            "Why do Java developers wear glasses? Because they can't C#",
            "What do you call 8 hobbits? \n" +
                    "A hobbyte\n"
    };

    public String getJoke() {
        int i = (int) (System.currentTimeMillis() % arr.length);
        return arr[i];
    }

}
